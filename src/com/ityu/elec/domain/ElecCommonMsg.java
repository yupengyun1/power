package com.ityu.elec.domain;

import java.util.Date;

public class ElecCommonMsg implements java.io.Serializable{

	//主键ID
	private String comID;
	//站点运行情况
	private String stationRun;
	//设备运行情况
	private String devRun;
	//创建日期
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
