package com.ityu.elec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ityu.elec.utils.PageInfo;

public interface ICommonDao<T> {

	public void save(T entity);
	
	public void update(T entity);
	
	public T findObjectById(Serializable id);
	
	public void deleteObjectByIds(Serializable... ids);
	
	public void deleteObjectByCollection(List<T> list);
	
	public List<T> findColletionByConditionNoPage(String condition,Object[] params,Map<String,String> orderby);
	
	public List<T> findCollectionByConditionNoPageWithCache(String condition,Object[] params,Map<String,String> orderby);
	
	public List<T> findCollectionByConditionWithPage(String condition,Object[] params,Map<String,String> orderby);
	
	public List findCollectionByConditionNoPageWithSelectCondition(String condition,Object[] params, Map<String, String> orderby, String selectCondition);
	
	public void saveList(List<T> list);
	
	public List<T> findCollectionByConditionWithPage(String condition,
			final Object[] params, Map<String, String> orderby,final PageInfo pageInfo);
}
