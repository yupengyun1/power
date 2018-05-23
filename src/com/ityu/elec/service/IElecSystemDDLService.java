package com.ityu.elec.service;

import java.util.List;

import com.ityu.elec.domain.ElecSystemDDL;




public interface IElecSystemDDLService {
	public static final String SERVICE_NAME = "com.ityu.elec.service.impl.ElecSystemDDLServiceImpl";

	List<ElecSystemDDL> findSystemDDLListByDistinct();

	List<ElecSystemDDL> findSystemDDLListByKeyword(String keyword);

	void saveSystemDDL(ElecSystemDDL elecSystemDDL);

	String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode);

	String findDdlCodeByKeywordAndDdlName(String keywrod, String ddlName);


}
