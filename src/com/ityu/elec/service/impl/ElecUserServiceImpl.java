package com.ityu.elec.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.ityu.elec.dao.IElecSystemDDLDao;
import com.ityu.elec.dao.IElecUserDao;
import com.ityu.elec.dao.IElecUserFileDao;
import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.domain.ElecUserFile;
import com.ityu.elec.service.IElecUserService;
import com.ityu.elec.utils.FileUtils;
import com.ityu.elec.utils.MD5keyBean;
import com.ityu.elec.utils.PageInfo;


@Service(IElecUserService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecUserServiceImpl implements IElecUserService {

	/**用户表Dao*/
	@Resource(name=IElecUserDao.SERVICE_NAME)
	IElecUserDao elecUserDao;
	
	/**用户附件表Dao*/
	@Resource(name=IElecUserFileDao.SERVICE_NAME)
	IElecUserFileDao elecUserFileDao;
	
	/**数据字典表Dao*/
	@Resource(name=IElecSystemDDLDao.SERVICE_NAME)
	IElecSystemDDLDao elecSystemDDLDao;

	public List<Object[]> chartUser(String zName, String eName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String checkUser(String logonName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteUserByID(ElecUser elecUser) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<ArrayList<String>> findFieldDataWithExcel(ElecUser elecUser) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> findFieldNameWithExcel() {
		// TODO Auto-generated method stub
		return null;
	}

	public ElecUser findUserByID(String userID) {
		ElecUser elecUser = elecUserDao.findObjectById(userID);
		return elecUser;
	}

	public ElecUser findUserByLogonName(String name) {

		String condition="";
		ArrayList<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(name)){
			condition +=" and o.logonName=?";
			paramsList.add(name);
		}
		Object[] params = paramsList.toArray();
		List<ElecUser> list = elecUserDao.findColletionByConditionNoPage(condition, params, null);
		ElecUser elecUser = null;
		if(list!=null && list.size()>0){
			elecUser = list.get(0);
		}
		return elecUser;
	}

	public ElecUserFile findUserFileByID(String fileID) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ElecUser> findUserListByCondition(ElecUser elecUser) {
		//组织查询条件
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//用户名称
		String userName = elecUser.getUserName();
		if(StringUtils.isNotBlank(userName)){
			condition += " and o.userName like ?";
			paramsList.add("%"+userName+"%");
		}
		//所属单位
		String jctID = elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition += " and o.jctID = ?";
			paramsList.add(jctID);
		}
		//入职开始时间
		Date onDutyDateBegin = elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition += " and o.onDutyDate >= ?";
			paramsList.add(onDutyDateBegin);
		}
		//入职结束时间
		Date onDutyDateEnd = elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition += " and o.onDutyDate <= ?";
			paramsList.add(onDutyDateEnd);
		}
		Object [] params = paramsList.toArray();
		//排序（按照入职时间的升序排列）
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		/**方案一：查询用户表，再转换数据字典表*/
		//List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage(condition, params, orderby);
		/**2014-12-9,添加分页 begin*/
		PageInfo pageInfo = new PageInfo(ServletActionContext.getRequest());
		List<ElecUser> list = elecUserDao.findCollectionByConditionWithPage(condition, params, orderby,pageInfo);
		ServletActionContext.getRequest().setAttribute("page", pageInfo.getPageBean());
		/**2014-12-9,添加分页 end*/
		/**
		 * 3：数据字典的转换
		 	* 使用数据类型和数据项的编号，查询数据字典，获取数据项的值
		 */
		this.convertSystemDDL(list);
		/**方案二：直接使用sql语句，完成1条语句操作用户表，检索出结果*/
//		List<ElecUser> list = elecUserDao.findCollectionByConditionNoPageWithSql(condition, params, orderby);
		return list;
	}
	/**使用数据类型和数据项的编号，查询数据字典，获取数据项的值*/
	private void convertSystemDDL(List<ElecUser> list) {
		if(list!=null && list.size()>0){
			for(ElecUser user:list){
				//性别
				String sexID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("性别",user.getSexID());
				user.setSexID(sexID);
				//职位
				String postID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("职位",user.getPostID());
				user.setPostID(postID);
			}
		}
	}
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveUser(ElecUser elecUser) {
		this.saveUserFiles(elecUser);
		this.md5password(elecUser);
		String userID = elecUser.getUserID();
		if(StringUtils.isNotBlank(userID)){
			elecUserDao.update(elecUser);
		}else{
			elecUserDao.save(elecUser);
		}
	}

	private void md5password(ElecUser elecUser) {
		
		String logonPwd = elecUser.getLogonPwd();
		String md5password = "";
		if(StringUtils.isBlank(logonPwd)){
			logonPwd="123";
		}
		String password = elecUser.getPassword();
		if(password!=null && password.equals(logonPwd)){
			md5password = logonPwd;
		}else{
			MD5keyBean md5keyBean = new MD5keyBean();
			md5password = md5keyBean.getkeyBeanofStr(logonPwd);
		}
		elecUser.setLogonPwd(md5password);
	}

	private void saveUserFiles(ElecUser elecUser) {
		Date progressTime = new Date();
		File[] uploads = elecUser.getUploads();
		String[] fileNames = elecUser.getUploadsFileName();
		String[] uploadsContentTypes = elecUser.getUploadsContentType();
		if(uploads!=null && uploads.length>0){
			for (int i = 0; i < uploads.length; i++) {
				ElecUserFile elecUserFile = new ElecUserFile();
				elecUserFile.setFileName(fileNames[i]);
			    elecUserFile.setProgressTime(progressTime);
			    String fileURL = FileUtils.fileUploadReturnPath(uploads[i], fileNames[i], "用户管理");
			    elecUserFile.setFileURL(fileURL);
			    elecUserFile.setElecUser(elecUser);
			    elecUserFileDao.save(elecUserFile);
			}
		}
	}

	public void saveUserList(List<ElecUser> userList) {
		// TODO Auto-generated method stub
		
	}
		
}
