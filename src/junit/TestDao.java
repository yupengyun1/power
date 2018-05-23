package junit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ityu.elec.dao.IElecTextDao;
import com.ityu.elec.domain.ElecText;



public class TestDao {

	/**����*/
	@Test
	public void save(){
		//����spring����
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		
		ElecText elecText = new ElecText();
		elecText.setTextName("����Dao");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("����Dao��ע");
		elecTextDao.save(elecText);
		
	}
	
	/**����*/
	@Test
	public void update(){
		//����spring����
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		
		ElecText elecText = new ElecText();
		elecText.setTextID("8ab292845fd85e3a015fd85e3cc90001");
		elecText.setTextName("����");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("��С��");
		elecTextDao.update(elecText);
		
	}
	
	/**ʹ������ID��ѯ����Ķ���*/
	@Test
	public void findObjectById(){
		//����spring����
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		Serializable id = "402895ef49f462790149f46280060001";
		ElecText elecText = elecTextDao.findObjectById(id);
		System.out.println(elecText.getTextName()+"    "+elecText.getTextRemark());
	}
	
	/**ɾ����ʹ������IDɾ����*/
	@Test
	public void deleteObjectByIds(){
		//����spring����
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		//Serializable [] ids = {"402895ef49f42c000149f42c0faf0001","402895ef49f42db20149f42db7d10001"};
		Serializable ids = "402895ef49f46a760149f46a7c560001";
		elecTextDao.deleteObjectByIds(ids);
	}
	
	/**ɾ����ʹ�ü���List����ɾ����*/
	@Test
	public void deleteObjectByCollection(){
		//����spring����
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		List<ElecText> list = new ArrayList<ElecText>();
		ElecText elecText1 = new ElecText();
		elecText1.setTextID("402895ef49f46c5f0149f46c64cd0001");
		
		ElecText elecText2 = new ElecText();
		elecText2.setTextID("402895ef49f46cd80149f46d55950001");
		
		ElecText elecText3 = new ElecText();
		elecText3.setTextID("402895ef49f46f060149f46f0cbb0001");
		
		list.add(elecText1);
		list.add(elecText2);
		list.add(elecText3);
		
		elecTextDao.deleteObjectByCollection(list);
	}
}
