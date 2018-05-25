package com.ityu.elec.web.form;

import java.io.File;
import java.io.InputStream;

public class ProcessDefinitionBean {

	//用做部署流程定义的文件（zip格式）
	private File upload;
	//流程定义的key
	private String key;
	//完成文件下载(查看流程图)
	private InputStream inputStream;
	//流程定义的id
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}
	
}
