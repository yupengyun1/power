package com.ityu.elec.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.CharArrayMap.EntrySet;
import org.apache.struts2.StrutsStatics;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.service.IElecRoleService;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class ErrorAndLimitInterceptor extends MethodFilterInterceptor{

	@Override
	protected String doIntercept(ActionInvocation actioninvocation) throws Exception {

		HttpServletRequest request = (HttpServletRequest) actioninvocation
				.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
		String result;
		try {
			Object action = actioninvocation.getAction();
			String methodName = actioninvocation.getProxy().getMethod();
			Method method = action.getClass().getMethod(methodName, null);
			
			result = null;
			if(true){
				result=actioninvocation.invoke();
			}else{
				request.setAttribute("errorMsg", "对不起！你没有权限操作此功能");
				return "errorMsg";
			}
			return result;
		} catch (Exception e) {
			String errorMsg="出现错误信息，请查看日志";
			if( e instanceof RuntimeException){
				RuntimeException re = (RuntimeException) e;
				errorMsg = re.getMessage().trim();
			}
			request.setAttribute("errorMsg", errorMsg);
			Log Log = LogFactory.getLog(actioninvocation.getAction().getClass());
			log.error(errorMsg, e);
			return "errorMsg";
		}
	}

	public boolean isCheckLimit(HttpServletRequest request,Method method){
		if(method==null){
			return false;
		}
		ElecUser elecUser = (ElecUser) request.getSession().getAttribute("globle_user");
		if(elecUser==null){
			return false;
		}
		HashMap<String,String> ht = (HashMap<String, String>) request.getSession().getAttribute("globle_role");
		if(ht==null){
			return false;
		}
		AnnotationLimit limit = method.getAnnotation(AnnotationLimit.class);
		String mid = limit.mid();
		String pid = limit.pid();
		boolean flag = false;
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		IElecRoleService elecRoleService = (IElecRoleService) wac.getBean(IElecRoleService.SERVICE_NAME);
		if(ht!=null && ht.size()>0){
			for (Iterator<Entry<String, String>> ite = ht.entrySet().iterator();ite.hasNext();) {
				Entry<String, String> entry = ite.next();
				String roleID = entry.getKey();
				flag = elecRoleService.findRolePopedomByID(roleID, mid, pid);
				if(flag){
					break;
				}
			}
		}
		return flag;
	}
}
