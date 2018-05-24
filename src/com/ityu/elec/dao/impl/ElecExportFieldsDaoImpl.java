package com.ityu.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecExportFieldsDao;
import com.ityu.elec.domain.ElecExportFields;

@Repository(IElecExportFieldsDao.SERVICE_NAME)
public class ElecExportFieldsDaoImpl extends CommonDaoImpl<ElecExportFields> implements IElecExportFieldsDao{

}
