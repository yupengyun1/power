package com.ityu.elec.dao;

import java.util.List;
import java.util.Map;

import com.ityu.elec.domain.ElecFileUpload;



public interface IElecFileUploadDao extends ICommonDao<ElecFileUpload> {
	
	public static final String SERVICE_NAME = "com.ityu.elec.dao.impl.ElecFileUploadDaoImpl";

	List<ElecFileUpload> findFileUploadListByCondition(String condition,
			Object[] params, Map<String, String> orderby);

	
	
}
