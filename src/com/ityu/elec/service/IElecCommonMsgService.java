package com.ityu.elec.service;

import com.ityu.elec.domain.ElecCommonMsg;

public interface IElecCommonMsgService {

	public static final String SERVICE_NAME = 
		"com.ityu.elec.service.impl.ElecCommonMsgServiceImpl"; 
	
	public ElecCommonMsg findCommonMsg();
	
	public void saveCommonMsg(ElecCommonMsg elecCommonMsg);
	
	
}
