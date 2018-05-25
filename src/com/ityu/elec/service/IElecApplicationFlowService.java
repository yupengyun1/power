package com.ityu.elec.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.ityu.elec.domain.ElecApplication;
import com.ityu.elec.domain.ElecApproveInfo;





public interface IElecApplicationFlowService {

	public static final String SERVICE_NAME = "com.ityu.elec.service.impl.ElecApplicationFlowServiceImpl";

	void saveApplication(ElecApplication elecApplication);

	List<ElecApplication> findElecApplicationListByCondition(
			ElecApplication elecApplication);

	List<ElecApplication> findMyTaskList();

	Collection<String> findOutComesByTaskID(String taskID);

	ElecApplication findElecApplicationByID(Integer id);

	void approve(ElecApplication elecApplication);

	List<ElecApproveInfo> findApproveInfoListByApplicationID(
			ElecApplication elecApplication);

	void viewProcessPicture(ElecApplication elecApplication);

	InputStream findInputStreamByDeploymentId(String deploymentId);

	

}
