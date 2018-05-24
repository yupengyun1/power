package com.ityu.elec.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ityu.elec.dao.IElecExportFieldsDao;
import com.ityu.elec.domain.ElecExportFields;
import com.ityu.elec.service.IElecExportFieldsService;

@Service(IElecExportFieldsService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecExportFieldsServiceImpl implements IElecExportFieldsService{

	@Resource(name=IElecExportFieldsDao.SERVICE_NAME)
	private IElecExportFieldsDao elecExportFieldsDao;
	public ElecExportFields findElecExportFields(String belongTo) {
		ElecExportFields elecExportFields = elecExportFieldsDao.findObjectById(belongTo);
		return elecExportFields;
	}

	public void saveSetExportExcel(ElecExportFields elecExportFields) {
		elecExportFieldsDao.update(elecExportFields);
	}

}
