package com.ityu.elec.web.form;

import java.io.File;
import java.util.Date;

import com.ityu.elec.domain.ElecApplicationTemplate;
import com.ityu.elec.domain.ElecUser;


@SuppressWarnings("serial")
public class ElecApplicationVariables implements java.io.Serializable {
	
	
	private Integer applicationID;				//主键ID
	private String title;						//上传的文件标题
	private String path;						//上传的文件的存储路径
	private Date applyTime;						//申请日期
	private String status;						//审核状态
	private String processInstanceID;			//流程实例ID（流程实例ID，用于工作流中查看活动执行位置的流程图）
	
	
	/**映射申请模板的多对一的关系*/
	private ElecApplicationTemplate elecApplicationTemplate;
	
	/**映射用户的多对一的关系*/
	private ElecUser elecUser;
	
	
	public Integer getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(Integer applicationID) {
		this.applicationID = applicationID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ElecApplicationTemplate getElecApplicationTemplate() {
		return elecApplicationTemplate;
	}

	public void setElecApplicationTemplate(
			ElecApplicationTemplate elecApplicationTemplate) {
		this.elecApplicationTemplate = elecApplicationTemplate;
	}

	public ElecUser getElecUser() {
		return elecUser;
	}

	public void setElecUser(ElecUser elecUser) {
		this.elecUser = elecUser;
	}

	public String getProcessInstanceID() {
		return processInstanceID;
	}

	public void setProcessInstanceID(String processInstanceID) {
		this.processInstanceID = processInstanceID;
	}
	
	

}
