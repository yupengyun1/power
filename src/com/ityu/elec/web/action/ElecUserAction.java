package com.ityu.elec.web.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ityu.elec.domain.ElecSystemDDL;
import com.ityu.elec.domain.ElecUser;
import com.ityu.elec.domain.ElecUserFile;
import com.ityu.elec.service.IElecSystemDDLService;
import com.ityu.elec.service.IElecUserService;
import com.ityu.elec.utils.ValueUtils;

@SuppressWarnings("serial")
@Controller("elecUserAction")
@Scope(value="prototype")
public class ElecUserAction extends BaseAction<ElecUser> {
	
	ElecUser elecUser = this.getModel();
	
	/**注入用户Service*/
	@Resource(name=IElecUserService.SERVICE_NAME)
	IElecUserService elecUserService;
	
	/**注入数据字典Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: 用户管理的首页显示
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-1（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/userIndex.jsp
	*/
	public String home(){
		//1：加载数据类型是所属单位的数据字典的集合，遍历在页面的下拉菜单中
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		//2：组织页面中传递的查询条件，查询用户表，返回List<ElecUser>
		List<ElecUser> userList = elecUserService.findUserListByCondition(elecUser);
		request.setAttribute("userList", userList);
		
		/**2014-12-9,添加分页 begin*/
		String initpage = request.getParameter("initpage");
		//执行ajax操作跳转到userList.jsp
		if(initpage!=null && initpage.equals("1")){
			return "list";
		}
		/**2014-12-9,添加分页 end*/
//		/**故意抛出异常*/
//		try {
//			ElecUser elecUser = null;
//			elecUser.getAddress();//null异常
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("抛出运行时异常，在用户的Action中的home方法");
//		}
		return "home";
	}
	
	/**  
	* @Name: add
	* @Description: 跳转到用户管理的新增页面显示
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-1（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/userAdd.jsp
	*/
	public String add(){
		//1：加载数据字典，用来遍历性别，职位，所属单位，是否在职
		this.initSystemDDL();
		return "add";
	}

	/**1：加载数据字典，用来遍历性别，职位，所属单位，是否在职*/
	private void initSystemDDL() {
		List<ElecSystemDDL> sexList = elecSystemDDLService.findSystemDDLListByKeyword("性别");
		request.setAttribute("sexList", sexList);
		List<ElecSystemDDL> postList = elecSystemDDLService.findSystemDDLListByKeyword("职位");
		request.setAttribute("postList", postList);
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		List<ElecSystemDDL> isDutyList = elecSystemDDLService.findSystemDDLListByKeyword("是否在职");
		request.setAttribute("isDutyList", isDutyList);
	}
	
	/**  
	* @Name: findJctUnit
	* @Description: 使用jquery的ajax完成二级联动，使用所属单位，关联单位名称
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-2（创建日期）
	* @Parameters: 无
	* @Return: 使用struts2的json插件包
	*/
	public String findJctUnit(){
		// 1：获取所属单位下的数据项的值
		String jctID = elecUser.getJctID();
		// 2：使用该值作为数据类型，查询对应数据字典的值，返回List<ElecSystemDDL>
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByKeyword(jctID);
		// 3：将List<ElecSystemDDL>转换成json的数组，将List集合放置到栈顶
		ValueUtils.putValueStack(list);
		return "findJctUnit";
	}
	
	/**  
	* @Name: checkUser
	* @Description: 使用jquery的ajax完成登录名的后台校验，判断是否数据库中存在，保证登录名惟一
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-2（创建日期）
	* @Parameters: 无
	* @Return: 使用struts2的json插件包
	*/
	public String checkUser(){
		//1:获取登录名
		String logonName = elecUser.getLogonName();
		//2:判断登录名是否出现重复
		String message = elecUserService.checkUser(logonName);
		//放置到栈顶
		elecUser.setMessage(message);//栈顶对象是ElecUser对象
		//ValueUtils.putValueStack(message);//栈顶对象是String类型的属性
		return "checkUser";
	}
	
