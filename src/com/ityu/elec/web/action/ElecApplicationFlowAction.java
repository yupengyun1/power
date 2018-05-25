package com.ityu.elec.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ityu.elec.domain.ElecApplication;
import com.ityu.elec.domain.ElecApplicationTemplate;
import com.ityu.elec.domain.ElecApproveInfo;
import com.ityu.elec.domain.ElecSystemDDL;
import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.service.IElecApplicationFlowService;
import com.ityu.elec.service.IElecApplicationTemplateService;
import com.ityu.elec.service.IElecSystemDDLService;
import com.ityu.elec.service.IElecUserService;


@SuppressWarnings("serial")
@Controller("elecApplicationFlowAction")
@Scope(value="prototype")
public class ElecApplicationFlowAction extends BaseAction<ElecApplication> {

	private ElecApplication elecApplication = this.getModel();
	
	//申请模板
	@Resource(name=IElecApplicationTemplateService.SERVICE_NAME)
	private IElecApplicationTemplateService elecApplicationTemplateService;
	
	//申请、审核流程的Service
	@Resource(name=IElecApplicationFlowService.SERVICE_NAME)
	private IElecApplicationFlowService elecApplicationFlowService;
	
	//用户Service
	@Resource(name=IElecUserService.SERVICE_NAME)
	private IElecUserService elecUserService;
	
	//数据字典的Service
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	private IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: 起草申请的首页显示
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/flowTemplateList.jsp
	*/
	public String home(){
		//查询系统中所有的申请模板，遍历页面，返回List<ElecApplicationTemplate>
		List<ElecApplicationTemplate> list = elecApplicationTemplateService.findElecApplicationTemplateList();
		request.setAttribute("list", list);
		return "home";
	}
	
	/**  
	* @Name: submitApplication
	* @Description: 跳转到提交申请页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/flowSubmitApplication.jsp
	*/
	public String submitApplication(){
		//查询审核人是部门经理的职位，显示到页面上，返回List<ElecUser>，2表示部门经理
		List<ElecUser> userList = elecUserService.findElecUserByPostID("2");
		request.setAttribute("userList", userList);
		return "submitApplication";
	}
	
	/**  
	* @Name: saveApplication
	* @Description: 提交申请信息（保存申请记录）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 重定向到到workflow/flowMyApplicationList.jsp
	*/
	public String saveApplication(){
		elecApplicationFlowService.saveApplication(elecApplication);
		return "saveApplication";
	}
	
	/**  
	* @Name: myApplicationHome
	* @Description: 跳转到我的申请查询首页
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/flowMyApplicationList.jsp
	*/
	public String myApplicationHome(){
		//1：遍历所有的申请模板，显示到页面的下拉菜单中，返回List<ElecApplicationTemplate>
		List<ElecApplicationTemplate> templateList = elecApplicationTemplateService.findElecApplicationTemplateList();
		request.setAttribute("templateList", templateList);
		//2：遍历所有的审核状态，显示到页面的下拉菜单中，返回List<ElecSystemDDL>
		List<ElecSystemDDL> systemList = elecSystemDDLService.findElecSystemDDLListByKeyword("审核状态");
		request.setAttribute("systemList", systemList);
		//3:获取页面传递的查询条件，如果查询条件不为空，指定查询条件，查询当前人的申请信息
		   //如果查询条件为空，此时表示查询所有的当前人的申请记录,返回List<ElecApplication>
		List<ElecApplication> list = elecApplicationFlowService.findElecApplicationListByCondition(elecApplication);
		request.setAttribute("list", list);
		return "myApplicationHome";
	}
	
	/**  
	* @Name: myTaskHome
	* @Description: 跳转到待我审批的首页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/flowMyTaskList.jsp
	*/
	public String myTaskHome(){
		List<ElecApplication> list = elecApplicationFlowService.findMyTaskList();
		request.setAttribute("list", list);
		return "myTaskHome";
	}
	
	/**  
	* @Name: flowApprove
	* @Description: 跳转到审批处理页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/flowApprove.jsp
	*/
	public String flowApprove(){
		//1:使用任务ID，查询当前任务执行下一步连线的名称集合，将集合遍历在【请选择下一步】的下拉菜单中
		//获取任务ID
		String taskID = elecApplication.getTaskID();
		//Collection中存放的值，即连线的名称
		Collection<String> outcomes = elecApplicationFlowService.findOutComesByTaskID(taskID);
		request.setAttribute("outcomes", outcomes);
		//2：查询职位是总经理的的用户集合，返回List<ElecUser>，遍历在【请选择审核人】的下拉菜单中，1表示总经理
		List<ElecUser> userList = elecUserService.findElecUserByPostID("1");
		request.setAttribute("userList", userList);
		return "flowApprove";
	}
	
	/**  
	* @Name: download
	* @Description: 文件下载
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 使用struts2的方式
	*/
	public String download(){
		try {
			//1：使用申请ID，查询申请的详细信息，从而获取申请的对象，获取路径，将对应的文件写到输入流中InputStream
			Integer id = elecApplication.getApplicationID();
			ElecApplication application = elecApplicationFlowService.findElecApplicationByID(id);
			//获取路径path
			String path = application.getPath();
			//将文件放置到输入流中
			InputStream in = new FileInputStream(new File(ServletActionContext.getServletContext().getRealPath("")+path));
			//获取表单标题的名称
			String name = application.getTitle();
			//处理response中的乱码问题
			name = new String(name.getBytes("gbk"),"iso-8859-1");
			//将name放置到request中，并且命名为filename
			request.setAttribute("filename", name);
			//将输入流中存放的文件，放置到栈顶对象的InputStream中
			elecApplication.setInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "download";
	}
	
	/**  
	* @Name: approve
	* @Description: 领导对申请信息进行审批处理
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 重定向到workflow/flowMyTaskList.jsp
	*/
	public String approve(){
		elecApplicationFlowService.approve(elecApplication);
		return "approve";
	}
	
	/**  
	* @Name: approvedHistory
	* @Description: 查看审核历史记录
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-28（创建日期）
	* @Parameters: 无
	* @Return: 重定向到workflow/flowApprovedHistory.jsp
	*/
	public String approvedHistory(){
		//使用申请ID，作为查询条件，查询对应该申请的审核记录，返回一个List<ElecApproveInfo>
		List<ElecApproveInfo> list = elecApplicationFlowService.findApproveInfoListByApplicationID(elecApplication);
		request.setAttribute("list", list);
		return "approvedHistory";
	}
	
	
	/**查看动态流程图*/
	public String approvedHistoryPic(){
		/**查看流程图的位置*/
		elecApplicationFlowService.viewProcessPicture(elecApplication);
		
		return "approvedHistoryPic";
	}
	
	/**查看流程图*/
	public String processImage(){
		try {
			String deploymentId = request.getParameter("deploymentId");
			InputStream in = elecApplicationFlowService.findInputStreamByDeploymentId(deploymentId);
			IOUtils.copy(in, response.getOutputStream());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	
}
