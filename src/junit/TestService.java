package junit;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ityu.elec.domain.ElecText;
import com.ityu.elec.service.IElecTextService;


public class TestService {

	/**����*/
	@Test
	public void save(){
		//����spring����
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextService elecTextService = (IElecTextService) ac.getBean(IElecTextService.SERVICE_NAME);
		
		ElecText elecText = new ElecText();
		elecText.setTextName("����Service����");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("����Service��ע");
		elecTextService.saveElecText(elecText);
		
	}
	
	/**ģ��Action�㣬���Եײ㷽����ָ����ѯ������ѯ����б�*/
	@Test
	public void findCollectionByConditionNoPage(){
		//����spring����
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextService elecTextService = (IElecTextService) ac.getBean(IElecTextService.SERVICE_NAME);
		
		//ģ�������з�װ���
		ElecText elecText = new ElecText();
		elecText.setTextName("��");
		elecText.setTextRemark("��");
		List<ElecText> list = elecTextService.findCollectionByCondittionNoPage(elecText);
		if(list!=null && list.size()>0){
			for(ElecText text:list){
				System.out.println(text.getTextName()+"   "+text.getTextRemark());
			}
		}
		
	}
}
