package com.ityu.elec.web.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ityu.elec.domain.ElecText;
import com.ityu.elec.service.IElecTextService;

@SuppressWarnings("serial")
@Controller("elecTextAction")
@Scope(value="prototype")
public class ElecTextAction extends BaseAction<ElecText>{

	ElecText elecText = this.getModel();
	
	@Resource(name=IElecTextService.SERVICE_NAME)
	private IElecTextService elecTextService;
	
	public String save(){
		elecTextService.saveElecText(elecText);
		String textDate = request.getParameter("textDate");
		return "save";
	}
}
