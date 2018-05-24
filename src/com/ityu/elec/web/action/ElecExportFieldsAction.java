package com.ityu.elec.web.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ityu.elec.domain.ElecExportFields;
import com.ityu.elec.service.IElecExportFieldsService;
import com.ityu.elec.utils.StringToListUtils;

@SuppressWarnings("serial")
@Controller("elecExportFieldsAction")
@Scope(value="prototype")
public class ElecExportFieldsAction extends BaseAction<ElecExportFields>{

	private ElecExportFields elecExportFields = this.getModel();
	
	@Resource(name=IElecExportFieldsService.SERVICE_NAME)
	private IElecExportFieldsService elecExportFieldsService;
	
	public String setExportExcel(){
		//获取所属模块（主键ID）
		String belongTo = elecExportFields.getBelongTo();
		//1：使用belongTo作为主键，查询导出设置表，获取ElecExportFields对象
		ElecExportFields exportFields = elecExportFieldsService.findElecExportFields(belongTo);
		/**
		 * 2:使用ElecExportFields对象获取4个字段的值
			 未导出的中文字段       未导出的英文字段           导出的中文字段           导出的英文字段
			   同时将4个字段的值，使用#号分割，转换成4个集合List<String> list
		 */
		List<String> noZList = StringToListUtils.stringToList(exportFields.getNoExpNameList(),"#");
		List<String> noEList = StringToListUtils.stringToList(exportFields.getNoExpFieldName(),"#");
		List<String> zList = StringToListUtils.stringToList(exportFields.getExpNameList(),"#");
		List<String> eList = StringToListUtils.stringToList(exportFields.getExpFieldName(),"#");
		/**
		 * 3：由于特点
			  【未导出的中文字段和未导出的英文字段，长度要一一对应
			   导出的中文字段和导出的英文字段，长度要一一对应】
		   4：使用2个map集合，存放未导出字段的集合，存放导出字段的集合
			  Map<String,String> map
			   map集合的key存放英文信息，map集合的value存放中文信息
		 */
		//存放未导出的字段
		Map<String, String> noMap = new LinkedHashMap<String, String>();
		//存放导出的字段
		Map<String, String> map = new LinkedHashMap<String, String>();
		//因为noZList和noEList长度一一对应
		if(noZList!=null && noZList.size()>0){
			for(int i=0;i<noZList.size();i++){
				noMap.put(noEList.get(i), noZList.get(i));
			}
		}
		//因为zList和eList长度一一对应
		if(zList!=null && zList.size()>0){
			for(int i=0;i<zList.size();i++){
				map.put(eList.get(i), zList.get(i));
			}
		}
		request.setAttribute("noMap", noMap);
		request.setAttribute("map", map);
		
		return "setExportExcel";
	}
	public String saveSetExportExcel(){
		elecExportFieldsService.saveSetExportExcel(elecExportFields);
		return "close";
	}
}
