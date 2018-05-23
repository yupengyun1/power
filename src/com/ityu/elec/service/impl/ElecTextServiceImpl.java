package com.ityu.elec.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ityu.elec.dao.IElecTextDao;
import com.ityu.elec.domain.ElecText;
import com.ityu.elec.service.IElecTextService;

@Service(IElecTextService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecTextServiceImpl implements IElecTextService {
	
	@Resource(name=IElecTextDao.SERVICE_NAME)
	private IElecTextDao elecTextDao;

	public List<ElecText> findCollectionByCondittionNoPage(ElecText elecText) {

		String condition = " ";
		List<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(elecText.getTextName())){
			condition+=" and o.textName like ?";
			paramsList.add("%"+elecText.getTextName()+"%");
		}
		if(StringUtils.isNotBlank(elecText.getTextRemark())){
			condition += " and o.textRemark like ?";
			paramsList.add("%"+elecText.getTextRemark()+"%");
		}
		Object[] params = paramsList.toArray();
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String,String>();
		orderby.put("o.textDate", "asc");
		orderby.put("o.textName", "desc");
		List<ElecText> list = elecTextDao.findColletionByConditionNoPage(condition, params, orderby);
		return list;
	}

	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveElecText(ElecText elecText) {

		elecTextDao.save(elecText);
	}

}
