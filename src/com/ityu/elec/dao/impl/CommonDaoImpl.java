 package com.ityu.elec.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ityu.elec.dao.ICommonDao;
import com.ityu.elec.utils.PageInfo;
import com.ityu.elec.utils.TUtil;

public class CommonDaoImpl<T> extends HibernateDaoSupport implements ICommonDao<T> {

	Class entityClass = TUtil.getActualType(this.getClass());

	@Resource(name="sessionFactory")
	public void setDi(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
	public void deleteObjectByCollection(List<T> list) {

		this.getHibernateTemplate().deleteAll(list);
	}

	public void deleteObjectByIds(Serializable... ids) {

		if(ids!=null&&ids.length>0){
			for (Serializable id : ids) {
				T entity = this.findObjectById(id);
				this.getHibernateTemplate().delete(entity);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findColletionByConditionNoPage(String condition,
			final Object[] params, Map<String, String> orderby) {

		String hql = " from "+entityClass.getSimpleName()+" o where 1=1 ";
		String orderByCondition = this.orderByHql(orderby);
		final String finalHql = hql + condition + orderByCondition;
		System.out.println(finalHql);
		List<T> list = (List<T>) this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(finalHql);
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				return query.list();
			}
		});
		return list;
	}

	public T findObjectById(Serializable id) {

		
		return (T)this.getHibernateTemplate().get(entityClass, id);
	}

	public void save(T entity) {

		this.getHibernateTemplate().save(entity);
	}

	public void update(T entity) {

		this.getHibernateTemplate().update(entity);
	}
	
	private String orderByHql(Map<String,String> orderby){
		 StringBuffer buffer = new StringBuffer();
		 if(orderby!=null && orderby.size()>0){
			 buffer.append(" ORDER BY ");
			 for (Map.Entry<String, String> map : orderby.entrySet()) {
				 buffer.append(map.getKey()+" "+map.getValue()+",");
			}
			 buffer.deleteCharAt(buffer.length()-1);
		 }
		return buffer.toString();
	}
	public List<T> findCollectionByConditionNoPageWithCache(String condition,
			final Object[] params, Map<String, String> orderby) {
		//hql语句
		String hql = "from "+entityClass.getSimpleName()+" o where 1=1 ";
		//将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderByHql(orderby);
		//添加查询条件
		final String finalHql = hql + condition + orderbyCondition;
		
		/**方案三*/
		List<T> list = (List<T>) this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(finalHql);
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				query.setCacheable(true);
				return query.list();
			}
			
		});
		return list;
	}
	public List findCollectionByConditionNoPageWithSelectCondition(
			String condition, Object[] params, Map<String, String> orderby,
			String selectCondition) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<T> findCollectionByConditionWithPage(String condition,
			Object[] params, Map<String, String> orderby) {
		// TODO Auto-generated method stub
		return null;
	}
	public void saveList(List<T> list) {
		// TODO Auto-generated method stub
		
	}
	public List<T> findCollectionByConditionWithPage(String condition,
			final Object[] params, Map<String, String> orderby,final PageInfo pageInfo) {
		//hql语句
		String hql = "from "+entityClass.getSimpleName()+" o where 1=1 ";
		//将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderByHql(orderby);
		//添加查询条件
		final String finalHql = hql + condition + orderbyCondition;

		List<T> list = (List<T>) this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(finalHql);
				if (params != null && params.length > 0) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				/**2014-12-9,添加分页 begin*/
				pageInfo.setTotalResult(query.list().size());//初始化总的记录数
				query.setFirstResult(pageInfo.getBeginResult());//当前页从第几条开始检索，默认是0,0是第一条
				query.setMaxResults(pageInfo.getPageSize());//当前页最多显示多少条记录
				/**2014-12-9,添加分页 end*/
				return query.list();
			}

		});
		return list;
	}
}
