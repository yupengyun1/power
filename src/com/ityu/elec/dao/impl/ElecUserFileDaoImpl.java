package com.ityu.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecUserFileDao;
import com.ityu.elec.domain.ElecUserFile;



@Repository(IElecUserFileDao.SERVICE_NAME)
public class ElecUserFileDaoImpl  extends CommonDaoImpl<ElecUserFile> implements IElecUserFileDao {

	
}
