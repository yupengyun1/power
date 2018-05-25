package com.ityu.elec.domain;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public class ElecApplication implements java.io.Serializable {
	
	
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
	
	/**映射一对多的关联关系，与审核信息表*/
	Set<ElecApproveInfo> elecApproveInfos = new HashSet<ElecApproveInfo>();

	public Set<ElecApproveInfo> getElecApproveInfos() {
		return elecApproveInfos;
	}

	public void setElecApproveInfos(Set<ElecApproveInfo> elecApproveInfos) {
		this.elecApproveInfos = elecApproveInfos;
	}

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
	
	
	/************非持久化javabean的属性***********/
	
	/**添加静态属性，表示申请状态*/
	public static final String CHECK_RUNNING = "1";//审核中
	public static final String CHECK_REJECT = "2";//审核不通过
	public static final String CHECK_PASS = "3";//审核通过
	
	//申请模板ID
	private Integer applicationTemplateID;
	//存放审核人的登录名
	private String checkLogonName;
	//上传的申请文件
	private File upload;
	//上传的申请文件的文件名
	private String uploadFileName;
	//任务ID
	private String taskID;
	//申请文件的下载
	private InputStream inputStream;
	
	//审核意见
	private String comment;
	//是否同意
	private boolean approval;
	//连线的名称
	private String outcome;

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isApproval() {
		return approval;
	}

	public void setApproval(boolean approval) {
		this.approval = approval;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getCheckLogonName() {
		return checkLogonName;
	}

	public void setCheckLogonName(String checkLogonName) {
		this.checkLogonName = checkLogonName;
	}

	public Integer getApplicationTemplateID() {
		return applicationTemplateID;
	}

	public void setApplicationTemplateID(Integer applicationTemplateID) {
		this.applicationTemplateID = applicationTemplateID;
	}
	
	
}
