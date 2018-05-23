package com.ityu.elec.dao;

import com.ityu.elec.domain.ElecCommonMsgContent;

public interface IElecCommonMsgContentDao extends ICommonDao<ElecCommonMsgContent> {

	public static final String SERVICE_NAME = 
		"com.ityu.elec.dao.impl.ElecCommonMsgContentDaoImpl";
}
