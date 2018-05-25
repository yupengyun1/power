package com.ityu.elec.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ityu.elec.dao.IElecApplicationDao;
import com.ityu.elec.dao.IElecApplicationTemplateDao;
import com.ityu.elec.dao.IElecApproveInfoDao;
import com.ityu.elec.dao.IElecSystemDDLDao;
import com.ityu.elec.domain.ElecApplication;
import com.ityu.elec.domain.ElecApplicationTemplate;
import com.ityu.elec.domain.ElecApproveInfo;
import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.service.IElecApplicationFlowService;
import com.ityu.elec.utils.DateUtils;
import com.ityu.elec.utils.FileUploadUtils;
import com.ityu.elec.web.form.ElecApplicationVariables;


@Service(IElecApplicationFlowService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecApplicationFlowServiceImpl implements IElecApplicationFlowService {

	//申请模板Dao
	@Resource(name=IElecApplicationTemplateDao.SERVICE_NAME)
	private IElecApplicationTemplateDao elecApplicationTemplateDao;
	//申请Dao
	@Resource(name=IElecApplicationDao.SERVICE_NAME)
	private IElecApplicationDao elecApplicationDao;
	//审核Dao
	@Resource(name=IElecApproveInfoDao.SERVICE_NAME)
	private IElecApproveInfoDao elecApproveInfoDao;
	//流程引擎
	@Resource(name="processEngine")
	private ProcessEngine processEngine;
	//数据字典的DAO
	@Resource(name=IElecSystemDDLDao.SERVICE_NAME)
	private IElecSystemDDLDao elecSystemDDLDao;
	
	/**  
	* @Name: saveApplication
	* @Description: 提交申请信息（保存申请记录，同时启动流程实例，同时完成当前人的个人任务）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: ElecApplication：存放申请信息
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveApplication(ElecApplication elecApplication) {
		/**1：使用申请模板ID，查询申请模板的详细信息（用作保存申请信息的外键）*/
		//申请模板ID
		Integer id = elecApplication.getApplicationTemplateID();
		ElecApplicationTemplate elecApplicationTemplate = elecApplicationTemplateDao.findObjectById(id);
		/**2：组织PO对象，保存申请记录信息*/
		this.saveElecApplication(elecApplication,elecApplicationTemplate);
		/**
		 * 3:从申请模板信息中，获取流程定义的key，使用流程定义的key启动流程实例，在启动流程实例的同时，要设置流程变量。
             * 流程变量的名称：叫做application（目的是动态的将任务的办理人，指定到第一个任务活动中）
		 * */
		//获取流程定义的key
		String key = elecApplicationTemplate.getProcessDefinitionKey();
		//启动流程实例，同时设置流程变量
		//流程变量，名称application，值表示申请对象中要存放elecUser对象的属性，elecUser对象的属性要存放logonName的属性值（登录名），即第一个任务application.elecUser.logonName的答案
		Map<String, Object> variables = new LinkedHashMap<String, Object>();
		ElecApplicationVariables elecApplicationVariables = this.copyElecApplicationVarables(elecApplication);
		variables.put("application", elecApplicationVariables);
		ProcessInstance pi = processEngine.getExecutionService()//
							.startProcessInstanceByKey(key, variables);
		/**
		 * 4：完成任务之前，先要指定下一个任务的办理人，用流程变量指定，流程变量的名称departmentManager，值就是页面选择的审核人
   			完成当前人的个人任务
		 * */
		//查询当前流程的第一个任务，流程启动后第一个活动只有1个任务
		Task task = processEngine.getTaskService()//
							.createTaskQuery()//
							.processInstanceId(pi.getId())//使用流程实例ID，获取当前流程的第一个任务
							.uniqueResult();
		//页面指定的审核人
		String checkLogonName = elecApplication.getCheckLogonName();
		if(StringUtils.isNotBlank(checkLogonName)){
			//设置流程变量
			variables.put("departmentManager",checkLogonName);
			processEngine.getTaskService()//
								.setVariables(task.getId(), variables);
		}
		//完成当前人的个人任务
		processEngine.getTaskService()//
						.completeTask(task.getId());
		/**5：将流程实例ID放置到PO对象中*/
		elecApplication.setProcessInstanceID(pi.getId());//使得流程关联申请信息
		
	}

	/**完成 从申请信息中的数据拷贝到流程变量中*/
	private ElecApplicationVariables copyElecApplicationVarables(
			ElecApplication elecApplication) {
		ElecApplicationVariables elecApplicationVariables = new ElecApplicationVariables();
		try {
			BeanUtils.copyProperties(elecApplicationVariables, elecApplication);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return elecApplicationVariables;
	}

	/**组织PO对象，保存申请记录信息，向申请信息表中添加数据*/
	private void saveElecApplication(ElecApplication elecApplication,
			ElecApplicationTemplate elecApplicationTemplate) {
		//从Session中获取用户信息
		ElecUser elecUser = (ElecUser)ServletActionContext.getRequest().getSession().getAttribute("globle_user");
		//组织PO对象
		//当前时间
		Date date = new Date();
		//申请文件标题(申请文件模板名称_申请人姓名_申请时间)
		elecApplication.setTitle(elecApplicationTemplate.getName()+"_"+elecUser.getUserName()+"_"+DateUtils.dateToString(date));
		//申请时间
		elecApplication.setApplyTime(date);
		//当前申请状态(状态设置成审核中)
		elecApplication.setStatus(ElecApplication.CHECK_RUNNING);
		//文件上传，同时返回路径path
		elecApplication.setPath(FileUploadUtils.fileUploadReturnPath(elecApplication.getUpload(), elecApplication.getUploadFileName()));
		
		//保存数据库表的外键列，针对hibernate来说，是对象的关联即可（elecApplicationTemplate和elecUser对象中必须存放ID，这样才能保存外键）
		//申请模板
		elecApplication.setElecApplicationTemplate(elecApplicationTemplate);
		//用户关联
		elecApplication.setElecUser(elecUser);
		//保存申请信息表
		elecApplicationDao.save(elecApplication);
	}
	
	/**  
	* @Name: findElecApplicationListByCondition
	* @Description: 指定查询条件，查询当前申请人的申请信息
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: ElecApplication：存放查询条件
	* @Return: List<ElecApplication>：申请信息集合
	*/
	public List<ElecApplication> findElecApplicationListByCondition(
			ElecApplication elecApplication) {
		//组成查询条件，查询申请信息表
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//申请模板
		Integer applicationTemplateId = elecApplication.getApplicationTemplateID();
		if(applicationTemplateId!=null){
			condition += " and o.elecApplicationTemplate.id=?";
			paramsList.add(applicationTemplateId);
		}
		//审核状态
		String status = elecApplication.getStatus();
		if(StringUtils.isNotBlank(status)){
			condition += " and o.status=?";
			paramsList.add(status);
		}
		//当前申请人
		//从Session中获取当前登录人的信息
		ElecUser elecUser = (ElecUser)ServletActionContext.getRequest().getSession().getAttribute("globle_user");
		condition += " and o.elecUser.logonName=?";
		paramsList.add(elecUser.getLogonName());
		
		Object [] params = paramsList.toArray();
		//排序，按申请时间降序排列（最后的申请在最前面）
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.applyTime", "desc");
		List<ElecApplication> list = elecApplicationDao.findColletionByConditionNoPage(condition, params, orderby);
		//在集合中遍历的ElecApplication中，存在数据字典的转换，将审核状态转换成对应的值
		this.initSystemDDL(list);
		return list;
	}

	/**数据字典的转换，将审核状态转换成对应的值*/
	private void initSystemDDL(List<ElecApplication> list) {
		if(list!=null && list.size()>0){
			for(ElecApplication application:list){
				//解决懒加载异常
				Hibernate.initialize(application.getElecUser());
				//使用数据类型和数据项的编号，获取数据项的值
				application.setStatus(elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("审核状态", application.getStatus()));
			}
		}
	}
	
	/**  
	* @Name: findMyTaskList
	* @Description: 获取当前登录人，需要审核的申请信息集合
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: ElecApplication：无
	* @Return: List<ElecApplication>：存放申请信息集合
	*/
	public List<ElecApplication> findMyTaskList() {
		//返回的结果集
		List<ElecApplication> applicationList = new ArrayList<ElecApplication>();
		//1：以当前登录人作为条件，查询正在执行的任务表，获取当前登录人需要办理的任务，返回List<Task>
		//从Session中获取当前登录人的信息
		ElecUser elecUser = (ElecUser)ServletActionContext.getRequest().getSession().getAttribute("globle_user");
		List<Task> list = processEngine.getTaskService()//
							.createTaskQuery()//
							.assignee(elecUser.getLogonName())//
							.list();
		//2：遍历List，获取每个任务Task，从每个任务对象中，获取对应的流程变量ElecApplicatonVariables，将流程变量转化成ElecApplicaton，组织List<ElecApplicaton>
		if(list!=null && list.size()>0){
			for(Task task:list){
				//任务ID
				String taskId = task.getId();
				//获取每个流程对应的流程变量
				ElecApplicationVariables elecApplicationVariables = (ElecApplicationVariables) processEngine.getTaskService()//
									.getVariable(taskId, "application");
				ElecApplication elecApplication = this.copyElecApplication(elecApplicationVariables);
				//赋值任务ID
				elecApplication.setTaskID(taskId);
				applicationList.add(elecApplication);
			}
		}
		return applicationList;
	}

	/**复制流程变量中的属性的值到申请信息对象中*/
	private ElecApplication copyElecApplication(
			ElecApplicationVariables elecApplicationVariables) {
		ElecApplication elecApplication = new ElecApplication();
		try {
			BeanUtils.copyProperties(elecApplication, elecApplicationVariables);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return elecApplication;
	}
	
	/**  
	* @Name: findOutComesByTaskID
	* @Description: 使用任务ID，查询当前任务执行下一步连线的名称集合
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: String：任务ID
	* @Return: Collection<String>：存放连线名称的集合
	*/
	public Collection<String> findOutComesByTaskID(String taskID) {
		Collection<String> outcomes = processEngine.getTaskService()//
								.getOutcomes(taskID);
		return outcomes;
	}
	
	/**  
	* @Name: findElecApplicationByID
	* @Description: 使用申请ID，查询申请表的详细信息
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: Integer：申请ID
	* @Return: ElecApplication：申请信息对象
	*/
	public ElecApplication findElecApplicationByID(Integer id) {
		ElecApplication application = elecApplicationDao.findObjectById(id);
		return application;
	}
	
	/**  
	* @Name: approve
	* @Description: 审核申请人的申请信息（保存审核信息表，完成当前人的个人任务，更新申请信息的状态）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: Integer：申请ID
	* @Return: ElecApplication：申请信息对象
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void approve(ElecApplication elecApplication) {
		/**1：使用申请ID，查询申请的详细信息，返回ElecApplication对象*/
		Integer applicationID = elecApplication.getApplicationID();
		ElecApplication application = elecApplicationDao.findObjectById(applicationID);
		//获取流程实例ID
		String processInstanceID = application.getProcessInstanceID();
		/**2：组织PO对象，向审核信息表中添加数据*/
		this.saveApproveInfo(elecApplication,application);
		/**3：在完成任务之前，先制定下一个任务的办理人，使用任务ID和页面传递的连线的名称，完成任务*/
		//获取任务ID
		String taskId = elecApplication.getTaskID();
		//连线名称
		String outcome = elecApplication.getOutcome();
		//设置下一个任务的流程变量
		String checkLogonName = elecApplication.getCheckLogonName();
		if(StringUtils.isNotBlank(checkLogonName)){
			//设置流程变量
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("generalManager", checkLogonName);
			processEngine.getTaskService()//
							.setVariables(taskId, variables);
		}
		//使用任务ID和页面传递的连线的名称，完成任务
		processEngine.getTaskService()//
							.completeTask(taskId, outcome);
		
		/**
		 * 使用流程实例ID，查询正在执行的流程实例，用来判断流程实例是否结束，且该方法一定要放置到完成任务之后
		 * pi==null:说明当前流程已经结束
		 * pi!=null:说明当前流程没有结束
		 */
		ProcessInstance pi = processEngine.getExecutionService()//
							.createProcessInstanceQuery()//
							.processInstanceId(processInstanceID)//使用流程实例ID查询
							.uniqueResult();
		/**
		 * 4：获取判断的结果，判断【同意】和【不同意】
				  如果【同意】
				       如果当前流程是最后一个活动(流程结束)，此时将申请信息表的审核状态从审核中-->审核通过
				  如果【不同意】
				      如果流程不是最后一个活动(流程没有结束)，此时就要强制终止流程
				      不管流程是否结束，都将申请信息表的审核状态从审核中-->审核不通过
		 * */
		boolean approval = elecApplication.isApproval();
		//选择【同意】
		if(approval){
			//如果流程结束
			if(pi==null){
				//此时将申请信息表的审核状态从审核中-->审核通过
				application.setStatus(ElecApplication.CHECK_PASS);
			}
		}
		//选择【不同意】
		else{
			//流程没有结束
			if(pi!=null){
				//此时就要强制终止流程
				processEngine.getExecutionService()//
									.endProcessInstance(processInstanceID, ProcessInstance.STATE_ENDED);
			}
			//不管流程是否结束，都将申请信息表的审核状态从审核中-->审核不通过
			application.setStatus(ElecApplication.CHECK_REJECT);
		}
	}

	/**组织PO对象，向审核信息表中添加数据
	 * VO:elecApplication
	 * PO:application
	 * */
	private void saveApproveInfo(ElecApplication elecApplication,
			ElecApplication application) {
		//从Session中获取用户对象
		ElecUser elecUser = (ElecUser)ServletActionContext.getRequest().getSession().getAttribute("globle_user");
		ElecApproveInfo elecApproveInfo = new ElecApproveInfo();
		//审核意见
		elecApproveInfo.setComment(elecApplication.getComment());
		//是否同意
		elecApproveInfo.setApproval(elecApplication.isApproval());
		//审核时间
		elecApproveInfo.setApproveTime(new Date());
		
		//关联申请信息对象
		elecApproveInfo.setElecApplication(application);
		//关联用户对象
		elecApproveInfo.setElecUser(elecUser);
		//执行保存
		elecApproveInfoDao.save(elecApproveInfo);
	}
	
	/**  
	* @Name: findApproveInfoListByApplicationID
	* @Description: 使用申请ID，作为查询条件，查询对应该申请的审核记录，返回一个List<ElecApproveInfo>
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: ElecApplication：申请ID
	* @Return:  List<ElecApproveInfo>:审核的历史记录集合
	*/
	public List<ElecApproveInfo> findApproveInfoListByApplicationID(
			ElecApplication elecApplication) {
		//获取申请ID
		Integer applicationID = elecApplication.getApplicationID();
		//组织查询条件
		String condition = " and o.elecApplication.applicationID=?";
		Object [] params = {applicationID};
		//排序：审批时间升序排列。
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.approveTime", "asc");
		List<ElecApproveInfo> list = elecApproveInfoDao.findColletionByConditionNoPage(condition, params, orderby);
		//解决懒加载异常
		if(list!=null && list.size()>0){
			for(ElecApproveInfo approveInfo:list){
				Hibernate.initialize(approveInfo.getElecUser());
			}
		}
		return list;
	}
	
	/**查询坐标和部署对象ID*/
	public void viewProcessPicture(ElecApplication elecApplication) {
		//获取申请ID
		Integer applicationID = elecApplication.getApplicationID();
		ElecApplication application = elecApplicationDao.findObjectById(applicationID);
		//获取审核状态，用来判断流程是否结束
		String status = application.getStatus();
		//获取流程实例ID
		String processInstanceId = application.getProcessInstanceID();

		// 1，获取当前正在执行的活动名称
		String deploymentId = null;
		String processDefinitionId = null;
		Set<String> activityNames = null;
		//表示流程正在执行
		if (status!=null && status.equals("1")) {
			// 正在执行的，就使用ExecutionService查询
			ProcessInstance pi = processEngine.getExecutionService()//
					.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//
					.uniqueResult();

			processDefinitionId = pi.getProcessDefinitionId();
			activityNames = new HashSet(pi.findActiveActivityNames()); // 当前正在执行的活动的名称
		} else {
			// 已执行完的，就查询历史
			HistoryProcessInstance hpi = processEngine.getHistoryService()//
					.createHistoryProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//
					.uniqueResult();

			processDefinitionId = hpi.getProcessDefinitionId();
			activityNames = new HashSet<String>();
			activityNames.add(hpi.getEndActivityName()); // 结束的活动名称

		}

		// 2，找出他们的坐标
		Set<ActivityCoordinates> coordList = new HashSet<ActivityCoordinates>();
		for (String activityName : activityNames) {
			if(StringUtils.isNotBlank(activityName)){
				ActivityCoordinates coord = processEngine.getRepositoryService().getActivityCoordinates(
						processDefinitionId, activityName);
				coordList.add(coord);
			}
			//表示jbpm4_hist_procinst表中的endActivity字段为null，则直接指向结束节点。
			else{
				ActivityCoordinates coord = processEngine.getRepositoryService().getActivityCoordinates(
						processDefinitionId, "end1");
				coordList.add(coord);
			}
		}
		ServletActionContext.getRequest().setAttribute("coordList", coordList);

		// 3，获取 deploymentId
		deploymentId = processEngine.getRepositoryService().createProcessDefinitionQuery()//
				.processDefinitionId(processDefinitionId)//
				.uniqueResult()//
				.getDeploymentId();
		ServletActionContext.getRequest().setAttribute("deploymentId", deploymentId);
	}
	
	/**获取输入流*/
	public InputStream findInputStreamByDeploymentId(String deploymentId) {
		String processImageName = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.deploymentId(deploymentId)//
				.uniqueResult()//
				.getImageResourceName();

		InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId,
				processImageName);
		return in;
	}

}
