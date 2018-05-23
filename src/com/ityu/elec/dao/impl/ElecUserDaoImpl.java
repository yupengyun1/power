package com.ityu.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecUserDao;
import com.ityu.elec.domain.ElecUser;



@Repository(IElecUserDao.SERVICE_NAME)
public class ElecUserDaoImpl  extends CommonDaoImpl<ElecUser> implements IElecUserDao {

	
}