	/**  
	* @Name: save
	* @Description: 保存用户
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-2（创建日期）
	* @Parameters: 无
	* @Return: 跳转到close.jsp
	*/
	public String save(){
		elecUserService.saveUser(elecUser);
		/**
			 * 用来判断
			 * roleflag==null：系统管理员操作编辑页面，此时保存：close.jsp
			 * roleflag==1：如果不是系统管理员操作编辑页面，此时保存：重定向到编辑页面
		 */
		String roleflag = elecUser.getRoleflag();
		//如果不是系统管理员操作编辑页面，此时保存：重定向到编辑页面
		if(roleflag!=null && roleflag.equals("1")){
			return "redirectEdit";
		}
		return "close";
	}
	
	/**  
	* @Name: edit
	* @Description: 跳转到编辑页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-2（创建日期）
	* @Parameters: 无
	* @Return: 跳转到system/userEdit.jsp
	*/
	public String edit(){
		//获取用户ID
		String userID = elecUser.getUserID();
		//1：使用用户ID，查询对象ElecUser对象，支持表单回显
		ElecUser user = elecUserService.findUserByID(userID);
		//将VO对象的属性，放置到PO对象的属性值
		user.setViewflag(elecUser.getViewflag());
		user.setRoleflag(elecUser.getRoleflag());
		//2：将ElecUser对象放置到栈顶，页面使用struts2的标签支持回显
		ValueUtils.putValueStack(user);//栈顶对象
		//3：加载数据字典，用来遍历性别，职位，所属单位，是否在职
		this.initSystemDDL();
		//4：二级联动的表单回显
		//(1)获取到所属单位的编号
		String ddlCode = user.getJctID();
		//(2)使用所属单位和数据项的编号，获取数据项的值
		String ddlName = elecSystemDDLService.findDdlNameByKeywordAndDdlCode("所属单位",ddlCode);
		//(3)使用查询的数据项的值，作为数据类型，查询该数据类型的对应的集合，返回List<ElecSystemDDL>
		List<ElecSystemDDL> jctUnitList = elecSystemDDLService.findSystemDDLListByKeyword(ddlName);
		request.setAttribute("jctUnitList", jctUnitList);
		return "edit";
	}
	
