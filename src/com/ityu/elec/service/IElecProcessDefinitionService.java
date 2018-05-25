package com.ityu.elec.service;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.jbpm.api.ProcessDefinition;

import com.ityu.elec.web.form.ProcessDefinitionBean;



public interface IElecProcessDefinitionService {

	public static final String SERVICE_NAME = "cn.itcast.elec.service.impl.ElecProcessDefinitionServiceImpl";

	List<ProcessDefinition> findProcessDefinitionListByLastVersion();

	void deployProcessDefinition(ZipInputStream zipInputStream);

	void deleteProcessDefinition(ProcessDefinitionBean processDefinitionBean);

	InputStream findInputStreamByImage(
			ProcessDefinitionBean processDefinitionBean);

}
