package com.ityu.elec.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ityu.elec.domain.ElecFileUpload;
import com.ityu.elec.domain.ElecSystemDDL;
import com.ityu.elec.service.IElecFileUploadService;
import com.ityu.elec.service.IElecSystemDDLService;



@SuppressWarnings("serial")
@Controller("elecFileUploadAction")
@Scope(value="prototype")
public class ElecFileUploadAction extends BaseAction<ElecFileUpload> {
	
	ElecFileUpload elecFileUpload = this.getModel();
	
	/**注入资料图纸管理Service*/
	@Resource(name=IElecFileUploadService.SERVICE_NAME)
	IElecFileUploadService elecFileUploadService;
	
	/**注入数据字典的Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	/**  
	* @Name: home
	* @Description: 资料图纸管理的首页显示
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-08（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到dataChart/dataChartIndex.jsp
	*/
	public String home(){
		//1：加载页面上显示的下拉菜单，所属单位和图纸列表的列表
		this.initSystemDDL();
		return "home";
	}
	
	/**加载页面上显示的下拉菜单，所属单位和图纸列表的列表*/
	private void initSystemDDL() {
		//所属单位
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		//图纸类别
		List<ElecSystemDDL> picList = elecSystemDDLService.findSystemDDLListByKeyword("图纸类别");
		request.setAttribute("picList", picList);
	}
	
	/**  
	* @Name: add
	* @Description: 跳转到新增页面的页面
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-08（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到dataChart/dataChartAdd.jsp
	*/
	public String add(){
		//1：加载页面上显示的下拉菜单，所属单位和图纸列表的列表
		this.initSystemDDL();
		return "add";
	}
	
	/**  
	* @Name: save
	* @Description: 保存资料图纸管理（管理员进行文件上传操作）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-08（创建日期）
	* @Parameters: 无
	* @Return: String：重定向到dataChart/dataChartAdd.jsp
	*/
	public String save(){
		elecFileUploadService.saveFileUpload(elecFileUpload);
		return "save";
	}
	
	/**  
	* @Name: addList
	* @Description: 使用所属单位和图纸类别查询对应的文件上传的数据信息
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-08（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到dataChart/dataChartAddList.jsp
	* 			将dataChartAddList.jsp页面的内容，放置到dataChartAdd.jsp的Form2表单中
	*/
	public String addList(){
		//1：通过选择所属单位和图纸类别的查询条件，查询对应单位和图纸下的文件上传的列表，返回List<ElecFileUpload>
		List<ElecFileUpload> list = elecFileUploadService.findFileUploadListByCondition(elecFileUpload);
		request.setAttribute("list", list);
		return "addList";
	}
	
	/**  
	* @Name: download
	* @Description: 文件下载（struts2的方式）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-8（创建日期）
	* @Parameters: 无
	* @Return: struts2的结果类型
	*/
	public String download(){
		try {
			//获取文件ID
			Integer fileID = elecFileUpload.getSeqId();
			//1：使用文件ID，查询资料图纸管理表，获取到路径path
			ElecFileUpload fileUpload = elecFileUploadService.findFileByID(fileID);
			//路径path
			String path = ServletActionContext.getServletContext().getRealPath("")+fileUpload.getFileUrl();
			//文件名称
			String filename = fileUpload.getFileName();
			//可以出现中文
			filename = new String(filename.getBytes("gbk"),"iso8859-1");
			request.setAttribute("filename", filename);
			
			//2：使用路径path，查找到对应的文件，转化成InputStream
			InputStream in = new FileInputStream(new File(path));
			//与栈顶的InputStream关联
			elecFileUpload.setInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "download";
	}
	
	/**  
	* @Name: luceneHome
	* @Description: 使用lucene进行全文检索
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-8（创建日期）
	* @Parameters: 无
	* @Return: String，跳转到dataChart/dataChartIndex.jsp
	*/
	public String luceneHome(){
		//* 加载页面下拉菜单的所属单位和图纸类别
		this.initSystemDDL();
		//使用lucene组织条件先查询索引库，使用主键ID查询数据库，返回List<ElecFileUpload>
		List<ElecFileUpload> list = elecFileUploadService.findFileUploadListByLuceneCondition(elecFileUpload);
		request.setAttribute("list", list);
		return "luceneHome";
	}
	
	/**  
	* @Name: delete
	* @Description: 删除资料图纸管理的数据
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-8（创建日期）
	* @Parameters: 无
	* @Return: String，重定向到dataChart/dataChartIndex.jsp
	*/
	public String delete(){
		//获取主键ID
		Integer seqId = elecFileUpload.getSeqId();
		elecFileUploadService.deleteFileUploadByID(seqId);
		return "delete";
	}
}
