package com.ityu.elec.dao;

import com.ityu.elec.domain.ElecText;

public interface IElecTextDao extends ICommonDao<ElecText>{

	public static final String SERVICE_NAME = 
		"com.ityu.elec.dao.impl.ElecTextDaoImpl";
}
