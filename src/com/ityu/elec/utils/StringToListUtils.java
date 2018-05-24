package com.ityu.elec.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringToListUtils {

	/**将一个字符串，按照指定的flag符号进行分割*/
	public static List<String> stringToList(String name, String flag) {
		List<String> list = new ArrayList<String>();
		if(StringUtils.isNotBlank(name)){
			String [] arrays = name.split(flag);
			if(arrays!=null && arrays.length>0){
				for(String array:arrays){
					list.add(array);
				}
			}
		}
		return list;
	}

}
