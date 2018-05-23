package com.ityu.elec.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ityu.elec.dao.IElecCommonMsgContentDao;
import com.ityu.elec.dao.IElecCommonMsgDao;
import com.ityu.elec.domain.ElecCommonMsg;
import com.ityu.elec.domain.ElecCommonMsgContent;
import com.ityu.elec.service.IElecCommonMsgService;
import com.ityu.elec.utils.StringUtil;

@Service(IElecCommonMsgService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecCommonMsgServiceImpl implements IElecCommonMsgService{

	@Resource(name=IElecCommonMsgDao.SERVICE_NAME)
	private IElecCommonMsgDao elecCommonMsgDao;
	@Resource(name=IElecCommonMsgContentDao.SERVICE_NAME)
	private IElecCommonMsgContentDao elecCommonMsgContentDao;
	
	public ElecCommonMsg findCommonMsg() {
		
		List<ElecCommonMsg> list = elecCommonMsgDao.findColletionByConditionNoPage("", null, null);
		ElecCommonMsg commonMsg = null;
		if(list!=null&&list.size()>0){
			commonMsg = list.get(0);
			String stationCondition = " and o.type=?";
			Object [] stationParams = {"1"};
			Map<String, String> stationOrderby = new LinkedHashMap<String,String>();
			stationOrderby.put(" o.orderby", "asc");
			List<ElecCommonMsgContent> stationList = elecCommonMsgContentDao.findColletionByConditionNoPage(stationCondition, stationParams, stationOrderby);
			String stationContent = "";
			if(stationList!=null&&stationList.size()>0){
				for (ElecCommonMsgContent elecCommonMsgContent : stationList) {
					String content = elecCommonMsgContent.getContent();
					stationContent += content;
				}
			}
			commonMsg.setStationRun(stationContent);
			String devCondition = " and o.type=?";
			Object [] devParams = {"2"};
			Map<String, String> devOrderby = new LinkedHashMap<String,String>();
			devOrderby.put(" o.orderby ", "asc");
			List<ElecCommonMsgContent> devList = elecCommonMsgContentDao.findColletionByConditionNoPage(devCondition, devParams, devOrderby);
			String devContent = "";
			if(devList!=null && devList.size()>0){
				for (ElecCommonMsgContent elecCommonMsgContent : devList) {
					String content = elecCommonMsgContent.getContent();
					devContent += content;
				}
			}
			commonMsg.setDevRun(devContent);
		}
		return commonMsg;
	}

	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveCommonMsg(ElecCommonMsg elecCommonMsg) {

		List<ElecCommonMsgContent> contentList = elecCommonMsgContentDao.findColletionByConditionNoPage("", null, null);
		elecCommonMsgContentDao.deleteObjectByCollection(contentList);
		String stationRun = elecCommonMsg.getStationRun();
		String devRun = elecCommonMsg.getDevRun();
		List<String> stationList = StringUtil.getContentByList(stationRun, 50);
		if(stationList!=null && stationList.size()>0){
			for (int i = 0; i < stationList.size(); i++) {
				ElecCommonMsgContent elecCommonMsgContent = new ElecCommonMsgContent();
				elecCommonMsgContent.setType("1");
				elecCommonMsgContent.setContent(stationList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		List<String> devList = StringUtil.getContentByList(devRun, 50);
		if(devList!=null && devList.size()>0){
			for(int i=0;i<devList.size();i++){
				ElecCommonMsgContent elecCommonMsgContent = new ElecCommonMsgContent();
				elecCommonMsgContent.setType("2");//2表示设备运行情况
				elecCommonMsgContent.setContent(devList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		List<ElecCommonMsg> list = elecCommonMsgDao.findColletionByConditionNoPage("", null, null);
		if(list!=null && list.size()>0){
			//方案一：先删除再创建
			//方案二：组织PO对象，执行update
			ElecCommonMsg commonMsg = list.get(0);
			commonMsg.setStationRun("1");//1表示站点运行情况
			commonMsg.setDevRun("2");//2表示设备运行情况
			commonMsg.setCreateDate(new Date());
		}else{
			ElecCommonMsg commonMsg = new ElecCommonMsg();
			commonMsg.setCreateDate(new Date());
			commonMsg.setStationRun("1");//1表示站点运行情况
			commonMsg.setDevRun("2");//2表示设备运行情况
			elecCommonMsgDao.save(commonMsg);
		}
	}

}
