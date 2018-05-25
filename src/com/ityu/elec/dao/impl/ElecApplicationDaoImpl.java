package com.ityu.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecApplicationDao;
import com.ityu.elec.domain.ElecApplication;



@Repository(IElecApplicationDao.SERVICE_NAME)
public class ElecApplicationDaoImpl extends CommonDaoImpl<ElecApplication> implements IElecApplicationDao {
	
}
