package com.ityu.elec.service;

import java.util.List;

import com.ityu.elec.domain.ElecApplicationTemplate;




public interface IElecApplicationTemplateService {

	public static final String SERVICE_NAME = "com.ityu.elec.service.impl.ElecApplicationTemplateServiceImpl";

	List<ElecApplicationTemplate> findElecApplicationTemplateList();

	void saveApplicationTemplate(ElecApplicationTemplate elecApplicationTemplate);

	ElecApplicationTemplate findElecApplicationTemplateByID(Integer id);

	void updateApplicationTemplate(
			ElecApplicationTemplate elecApplicationTemplate);

	void deleteApplicationTemplateByID(Integer id);

}
