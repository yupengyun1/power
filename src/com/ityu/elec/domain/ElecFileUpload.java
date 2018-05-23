package com.ityu.elec.domain;

import java.io.File;
import java.io.InputStream;

public class ElecFileUpload {

	private Integer seqId;//主键ID
	private String projId;//工程ID/所属单位
	private String belongTo;//所属模块/图纸类别
	private String fileName;//文件名
	private String fileUrl;//文件上传的路径
	private String progressTime;//上传的时间
	private String comment;//文件的描述
	public Integer getSeqId() {
		return seqId;
	}
	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}
	
	public String getProjId() {
		return projId;
	}
	public void setProjId(String projId) {
		this.projId = projId;
	}
	public String getBelongTo() {
		return belongTo;
	}
	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getProgressTime() {
		return progressTime;
	}
	public void setProgressTime(String progressTime) {
		this.progressTime = progressTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	private String queryString;//页面上；按文件名称和描述查询
	
	private File [] uploads;//文件上传的file
	
	private String [] uploadsFileName;//文件上传的文件名
	
	private String [] comments;//上传的文件描述
	
	private InputStream inputStream;//文件下载
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public File[] getUploads() {
		return uploads;
	}
	public void setUploads(File[] uploads) {
		this.uploads = uploads;
	}
	public String[] getUploadsFileName() {
		return uploadsFileName;
	}
	public void setUploadsFileName(String[] uploadsFileName) {
		this.uploadsFileName = uploadsFileName;
	}
	public String[] getComments() {
		return comments;
	}
	public void setComments(String[] comments) {
		this.comments = comments;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}
