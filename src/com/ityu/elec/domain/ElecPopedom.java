package com.ityu.elec.domain;

import java.util.ArrayList;
import java.util.List;

public class ElecPopedom implements java.io.Serializable{

	private String mid;
	private String pid;
	private String name;
	private String url;
	private String icon;
	private String target;
	private boolean isParent;
	private boolean isMenu;
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public boolean getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(boolean isMenu) {
		this.isMenu = isMenu;
	}
	private List<ElecPopedom> list=new ArrayList<ElecPopedom>();
	
	private String roleID;
	
	private String flag;
	
	private String[] selectoper;
	
	private String[] selectuser;
	public List<ElecPopedom> getList() {
		return list;
	}
	public void setList(List<ElecPopedom> list) {
		this.list = list;
	}

	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String[] getSelectoper() {
		return selectoper;
	}
	public void setSelectoper(String[] selectoper) {
		this.selectoper = selectoper;
	}
	public String[] getSelectuser() {
		return selectuser;
	}
	public void setSelectuser(String[] selectuser) {
		this.selectuser = selectuser;
	}
	
	
}
