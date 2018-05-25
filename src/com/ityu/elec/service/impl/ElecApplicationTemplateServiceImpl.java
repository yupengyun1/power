package com.ityu.elec.service.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ityu.elec.dao.IElecApplicationTemplateDao;
import com.ityu.elec.domain.ElecApplicationTemplate;
import com.ityu.elec.service.IElecApplicationTemplateService;
import com.ityu.elec.utils.FileUploadUtils;

@Service(IElecApplicationTemplateService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecApplicationTemplateServiceImpl implements IElecApplicationTemplateService {

	@Resource(name=IElecApplicationTemplateDao.SERVICE_NAME)
	private IElecApplicationTemplateDao elecApplicationTemplateDao;
	
	/**  
	* @Name: findElecApplicationTemplateList
	* @Description: 查询所有的申请模板集合
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: List<ElecApplicationTemplate>：申请模板集合
	*/
	public List<ElecApplicationTemplate> findElecApplicationTemplateList() {
		List<ElecApplicationTemplate> list = elecApplicationTemplateDao.findColletionByConditionNoPage("", null, null);
		return list;
	}
	
	/**  
	* @Name: saveApplicationTemplate
	* @Description: 保存申请模板
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: ElecApplicationTemplate：页面保存的参数
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveApplicationTemplate(
			ElecApplicationTemplate elecApplicationTemplate) {
		//1:在保存记录之前，先完成文件上传
		//上传的文件
		File upload = elecApplicationTemplate.getUpload();
		//上传文件的文件名
		String fileName = elecApplicationTemplate.getUploadFileName();
		//完成文件上传，同时返回相对路径的path
		String path = FileUploadUtils.fileUploadReturnPath(upload,fileName);
		//2:组织PO对象，保存到申请模板信息表中
		elecApplicationTemplate.setPath(path);
		elecApplicationTemplateDao.save(elecApplicationTemplate);
	}
	
	/**  
	* @Name: findElecApplicationTemplateByID
	* @Description: 使用申请模板ID，查询申请模板的详细信息
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: Integer：主键ID
	* @Return: ElecApplicationTemplate：存放申请模板信息
	*/
	public ElecApplicationTemplate findElecApplicationTemplateByID(Integer id) {
		ElecApplicationTemplate applicationTemplate = elecApplicationTemplateDao.findObjectById(id);
		return applicationTemplate;
	}
	
	/**  
	* @Name: updateApplicationTemplate
	* @Description: 更新保存申请模板
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: ElecApplicationTemplate：页面更新保存的参数
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void updateApplicationTemplate(
			ElecApplicationTemplate elecApplicationTemplate) {
		//获取页面的upload，判断是否上传了新的附件
		File upload = elecApplicationTemplate.getUpload();
		/**
		 * 说明上传了新的附件
		      * 删除之前的附件，使用页面传递的隐藏域path，删除之前的附件
		      * 上传新的附件，同时返回新的路径path，将新的路径path放置到PO对象中
		 */
		if(upload!=null){
			//获取页面隐藏域的path
			String oldPath = elecApplicationTemplate.getPath();
			//删除之前附件
			File oldFile = new File(ServletActionContext.getServletContext().getRealPath("")+oldPath);
			if(oldFile.exists()){
				oldFile.delete();
			}
			//上传新的附件
			//上传文件的文件名
			String fileName = elecApplicationTemplate.getUploadFileName();
			//完成文件上传，同时返回相对路径的path
			String path = FileUploadUtils.fileUploadReturnPath(upload,fileName);
			//将新的路径path放置到PO对象中
			elecApplicationTemplate.setPath(path);
		}
		//完成更新
		elecApplicationTemplateDao.update(elecApplicationTemplate);
	}
	
	/**  
	* @Name: deleteApplicationTemplateByID
	* @Description: 删除申请模板信息
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: ElecApplicationTemplate：页面更新保存的参数
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void deleteApplicationTemplateByID(Integer id) {
		//使用申请模板ID，查询申请模板信息，获取路径path，使用路径path，删除文件
		ElecApplicationTemplate applicationTemplate = elecApplicationTemplateDao.findObjectById(id);
		//获取路径path
		String path = applicationTemplate.getPath();
		//删除附件
		File oldFile = new File(ServletActionContext.getServletContext().getRealPath("")+path);
		if(oldFile.exists()){
			oldFile.delete();
		}
		//使用申请模板ID，删除数据库
		elecApplicationTemplateDao.deleteObjectByIds(id);
	}
}
