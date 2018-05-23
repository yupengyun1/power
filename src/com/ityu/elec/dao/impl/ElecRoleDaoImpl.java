package com.ityu.elec.dao.impl;



import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecRoleDao;
import com.ityu.elec.domain.ElecRole;

@Repository(IElecRoleDao.SERVICE_NAME)
public class ElecRoleDaoImpl extends CommonDaoImpl<ElecRole> implements IElecRoleDao{

	
}
