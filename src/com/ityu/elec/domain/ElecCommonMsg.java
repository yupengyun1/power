package com.ityu.elec.domain;

import java.util.Date;

public class ElecCommonMsg implements java.io.Serializable{

	//����ID
	private String comID;
	//վ���������
	private String stationRun;
	//�豸�������
	private String devRun;
	//��������
	private Date createDate;
	public String getComID() {
		return comID;
	}
	public void setComID(String comID) {
		this.comID = comID;
	}
	public String getStationRun() {
		return stationRun;
	}
	public void setStationRun(String stationRun) {
		this.stationRun = stationRun;
	}
	public String getDevRun() {
		return devRun;
	}
	public void setDevRun(String devRun) {
		this.devRun = devRun;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}
