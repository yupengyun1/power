package com.ityu.elec.service.impl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ityu.elec.service.IElecProcessDefinitionService;
import com.ityu.elec.web.form.ProcessDefinitionBean;


@Service(IElecProcessDefinitionService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecProcessDefinitionServiceImpl implements IElecProcessDefinitionService {

	@Resource(name="processEngine")
	private ProcessEngine processEngine;
	
	/**  
	* @Name: findProcessDefinitionListByLastVersion
	* @Description: 查询所有最新版本的流程定义
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: List<ProcessDefinition>：封装流程定义集合
	*/
	public List<ProcessDefinition> findProcessDefinitionListByLastVersion() {
		//1:查询流程定义列表，按照版本的升序排列
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
									.createProcessDefinitionQuery()//
									.orderAsc(ProcessDefinitionQuery.PROPERTY_VERSION)//按照流程定义版本的升序排列
									.list();
		//2:使用Map集合，map集合的特点，当流程定义的key相同的情况下，后一次版本的流程定义对象将覆盖前一次版本的流程定义
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
		//3：遍历list，存放到map集合中
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				map.put(pd.getKey(), pd);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
		return pdList;
	}
	
	/**  
	* @Name: deployProcessDefinition
	* @Description: 使用zip格式的文件，完成流程定义的部署
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: ZipInputStream：zip文件输入流
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void deployProcessDefinition(ZipInputStream zipInputStream) {
		//2：使用ZipInPutStream完成部署
		processEngine.getRepositoryService()//
						.createDeployment()//
						.addResourcesFromZipInputStream(zipInputStream)//
						.deploy();
	}
	
	/**  
	* @Name: deleteProcessDefinition
	* @Description: 使用流程定义的key删除流程定义
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: ProcessDefinitionBean：存放流程定义的key
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void deleteProcessDefinition(
			ProcessDefinitionBean processDefinitionBean) {
		//获取流程定义的key
		String key = processDefinitionBean.getKey();
		try {
			key = URLDecoder.decode(key, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//使用流程定义key先查询对应key值所有版本的流程定义，返回List<ProcessDefintion>
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
									.createProcessDefinitionQuery()//
									.processDefinitionKey(key)//按照流程定义的key查询
									.list();
		//遍历List，获取每个部署对象，获取部署ID，执行删除
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				//获取部署ID
				String deploymentId = pd.getDeploymentId();
				//删除流程定义
				processEngine.getRepositoryService()//
									.deleteDeploymentCascade(deploymentId);
			}
		}
	}
	
	/**  
	* @Name: findInputStreamByImage
	* @Description: 使用传递的流程定义ID，获取对应流程定义的对象，查询流程图，将流程图放置到InputStream中
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: ProcessDefinitionBean：存放流程定义的id
	* @Return: InputStream:存放流程图的输入流
	*/
	public InputStream findInputStreamByImage(
			ProcessDefinitionBean processDefinitionBean) {
		//使用传递的流程定义ID，获取对应流程定义的对象
		String id = processDefinitionBean.getId();
		try {
			id = URLDecoder.decode(id, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ProcessDefinition pd = processEngine.getRepositoryService()//
							.createProcessDefinitionQuery()//
							.processDefinitionId(id)//
							.uniqueResult();
		//使用流程定义的对象，获取部署ID和资源图片名称，获取输入流对象（inputStream）
		//部署ID
		String deploymentId = pd.getDeploymentId();
		//资源图片名称
		String imageResourceName = pd.getImageResourceName();
		//存放资源图片
		InputStream inputStream = processEngine.getRepositoryService()//
									.getResourceAsStream(deploymentId, imageResourceName);
		return inputStream;
	}
}
