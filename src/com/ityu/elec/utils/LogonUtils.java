package com.ityu.elec.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class LogonUtils {

	public static boolean checkNumber(HttpServletRequest request){
		String imageNumber = request.getParameter("checkNumber");
		if(StringUtils.isBlank(imageNumber)){
			return false;
		}
		String  CHECK_NUMBER_KEY = (String) request.getSession().getAttribute("CHECK_NUMBER_KEY");
		if(StringUtils.isBlank(CHECK_NUMBER_KEY)){
			return false;
		}
		return imageNumber.equalsIgnoreCase(CHECK_NUMBER_KEY);
		
	}
	public static void remeberMe(String name,String password,
			HttpServletRequest request, HttpServletResponse response ){
		
		try {
			name = URLEncoder.encode(name,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cookie nameCookie = new Cookie("name",name);
		Cookie passwordCookie = new Cookie("password",password);
		String remberMe = request.getParameter("remeberMe");
		nameCookie.setPath(request.getContextPath()+"/");
		passwordCookie.setPath(request.getContextPath()+"/");
		if(remberMe!=null && remberMe.equals("yes")){
			nameCookie.setMaxAge(7*24*60*60);
			passwordCookie.setMaxAge(7*24*60*60);
		}else{
			nameCookie.setMaxAge(0);
			passwordCookie.setMaxAge(0);
		}
		response.addCookie(nameCookie);
		response.addCookie(passwordCookie);
	}
}
