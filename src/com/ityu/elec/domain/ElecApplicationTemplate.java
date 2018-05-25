package com.ityu.elec.domain;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public class ElecApplicationTemplate implements java.io.Serializable {
	
	private Integer id;						//主键ID
	private String name;					//名称
	private String processDefinitionKey;	//流程定义的key
	private String path;					//上传的模板文件的存储位置
	
	/**映射一对多的关联关系，与申请信息表*/
	private Set<ElecApplication> elecApplications = new HashSet<ElecApplication>();
	
	public Set<ElecApplication> getElecApplications() {
		return elecApplications;
	}
	public void setElecApplications(Set<ElecApplication> elecApplications) {
		this.elecApplications = elecApplications;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}
	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	/****************非持久化javabean的属性*************************/
	//上传的模板文件
	private File upload;
	//上传的模板文件的文件名称
	private String uploadFileName;
	//申请模板文件下载
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	
	
	
	
}
