package com.ityu.elec.web.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ityu.elec.domain.ElecPopedom;
import com.ityu.elec.domain.ElecRole;
import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.service.IElecRoleService;

@Controller("elecRoleAction")
@Scope(value="prototype")
public class ElecRoleAction extends BaseAction<ElecPopedom>{
	ElecPopedom elecPopedom = this.getModel();

	@Resource(name=IElecRoleService.SERVICE_NAME)
	IElecRoleService elecRoleService;
	public String home(){
		List<ElecRole> roleList = elecRoleService.findAllRoleList();
		request.setAttribute("roleList", roleList);
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomList();
		request.setAttribute("popedomList", popedomList);
		return "home";
	}
	
	public String edit(){
		
		String roleID = elecPopedom.getRoleID();
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomListByRoleID(roleID);
		request.setAttribute("popedomList", popedomList);
		List<ElecUser> userList = elecRoleService.findAllUserListByRoleID(roleID);
		request.setAttribute("userList", userList);
		return "edit";
	}
	
	public String save(){
		elecRoleService.saveRole(elecPopedom);
		return "save";
	}
}