	/**  
	* @Name: download
	* @Description: 文件下载（普通方式）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-2（创建日期）
	* @Parameters: 无
	* @Return: 无
	*/
//	public String download(){
//		try {
//			//获取文件ID
//			String fileID = elecUser.getFileID();
//			//1：使用文件ID，查询用户文件表，获取到路径path
//			ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
//			//路径path
//			String path = ServletActionContext.getServletContext().getRealPath("")+elecUserFile.getFileURL();
//			//文件名称
//			String filename = elecUserFile.getFileName();
//			//可以出现中文
//			filename = new String(filename.getBytes("gbk"),"iso8859-1");
//			//  * 填写下载文件的头部信息（不用记）
////			response.setContentType("application/vnd.ms-excel");
//			response.setHeader("Content-disposition", "attachment;filename="+filename);
//			
//			//2：使用路径path，查找到对应的文件，转化成InputStream
//			InputStream in = new FileInputStream(new File(path));
//			
//			//3：从响应对象Response中获取输出流OutputStream
//			OutputStream out = response.getOutputStream();
//			//4：将输入流的数据读取，写到输出流中
//			//IOUtils.copy(input, output)
//			for(int b=-1;(b=in.read())!=-1;){
//				out.write(b);
//			}
//			out.close();
//			in.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return NONE;
//	}
	
	
	/**  
	* @Name: download
	* @Description: 文件下载（struts2的方式）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-2（创建日期）
	* @Parameters: 无
	* @Return: struts2的结果类型
	*/
	public String download(){
		try {
			//获取文件ID
			String fileID = elecUser.getFileID();
			//1：使用文件ID，查询用户文件表，获取到路径path
			ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
			//路径path
			String path = ServletActionContext.getServletContext().getRealPath("")+elecUserFile.getFileURL();
			//文件名称
			String filename = elecUserFile.getFileName();
			//可以出现中文
			filename = new String(filename.getBytes("gbk"),"iso8859-1");
			request.setAttribute("filename", filename);
			
			//2：使用路径path，查找到对应的文件，转化成InputStream
			InputStream in = new FileInputStream(new File(path));
			//与栈顶的InputStream关联
			elecUser.setInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "download";
	}
	
	/**  
	* @Name: delete
	* @Description: 删除用户信息
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-2（创建日期）
	* @Parameters: 无
	* @Return: String 重定向到system/userIndex.jsp
	*/
	public String delete(){
		elecUserService.deleteUserByID(elecUser);
		/**
		 * 添加执行删除，传递当前页，这样做，可以删除后定向到当前页
		 */
		request.setAttribute("pageNO", request.getParameter("pageNO"));
		return "delete";
	}
	
	/**  
	* @throws 、Exception 
	* @Name: exportExcel
	* @Description: 将数据通过查询条件，导出对应数据的excel报表
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-9（创建日期）
	* @Parameters: 无
	* @Return: 不使用struts2开发，导出
	*/
//	public String exportExcel() throws Exception{
//		//1：初始化数据
//		//excel的标题数据（只有一条）
//		ArrayList<String> fieldName = elecUserService.findFieldNameWithExcel();
//		//excel的内容数据（多条）
//		ArrayList<ArrayList<String>> fieldData = elecUserService.findFieldDataWithExcel(elecUser);
//		//2：调用封装的POI报表的导出类ExcelFileGenerator.java，完成excel报表的导出
//		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldData);
//		
//		/**导出报表的文件名*/
//		String filename = "用户报表（"+DateUtils.dateToStringWithExcel(new Date())+"）.xls";
//		//处理乱码
//		filename = new String(filename.getBytes("gbk"),"iso-8859-1"); 
//		/**response中进行设置，总结下载，导出，需要io流和头*/
//		response.setContentType("application/vnd.ms-excel");
//		response.setHeader("Content-disposition", "attachment;filename="+filename);
//		response.setBufferSize(1024);
//		
//		
//		//获取输出流
//		OutputStream os = response.getOutputStream();
//		excelFileGenerator.expordExcel(os);//使用输出流，导出
//		return null;
//	}
	
	
	/**  
	* @Name: importPage
	* @Description: 跳转到导入页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-9（创建日期）
	* @Parameters: 无
	* @Return: 跳转到system/userImport.jsp
	*/
	public String importPage(){
		return "importPage";
	}

		
	/**  
	* @Name: chartUserFCF
	* @Description: 用户统计（按照性别）技术FCF报表
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-10（创建日期）
	* @Parameters: 无
	* @Return: 跳转到system/userReportFCF.jsp
	*/
	public String chartUserFCF(){
		//查询数据库，获取图形需要数据集合
		List<Object[]> list = elecUserService.chartUser("性别","sexID");
		//组织XML的数据
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
				/**b.keyword,b.ddlName,COUNT(b.ddlCode)*/
				Object[] objects = (Object[])list.get(i);
				if(i==0){//组织第一个值
					String x = "男女比例统计";
					String y = "unit";//存在FusionChart中的一个问题，Y轴的显示不支持中文，所以我们用英文代替
					builder.append("<graph caption='用户统计报表("+objects[0].toString()+")' xAxisName='"+x+"' bgColor='FFFFDD' yAxisName='"+y+"' showValues='1'  decimals='0' baseFontSize='18'  maxColWidth='60' showNames='1' decimalPrecision='0'> ");
					builder.append("<set name='"+objects[1].toString()+"' value='"+objects[2].toString()+"' color='AFD8F8'/>");
				}
			    
			    if(i==list.size()-1){//组织最后一个值
			    	builder.append("<set name='"+objects[1].toString()+"' value='"+objects[2].toString()+"' color='FF8E46'/>");
			    	builder.append("</graph>");
			    }
		} 
		request.setAttribute("chart", builder);//request中存放XML格式的数据
		return "chartUserFCF";
	}
}
