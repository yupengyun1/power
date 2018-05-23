package com.ityu.elec.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.ityu.elec.dao.IElecPopedomDao;
import com.ityu.elec.dao.IElecRoleDao;
import com.ityu.elec.dao.IElecRolePopedomDao;
import com.ityu.elec.dao.IElecUserDao;
import com.ityu.elec.domain.ElecPopedom;
import com.ityu.elec.domain.ElecRole;
import com.ityu.elec.domain.ElecRolePopedom;
import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.service.IElecRoleService;

@Service(IElecRoleService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecRoleServiceImpl implements IElecRoleService{

	/**用户表Dao*/
	@Resource(name=IElecUserDao.SERVICE_NAME)
	IElecUserDao elecUserDao;
	
	/**角色表Dao*/
	@Resource(name=IElecRoleDao.SERVICE_NAME)
	IElecRoleDao elecRoleDao;
	
	/**权限表Dao*/
	@Resource(name=IElecPopedomDao.SERVICE_NAME)
	IElecPopedomDao elecPopedomDao;
	
	
	/**角色权限表Dao*/
	@Resource(name=IElecRolePopedomDao.SERVICE_NAME)
	IElecRolePopedomDao elecRolePopedomDao;
	
	

	public List<ElecRole> findAllRoleList() {

		Map<String, String> orderby = new LinkedHashMap<String,String>();
		orderby.put("o.roleID", "asc");
		List<ElecRole> list = elecRoleDao.findColletionByConditionNoPage("",null,orderby);
		return list;
	}

	public List<ElecPopedom> findAllPopedomList() {

		String condition = " and o.pid=?";
		Object [] params = {"0"};
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String,String>();
		orderby.put("o.mid", "asc");
		List<ElecPopedom> list = elecPopedomDao.findColletionByConditionNoPage(condition, params, orderby);
		if(list!=null && list.size()>0){
			for (ElecPopedom elecPopedom : list) {
				String mid = elecPopedom.getMid();
				String condition1 = " and o.pid=?";
				Object[] params1 = {mid};
				Map<String, String> orderby1 = new LinkedHashMap<String,String>();
				orderby.put("o.mid", "asc");
				List<ElecPopedom> list1 = elecPopedomDao.findColletionByConditionNoPage(condition1, params1, orderby1);
				elecPopedom.setList(list1);
			}
		}
		return list;
	}

	public List<ElecPopedom> findAllPopedomListByRoleID(String roleID) {
		List<ElecPopedom> list = this.findAllPopedomList();
		String condition = " and o.roleID=?";
		Object[] params = {roleID};
		List<ElecRolePopedom> popedomList = elecRolePopedomDao.findColletionByConditionNoPage(condition, params, null);
		StringBuffer popedomBuffer = new StringBuffer();
		if(popedomList!=null && popedomList.size()>0){
			for (ElecRolePopedom elecRolePopedom : popedomList) {
				String mid = elecRolePopedom.getMid();
				popedomBuffer.append(mid).append("@");
			}
			popedomBuffer.deleteCharAt(popedomBuffer.length()-1);
		}
		String popedom = popedomBuffer.toString();
		this.findPopedomResult(popedom,list);
		return list;
	}

	public List<ElecUser> findAllUserListByRoleID(String roleID) {
		Map<String, String> orderby = new LinkedHashMap<String,String>();
		orderby.put("o.onDutyDate", "asc");
		List<ElecUser> list = elecUserDao.findColletionByConditionNoPage("", null, orderby);
		ElecRole elecRole = elecRoleDao.findObjectById(roleID);
		Set<ElecUser> elecUsers = elecRole.getElecUsers();
		if(list!=null && list.size()>0){
			for (ElecUser elecUser : elecUsers) {
				String userID = elecUser.getUserID();
				if(elecUsers!=null && elecUsers.size()>0){
					for (ElecUser elecUser2 : elecUsers) {
						String userID2 = elecUser2.getUserID();
						if(userID.equals(userID2)){
							elecUser.setFlag("1");
						}else{
							elecUser.setFlag("2");
						}
					}
				}
			}
		}
		return list;
	
	}

	private void findPopedomResult(String popedom, List<ElecPopedom> list) {

		if(list!=null && list.size()>0){
			for (ElecPopedom elecPopedom : list) {
				String mid = elecPopedom.getMid();
				if(popedom.contains(mid)){
					elecPopedom.setFlag("1");
				}else {
					elecPopedom.setFlag("2");
				}
				List<ElecPopedom> childList = elecPopedom.getList();
				if(childList!=null && childList.size()>0){
					this.findPopedomResult(popedom, childList);
				}
			}
		}
		
	}

	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveRole(ElecPopedom elecPopedom) {
		String roleID = elecPopedom.getRoleID();
		String[] selectopers = elecPopedom.getSelectoper();
		String[] selectusers = elecPopedom.getSelectuser();
		this.saveRolePopedom(roleID,selectopers);
		this.saveUserRole(roleID,selectusers);
		
	}

	private void saveUserRole(String roleID, String[] selectusers) {

		ElecRole elecRole = elecRoleDao.findObjectById(roleID);
		HashSet<ElecUser> elecUsers = new HashSet<ElecUser>();
		if(selectusers!=null && selectusers.length>0){
			for (String userID : selectusers) {
				ElecUser elecUser = new ElecUser();
				elecUser.setUserID(userID);
				elecUsers.add(elecUser);
			}
		}
		elecRole.setElecUsers(elecUsers);
	}

	private void saveRolePopedom(String roleID, String[] selectopers) {

		String condition = " and roleID=?";
		Object[] params={roleID};
		List<ElecRolePopedom> list = elecRolePopedomDao.findColletionByConditionNoPage(condition, params, null);
		elecRolePopedomDao.deleteObjectByCollection(list);
		if(selectopers!=null && selectopers.length>0){
			for (String ids : selectopers) {
				String[] arrays = ids.split("_");
				ElecRolePopedom elecRolePopedom = new ElecRolePopedom();
				elecRolePopedom.setRoleID(roleID);
				elecRolePopedom.setMid(arrays[1]);
				elecRolePopedom.setPid(arrays[0]);
				elecRolePopedomDao.save(elecRolePopedom);
			}
		}
	}

	public String findPopedomByRoleIDs(Hashtable<String, String> ht) {
		StringBuffer buffercondition = new StringBuffer();
		if(ht!=null && ht.size()>0){
			Iterator<Entry<String, String>> iterator = ht.entrySet().iterator();
			boolean hasNext = iterator.hasNext();
			if(hasNext){
				Entry<String, String> entry = iterator.next();
				buffercondition.append("'").append(entry.getKey()).append("'").append(",");
			}
			buffercondition.deleteCharAt(buffercondition.length()-1);
		}
		String condition = buffercondition.toString();
		List<Object> list = elecRolePopedomDao.findPopedomByRoleIDs(condition);
		StringBuffer buffer = new StringBuffer();
		if(list!=null && list.size()>0){
			for (Object o : list) {
				buffer.append(o.toString()).append("@");
			}
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}

	public List<ElecPopedom> findPopedomListByString(String popedom) {
		String condition = " and o.isMenu=? and o.mid in('"+ popedom.replace("@", "','")+"')";
		Object [] params = {true};
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.mid", "asc");
		List<ElecPopedom> list = null;
		try {
			list = elecPopedomDao.findColletionByConditionNoPage(condition, params, orderby);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public boolean findRolePopedomByID(String roleID, String mid, String pid) {

		String  condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(roleID)){
			condition+=" and o.roleID = ?";
			paramsList.add(roleID);
		}
		if(StringUtils.isNotBlank(mid)){
			condition+=" and o.mid = ?";
			paramsList.add(mid);
		}
		if(StringUtils.isNotBlank(pid)){
			condition+=" and o.pid = ?";
			paramsList.add(pid);
		}
		Object[] params = paramsList.toArray();
		List<ElecRolePopedom> list = elecRolePopedomDao.findColletionByConditionNoPage(condition, params, null);
		boolean flag = false;
		if(list!=null && list.size()>0){
			flag = true;
		}
		return flag;
	}
	
	
}
