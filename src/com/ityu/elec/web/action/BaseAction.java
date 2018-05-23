package com.ityu.elec.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.ityu.elec.utils.TUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T>,ServletRequestAware,ServletResponseAware {

	T entity;
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public BaseAction(){
		Class entityClass = TUtil.getActualType(this.getClass());
		try{
			entity = (T) entityClass.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public T getModel() {
		
		return entity;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}
public static void main(String[] args) {
	System.out.println("sss");
}
}
