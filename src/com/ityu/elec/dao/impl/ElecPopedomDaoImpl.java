package com.ityu.elec.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecPopedomDao;
import com.ityu.elec.dao.IElecRolePopedomDao;
import com.ityu.elec.domain.ElecPopedom;

@Repository(IElecPopedomDao.SERVICE_NAME)
public class ElecPopedomDaoImpl extends CommonDaoImpl<ElecPopedom> implements IElecPopedomDao{

}
