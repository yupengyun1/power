package com.ityu.elec.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecSystemDDLDao;
import com.ityu.elec.domain.ElecSystemDDL;



@Repository(IElecSystemDDLDao.SERVICE_NAME)
public class ElecSystemDDLDaoImpl  extends CommonDaoImpl<ElecSystemDDL> implements IElecSystemDDLDao {

	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		//返回的List集合
		List<ElecSystemDDL> systemList = new ArrayList<ElecSystemDDL>();
		/**使用hql语句直接将投影查询的字段放置到对象中*/
		String hql = "SELECT DISTINCT new com.ityu.elec.domain.ElecSystemDDL(o.keyword) FROM ElecSystemDDL o";
		systemList = this.getHibernateTemplate().find(hql);
		return systemList;
	}
	
	public String findDdlNameByKeywordAndDdlCode(final String keyword, final String ddlCode) {
		final String hql = "select o.ddlName from ElecSystemDDL o where o.keyword=? and o.ddlCode=?";
		List<Object> list = (List<Object>) this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameter(0, keyword);
				query.setParameter(1, Integer.parseInt(ddlCode));

				//启用查询缓存
				query.setCacheable(true);
				return query.list();
			}
			
		});
		//返回数据项的值
		String ddlName = "";
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			ddlName = o.toString();
		}
		return ddlName;
	}
	
	public String findDdlCodeByKeywordAndDdlName(final String keyword, final String ddlName) {
		final String hql = "select o.ddlCode from ElecSystemDDL o where o.keyword=? and o.ddlName=?";
		List<Object> list = (List<Object>) this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameter(0, keyword);
				query.setParameter(1, ddlName);
				//启用查询缓存
				query.setCacheable(true);
				return query.list();
			}

		});
		//返回数据项的值
		String ddlCode = "";
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			ddlCode = o.toString();
		}
		return ddlCode;
	}

}
