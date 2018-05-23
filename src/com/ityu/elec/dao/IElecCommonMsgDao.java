package com.ityu.elec.dao;

import com.ityu.elec.domain.ElecCommonMsg;

public interface IElecCommonMsgDao extends ICommonDao<ElecCommonMsg> {

	public static final String SERVICE_NAME  = 
		"com.ityu.elec.dao.impl.ElecCommonMsgDaoImpl";
}
