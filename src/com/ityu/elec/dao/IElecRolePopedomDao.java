package com.ityu.elec.dao;

import java.util.List;

import com.ityu.elec.domain.ElecPopedom;
import com.ityu.elec.domain.ElecRolePopedom;

public interface IElecRolePopedomDao extends ICommonDao<ElecRolePopedom>{

	public static final String SERVICE_NAME=
			"com.ityu.elec.dao.impl.ElecRolePopedomDaoImpl";
	
	public List<Object> findPopedomByRoleIDs(String condition);
}
