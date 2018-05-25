package com.ityu.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecApproveInfoDao;
import com.ityu.elec.domain.ElecApproveInfo;

@Repository(IElecApproveInfoDao.SERVICE_NAME)
public class ElecApproveInfoDaoImpl extends CommonDaoImpl<ElecApproveInfo> implements IElecApproveInfoDao {
	
}
