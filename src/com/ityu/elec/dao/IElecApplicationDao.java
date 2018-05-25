package com.ityu.elec.dao;

import com.ityu.elec.domain.ElecApplication;


public interface IElecApplicationDao extends ICommonDao<ElecApplication> {

	public static final String SERVICE_NAME = "com.ityu.elec.dao.impl.ElecApplicationDaoImpl";
	
}
