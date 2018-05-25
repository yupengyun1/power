package com.ityu.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.ityu.elec.dao.IElecApplicationTemplateDao;
import com.ityu.elec.domain.ElecApplicationTemplate;



@Repository(IElecApplicationTemplateDao.SERVICE_NAME)
public class ElecApplicationTemplateDaoImpl extends CommonDaoImpl<ElecApplicationTemplate> implements IElecApplicationTemplateDao {
	
}
