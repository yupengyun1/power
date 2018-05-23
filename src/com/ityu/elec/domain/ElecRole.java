package com.ityu.elec.domain;

import java.util.HashSet;
import java.util.Set;

public class ElecRole implements java.io.Serializable{

	private String roleID;
	private String roleName;
	private Set<ElecUser> elecUsers=new HashSet<ElecUser>();
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Set<ElecUser> getElecUsers() {
		return elecUsers;
	}
	public void setElecUsers(Set<ElecUser> elecUsers) {
		this.elecUsers = elecUsers;
	}
	
}
