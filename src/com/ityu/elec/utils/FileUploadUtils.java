package com.ityu.elec.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

public class FileUploadUtils {

	/**
	 * 完成文件上传，同时返回相对路径的path
	 * 要求：
	 *    * 要求上传的文件名称支持同名，使用UUID的方式作为文件名
		  * 将上传的文件，放置到项目路径下的upload文件夹下
		  * 将上传的文件统一使用日期文件夹的形式维护
		    upload/2013/11/27/Xxxxx.dox
		    upload/2013/11/28/Yyyyy.dox
		  * 存放PO对象中的path路径的时候，使用相对路径（采用项目路径）
	 * @param upload:上传文件
	 * @param fileName：文件名称
	 * @return 路径path
	 */
	public static String fileUploadReturnPath(File upload, String fileName) {
		//查找项目中的upload的文件夹
		String basePath = ServletActionContext.getServletContext().getRealPath("/upload");
		//创建项目中日期目录（针对当前时间）
		String datePath = new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());
		//文件名采用UUID的形式（防止文件名称冲突）,组织Xxxxx.dox
		String name = UUID.randomUUID().toString()+""+fileName.substring(fileName.lastIndexOf("."));
		//如果日期文件夹不存在，先创建日期文件夹
		String dateString = basePath + datePath;
		File file = new File(dateString);
		if(!file.exists()){
			file.mkdirs();
		}
		//文件上传
		upload.renameTo(new File(basePath+datePath+name));
		/**保存相对路径*/
		String path = "/upload" + datePath + name;
		return path;
	}
	
	/**测试文件上传*/
	public static void main(String[] args) {
		//原文件
		File srcFile = new File("F:/dir/a.txt");
		//目标文件
		File destFile = new File("F:/dir/dir2xxxxxxxxx/a.txt");
		//文件上传(复制)
//		try {
//			FileUtils.copyFile(srcFile, destFile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//文件上传（剪切）
		boolean flag = srcFile.renameTo(destFile);
		System.out.println(flag);
	}

}
