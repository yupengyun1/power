package com.ityu.elec.service;

import com.ityu.elec.domain.ElecExportFields;

public interface IElecExportFieldsService {

	public static final String SERVICE_NAME = "com.ityu.elec.service.impl.ElecExportFieldsServiceImpl";

	ElecExportFields findElecExportFields(String belongTo);

	void saveSetExportExcel(ElecExportFields elecExportFields);

}