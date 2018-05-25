package cn.itcast.elec.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.jbpm.api.ProcessDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.elec.domain.ElecApplicationTemplate;
import cn.itcast.elec.service.IElecApplicationTemplateService;
import cn.itcast.elec.service.IElecProcessDefinitionService;
import cn.itcast.elec.util.ValueStackUtils;


@SuppressWarnings("serial")
@Controller("elecApplicationTemplateAction")
@Scope(value="prototype")
public class ElecApplicationTemplateAction extends BaseAction<ElecApplicationTemplate> {

	private ElecApplicationTemplate elecApplicationTemplate = this.getModel();
	
	//申请模板
	@Resource(name=IElecApplicationTemplateService.SERVICE_NAME)
	private IElecApplicationTemplateService elecApplicationTemplateService;
	
	//流程定义
	@Resource(name=IElecProcessDefinitionService.SERVICE_NAME)
	private IElecProcessDefinitionService elecProcessDefinitionService;
	
	
	/**  
	* @Name: home
	* @Description: 申请模板管理的首页显示
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/applicationTemplateList.jsp
	*/
	public String home(){
		//查询申请模板表，获取所有的申请模板，返回List<ElecApplicationTempllate>
		List<ElecApplicationTemplate> list = elecApplicationTemplateService.findElecApplicationTemplateList();
		request.setAttribute("list", list);
		return "home";
	}
	
	/**  
	* @Name: add
	* @Description: 跳转到新增模板页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/applicationTemplateAdd.jsp
	*/
	public String add(){
		//查询所有最新版本的流程定义列表，返回List<ProcessDefinition>，遍历在页面的下拉菜单中
		this.initProcessDefinition();
		return "add";
	}
	
	/**查询所有最新版本的流程定义列表，返回List<ProcessDefinition>，遍历在页面的下拉菜单中*/
	private void initProcessDefinition() {
		List<ProcessDefinition> pdList = elecProcessDefinitionService.findProcessDefinitionListByLastVersion();
		request.setAttribute("pdList", pdList);
	}

	/**  
	* @Name: save
	* @Description: 保存申请模板
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: 跳转到close.jsp
	*/
	public String save(){
		elecApplicationTemplateService.saveApplicationTemplate(elecApplicationTemplate);
		return "close";
	}
	
	/**  
	* @Name: edit
	* @Description: 跳转到编辑页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: 跳转到workflow/applicationTemplateEdit.jsp
	*/
	public String edit(){
		//1：使用申请模板ID，查询申请模板信息，返回ElecApplicationTemplate
		//获取申请模板ID
		Integer id = elecApplicationTemplate.getId();
		ElecApplicationTemplate applicationTemplate = elecApplicationTemplateService.findElecApplicationTemplateByID(id);
		//2：将对象放置到栈顶，支持表单回显
		ValueStackUtils.setValueStack(applicationTemplate);
		//3：查询所有最新版本的流程定义列表，返回List<ProcessDefinition>，遍历在页面的下拉菜单中
		this.initProcessDefinition();
		return "edit";
	}
	
	/**  
	* @Name: update
	* @Description: 编辑更新申请模板
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: 跳转到close.jsp
	*/
	public String update(){
		elecApplicationTemplateService.updateApplicationTemplate(elecApplicationTemplate);
		return "close";
	}
	
	/**  
	* @Name: delete
	* @Description: 删除申请模板
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: 重定向到workflow/applicationTemplateList.jsp
	*/
	public String delete(){
		//获取申请模板ID
		Integer id = elecApplicationTemplate.getId();
		elecApplicationTemplateService.deleteApplicationTemplateByID(id);
		return "delete";
	}
	
//	/**  
//	* @Name: download
//	* @Description: 文件下载（不使用struts2的流的类型）
//	* @Author: 刘洋（作者）
//	* @Version: V1.00 （版本号）
//	* @Create Date: 2013-11-27（创建日期）
//	* @Parameters: 无
//	* @Return: null
//	*/
//	public String download(){
//		try {
//			//1：使用申请模板ID，查询申请模板的详细信息，从而获取申请模板的对象，获取路径，将对应的文件写到输入流中InputStream
//			Integer id = elecApplicationTemplate.getId();
//			ElecApplicationTemplate applicationTemplate = elecApplicationTemplateService.findElecApplicationTemplateByID(id);
//			//获取路径path
//			String path = applicationTemplate.getPath();
//			//将文件放置到输入流中
//			InputStream in = new FileInputStream(new File(ServletActionContext.getServletContext().getRealPath("")+path));
//			//获取表单模板的名称
//			String name = applicationTemplate.getName();
//			//处理response中的乱码问题
//			name = new String(name.getBytes("gbk"),"iso-8859-1");
//			//2:在response对象中设置属性
//			//设置word文档的格式
//			response.setContentType("application/msword");
//			//设置附件的方式下载文件
//			response.setHeader("Content-disposition", "attachment;filename="+name+".doc");
//			//设置缓冲区大小
//			response.setBufferSize(1024);
//			//3：使用输出流OutPutstream，将输入流的数据写到输出流中，放置到response中
//			OutputStream out = response.getOutputStream();
//			for(int b=-1;(b=in.read())!=-1;){
//				out.write(b);
//			}
//			in.close();
//			out.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//		return null;
//	}
	
	/**  
	* @Name: download
	* @Description: 文件下载（使用struts2的流的类型）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2013-11-27（创建日期）
	* @Parameters: 无
	* @Return: 使用struts2提供的stream类型支持文件下载
	*/
	public String download(){
		try {
			//1：使用申请模板ID，查询申请模板的详细信息，从而获取申请模板的对象，获取路径，将对应的文件写到输入流中InputStream
			Integer id = elecApplicationTemplate.getId();
			ElecApplicationTemplate applicationTemplate = elecApplicationTemplateService.findElecApplicationTemplateByID(id);
			//获取路径path
			String path = applicationTemplate.getPath();
			//将文件放置到输入流中
			InputStream in = new FileInputStream(new File(ServletActionContext.getServletContext().getRealPath("")+path));
			//获取表单模板的名称
			String name = applicationTemplate.getName();
			//处理response中的乱码问题
			name = new String(name.getBytes("gbk"),"iso-8859-1");
			//将name放置到request中，并且命名为filename
			request.setAttribute("filename", name);
			//将输入流中存放的文件，放置到栈顶对象的InputStream中
			elecApplicationTemplate.setInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "download";
	}
}
