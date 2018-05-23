package com.ityu.elec.service;

import java.util.Hashtable;
import java.util.List;

import com.ityu.elec.domain.ElecPopedom;
import com.ityu.elec.domain.ElecRole;
import com.ityu.elec.domain.ElecUser;

public interface IElecRoleService {


	public static final String SERVICE_NAME = 
			"com.ityu.elec.service.impl.ElecRoleServiceImpl";
	
	public List<ElecRole> findAllRoleList();
	
	public List<ElecPopedom>  findAllPopedomList();
	
	public List<ElecPopedom> findAllPopedomListByRoleID(String roleID);
	
	public List<ElecUser> findAllUserListByRoleID(String roleID);

	public void saveRole(ElecPopedom elecPopedom);

	public String findPopedomByRoleIDs(Hashtable<String, String> ht);

	public List<ElecPopedom> findPopedomListByString(String popedom);

	public boolean findRolePopedomByID(String roleID, String mid, String pid);
}
