package com.ityu.elec.web.action;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import sun.security.provider.MD5;

import com.ityu.elec.domain.ElecCommonMsg;
import com.ityu.elec.domain.ElecPopedom;
import com.ityu.elec.domain.ElecRole;
import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.service.IElecCommonMsgService;
import com.ityu.elec.service.IElecRoleService;
import com.ityu.elec.service.IElecUserService;
import com.ityu.elec.utils.LogonUtils;
import com.ityu.elec.utils.MD5keyBean;
import com.ityu.elec.utils.ValueUtils;
import com.ityu.elec.web.form.MenuForm;

@Controller("elecMenuAction")
@Scope(value="prototype")
public class ElecMenuAction extends BaseAction<MenuForm>{

	MenuForm menuForm = this.getModel();
	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	@Resource(name=IElecUserService.SERVICE_NAME)
	IElecUserService elecUserService;
	/**注入角色Service*/
	@Resource(name=IElecRoleService.SERVICE_NAME)
	IElecRoleService elecRoleService;
	public String menuHome(){
		String name = menuForm.getName();
		String password = menuForm.getPassword();
		boolean flag = LogonUtils.checkNumber(request);
		if(!flag){
			this.addActionError("验证码输入有误！");
			return "logonError";
		}
		ElecUser elecUser = elecUserService.findUserByLogonName(name);
		if(elecUser==null){
			this.addActionError("用户名输入有误");
			return "logonError";
		}
		if(StringUtils.isBlank(password)){
			this.addActionError("密码不能为空");
			return "logonError";
		}else{
			MD5keyBean bean = new MD5keyBean();
			String md5password = bean.getkeyBeanofStr(password);
			if(!md5password.equals(elecUser.getLogonPwd())){
				this.addActionError("密码输入有误");
				return "logonError";
			}
		}
		Hashtable<String, String> ht = new Hashtable<String, String>();
		Set<ElecRole> elecRoles = elecUser.getElecRoles();
		if(elecRoles==null || 0==elecRoles.size()){
			this.addActionError("当前用户没有分配角色，请与管理员联系！");
			return "logonError";
		}else{
			for (ElecRole elecRole : elecRoles) {
				ht.put(elecRole.getRoleID(), elecRole.getRoleName());
			}
		}
		String popedom = elecRoleService.findPopedomByRoleIDs(ht);
		if(StringUtils.isBlank(popedom)){
			this.addActionError("当前用户具有的角色没有分配权限，请与管理员联系！");
			return "logonError";
		}
		LogonUtils.remeberMe(name, password, request, response);
		request.getSession().setAttribute("globle_user", elecUser);
		request.getSession().setAttribute("globle_role", ht);
		request.getSession().setAttribute("globle_popedom", popedom);
		return "menuHome";
	}
	/**标题*/
	public String title(){
		return "title";
	}
	public String left(){
		return "left";
	}
	public String change(){
		return "change";
	}
	public String loading(){
		ElecCommonMsg commonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(commonMsg);
		return "loading";
	}
	public String logout(){
//		request.getSession().removeAttribute(arg0)
		request.getSession().invalidate();
		return "logout";
	}
	public String alermStation(){
		ElecCommonMsg commonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(commonMsg);
		return "alermStation";
	}
	public String alermDevice(){
		ElecCommonMsg commonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(commonMsg);
		return "alermDevice";
	}
	
	public String showMenu(){
		Hashtable<String,String> ht = (Hashtable<String, String>) request.getSession().getAttribute("globle_role");
		ElecUser elecUser = (ElecUser) request.getSession().getAttribute("globle_user");
		String popedom = (String) request.getSession().getAttribute("globle_popedom");
		List<ElecPopedom> list = elecRoleService.findPopedomListByString(popedom);
		if(!ht.containsKey("1")){
			if(list!=null && list.size()>0){
				for (ElecPopedom elecPopedom : list) {
					String mid = elecPopedom.getMid();
					String pid = elecPopedom.getPid();
					if("an".equals(mid) && "am".equals(pid)){
						elecPopedom.setUrl("../system/elecUserAction_edit.do?userID="+elecUser.getUserID()+"&roleflag=1");
					}
				}
			}
		}
		ValueUtils.putValueStack(list);
		return "showMenu";
	}
}
