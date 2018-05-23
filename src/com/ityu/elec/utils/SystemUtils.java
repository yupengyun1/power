package com.ityu.elec.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ityu.elec.domain.ElecUser;

public class SystemUtils implements Filter{
	List<String> list = new ArrayList<String>();

	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		String path = request.getServletPath();
		this.initIndexPage(request,path);
		if(list.contains(path)){
			chain.doFilter(request, response);
			return ;
		}
		
		ElecUser elecUser = (ElecUser) request.getSession().getAttribute("globle_user");
		if(elecUser!=null){
			chain.doFilter(request, response);
			return;
		}
	}

	private void initIndexPage(HttpServletRequest request, String path) {

		if(path!=null && path.equals("/index.jsp")){
			String name = "";
			String password = "";
			String checked = "";
			Cookie[] cookies = request.getCookies();
			if(cookies!=null && cookies.length>0){
				for (Cookie cookie : cookies) {
					if("name".equals(cookie.getName())){
						name = cookie.getValue();
						try {
							name = URLDecoder.decode(name, "utf-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if("password".equals(cookie.getName())){
						password = cookie.getValue();
					}
				}
			}
			request.setAttribute("name",name);
			request.setAttribute("password", password);
			request.setAttribute("checked", checked);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {

		list.add("/index.jsp");
		list.add("/image.jsp");
		list.add("/system/elecMenuAction_menuHome.do");
		list.add("/error.jsp");
		list.add("/system/elecMenuAction_logout.do");
	}

	
}
