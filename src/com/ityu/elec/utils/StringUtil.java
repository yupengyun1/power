package com.ityu.elec.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static List<String> getContentByList(String wholecontent,int cutcount){
		List<String> list = new ArrayList<String>();
		int contentlen = wholecontent.length();
		if(contentlen<cutcount){
			list.add(wholecontent);
		}else{
			String contentpart = "";
			int contentround = 0;
			int begincount = 0;
			int contentcutpart = contentlen/cutcount;
			int contentcutparts = contentlen%cutcount;
			if(contentcutparts==0){
				contentround = contentcutpart;
			}else{
				contentround = contentcutpart+1;
			}
			for (int i = 0; i < contentround; i++) {
				if(i!=contentround){
					contentpart = wholecontent.substring(begincount,cutcount*i);
				}else{
					contentpart = wholecontent.substring(begincount,contentlen);
				}
				begincount = cutcount*i;
				list.add(contentpart);
			}
		}
		return list;
		
	}
}
