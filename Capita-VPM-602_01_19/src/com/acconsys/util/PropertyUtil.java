package com.acconsys.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 这个类主要是是将conf中的properties文件中的配置信息保存起来，方便使用
 * 
 * @author Administrator
 * 
 */
public class PropertyUtil {
	private static String CAPITAL_VPM_602 = "../plugins/conf/602.properties";
//	private static String CAPITAL_VPM_602 = "conf/602.properties" ;
	private static Properties p;
	private static PropertyUtil propertyUtil;
	private static FileInputStream inputStream;

	public PropertyUtil() {
		this.p = new Properties();
	}

	public static PropertyUtil getPropertyUtil() {
		if (propertyUtil == null) {
			propertyUtil = new PropertyUtil();
			 p = new Properties();
		        try {
		            inputStream = new FileInputStream(CAPITAL_VPM_602);
		            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		            p.load(bf);
		            System.out.println("配置文件信息:"+p);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }finally{
		        	try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					} // 关闭流
		        }
		}
		return propertyUtil;
	}

	public String getValue(String key) {

		return p.getProperty(key);
	}

	public static void main(String[] args) {
		PropertyUtil u = PropertyUtil.getPropertyUtil();
		System.out.println(u.getValue(Constants.FILE_PATH));
//		u.p.setProperty(Constants.VPM_USERNAME, "vpmU");
//		System.out.println("size(): " + u.p.size());
//		try {
//			u.p.list(new PrintStream("./temp/Capital-VPM-602.properties"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
