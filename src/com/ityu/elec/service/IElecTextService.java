package com.ityu.elec.service;

import java.util.List;

import com.ityu.elec.domain.ElecText;

public interface IElecTextService {

	public static final String SERVICE_NAME = 
		"com.ityu.elec.service.impl.ElecTextServiceImpl";
	
	public void saveElecText(ElecText elecText);
	
	public List<ElecText> findCollectionByCondittionNoPage(ElecText elecText);
	
}
