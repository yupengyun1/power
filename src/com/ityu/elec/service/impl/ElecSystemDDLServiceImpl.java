package com.ityu.elec.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ityu.elec.dao.IElecSystemDDLDao;
import com.ityu.elec.domain.ElecSystemDDL;
import com.ityu.elec.service.IElecSystemDDLService;



@Service(IElecSystemDDLService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecSystemDDLServiceImpl implements IElecSystemDDLService {

	/**数据字典表Dao*/
	@Resource(name=IElecSystemDDLDao.SERVICE_NAME)
	IElecSystemDDLDao elecSystemDDLDao;

	/**  
	* @Name: findSystemDDLListByDistinct
	* @Description: 查询数据字典，去掉重复值
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-1（创建日期）
	* @Parameters: 无
	* @Return: List<ElecSystemDDL>：存放数据类型的集合
	*/
	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		List<ElecSystemDDL> list = elecSystemDDLDao.findSystemDDLListByDistinct();
		return list;
	}
	
	/**  
	* @Name: findSystemDDLListByKeyword
	* @Description: 以数据类型作为条件，查询数据字典
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-1（创建日期）
	* @Parameters: String：数据类型
	* @Return: List<ElecSystemDDL>：存放数据字典的集合
	*/
	public List<ElecSystemDDL> findSystemDDLListByKeyword(String keyword) {
		//查询条件
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(keyword)){
			condition += " and o.keyword=?";
			paramsList.add(keyword);
		}
		Object [] params = paramsList.toArray();
		//排序
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.ddlCode", "asc");
		//启用二级缓存的查询缓存
		List<ElecSystemDDL> list = elecSystemDDLDao.findCollectionByConditionNoPageWithCache(condition, params, orderby);
		return list;
	}
	
	/**  
	* @Name: saveSystemDDL
	* @Description: 保存数据字典
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-1（创建日期）
	* @Parameters: ElecSystemDDL：VO对象
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveSystemDDL(ElecSystemDDL elecSystemDDL) {
		//1：获取页面传递的参数
		//数据类型
		String keyword = elecSystemDDL.getKeywordname();
		//业务标识
		String typeflag = elecSystemDDL.getTypeflag();
		//数据项的值（数组）
		String itemnames [] = elecSystemDDL.getItemname();
		//2：获取判断业务逻辑的标识（new和add）
		// 如果typeflag==new：新增一种新的数据类型
		if(typeflag!=null && typeflag.equals("new")){
			//* 遍历页面传递过来的数据项的名称，组织PO对象，执行保存
			this.saveDDL(keyword,itemnames);
		}
		// 如果typeflag==add：在已有的数据类型基础上进行编辑和修改
		else{
			//* 使用数据类型，查询该数据类型对应的list，删除list
			List<ElecSystemDDL> list = this.findSystemDDLListByKeyword(keyword);
			elecSystemDDLDao.deleteObjectByCollection(list);
		    //* 遍历页面传递过来的数据项的名称，组织PO对象，执行保存
			this.saveDDL(keyword, itemnames);
		}
		    
		
	}

	private void saveDDL(String keyword, String[] itemnames) {
		if(itemnames!=null && itemnames.length>0){
			for(int i=0;i<itemnames.length;i++){
				//组织PO对象，执行保存
				ElecSystemDDL systemDDL = new ElecSystemDDL();
				systemDDL.setKeyword(keyword);
				systemDDL.setDdlCode(i+1);
				systemDDL.setDdlName(itemnames[i]);
				elecSystemDDLDao.save(systemDDL);
			}
		}
	}
	
	public String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode) {
		return elecSystemDDLDao.findDdlNameByKeywordAndDdlCode(keyword, ddlCode);
	}

	
	public String findDdlCodeByKeywordAndDdlName(String keyword, String ddlName) {
		return elecSystemDDLDao.findDdlCodeByKeywordAndDdlName(keyword, ddlName);
	}
}
