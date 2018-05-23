package com.ityu.elec.dao;

import java.util.List;
import java.util.Map;

import com.ityu.elec.domain.ElecSystemDDL;


public interface IElecSystemDDLDao extends ICommonDao<ElecSystemDDL> {
	
	public static final String SERVICE_NAME = "com.ityu.elec.dao.impl.ElecSystemDDLDaoImpl";

	List<ElecSystemDDL> findSystemDDLListByDistinct();

	String findDdlNameByKeywordAndDdlCode(String keywrod, String ddlCode);

	String findDdlCodeByKeywordAndDdlName(String keyword, String ddlName);

	List<ElecSystemDDL> findCollectionByConditionNoPageWithCache(
			String condition, Object[] params, Map<String, String> orderby);

	
	

	

	
	
}
