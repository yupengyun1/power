package com.ityu.elec.web.action;

import java.io.PrintWriter;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ityu.elec.domain.ElecCommonMsg;
import com.ityu.elec.service.IElecCommonMsgService;
import com.ityu.elec.utils.ValueUtils;

@Controller("elecCommonMsgAction")
@Scope(value="prototype")
public class ElecCommonMsgAction extends BaseAction<ElecCommonMsg>{
	ElecCommonMsg elecCommonMsg = this.getModel();

	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	public String home(){
		//1����ѯ���ݿ����м�ر�����ݣ�����ΩһElecCommonMsg
		ElecCommonMsg commonMsg = elecCommonMsgService.findCommonMsg();
		//2����ElecCommonMsg����ѹ��ջ����֧�ֱ�����
		ValueUtils.putValueStack(commonMsg);
		return "home";
	}
	
	
	/**
	 * name: ������
	 * @return
	 */
	public String save(){
		//ģ�Ᵽ��150�Σ��������ٷֱ�
		for(int i=1;i<=150;i++){
			elecCommonMsgService.saveCommonMsg(elecCommonMsg);
			request.getSession().setAttribute("percent", (double)i/150*100);//��ż���İٷֱ�
		}
		//�߳̽���ʱ����յ�ǰsession
		request.getSession().removeAttribute("percent");
		return "save";
	}
	/**  
	* @throws Exception 
	 * @Name: progressBar
	* @Description: ��ҳ��ִ�б����ʹ��ajax������ִ�еİٷֱ�������������ʾ��ҳ����
	* @Author: �����ƣ����ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2014-12-1���������ڣ�
	* @Parameters: ��
	* @Return: ajax�������Ҫ��תҳ�棬����null����NONE
	*/
	public String progressBar() throws Exception{
		//��session�л�ȡ���������м���İٷֱ�
		Double percent = (Double) request.getSession().getAttribute("percent");
		String res = "";
		//��ʱ˵��������ҵ�񷽷���Ȼ������ִ��
		if(percent!=null){
			//�����С������������ȡ��
			int percentInt = (int) Math.rint(percent); 
			res = "<percent>" + percentInt + "</percent>";
		}
		//��ʱ˵��������ҵ�񷽷��Ѿ�ִ����ϣ�session�е�ֵ�Ѿ������
		else{
			//��Űٷֱ�
			res = "<percent>" + 100 + "</percent>";
		}
		//����ajax�ķ��ؽ����XML����ʽ
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		//��Ž�����ݣ����磺<response><percent>88</percent></response>
		out.println("<response>");
		out.println(res);
		out.println("</response>");
		out.close();
		return null;

	}

	public String actingView(){
		//��ѯ���м�ص�����
		//1����ѯ���ݿ����м�ر�����ݣ�����ΩһElecCommonMsg
		ElecCommonMsg commonMsg = elecCommonMsgService.findCommonMsg();
		//2����ElecCommonMsg����ѹ��ջ����֧�ֱ�����
		ValueUtils.putValueStack(commonMsg);
		return "actingView";
	}
}
