package com.ityu.elec.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecRolePopedomDao;
import com.ityu.elec.domain.ElecRolePopedom;

@Repository(IElecRolePopedomDao.SERVICE_NAME)
public class ElecRolePopedomDaoImpl extends CommonDaoImpl<ElecRolePopedom> implements IElecRolePopedomDao{



	public List<Object> findPopedomByRoleIDs(String condition) {
		String hql = "SELECT DISTINCT o.mid FROM ElecRolePopedom o WHERE 1=1 AND o.roleID IN ("+condition+")";
		List<Object> list = this.getHibernateTemplate().find(hql);
		return list;
	}



	
}
