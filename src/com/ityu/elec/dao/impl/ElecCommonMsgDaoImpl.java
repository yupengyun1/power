package com.ityu.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecCommonMsgDao;
import com.ityu.elec.domain.ElecCommonMsg;

@Repository(IElecCommonMsgDao.SERVICE_NAME)
public class ElecCommonMsgDaoImpl extends CommonDaoImpl<ElecCommonMsg> implements IElecCommonMsgDao{

}
