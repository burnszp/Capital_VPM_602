package com.acconsys.util;
/*******************************************************************************
 * @project:
 * @package: com.acconsys.docgen.freemarker
 * @file: DGFreemarkerToDoc.java
 * @author: Administrator
 * @created: 2011-4-25
 * @purpose:
 * 
 * @version: 1.0
 * 
 * Revision History at the end of file.
 * 
 * Copyright 2011 AcconSys All rights reserved.
 ******************************************************************************/



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DGFreemarkerToDoc {
	
	public void outPutFile(Object object,
										String outFile ,String inFile,String freemarkerName) 
											throws IOException, TemplateException {
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(inFile));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setEncoding(Locale.CHINA,"UTF-8");
		/* 在整个应用的生命周期中，这个工作你可以执行多次 */
		/* 获取或创建模板*/
		Template temp = cfg.getTemplate(freemarkerName);
		  temp.setEncoding("UTF-8");
		/* 创建数据模型 */
		Map<String,Object> root = new HashMap<String ,Object>();
		root.put("dataModel", object);
		/* 将模板和数据模型合并 */
		File outputFile = new File(outFile);//建立文件输出
		Writer outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
		//FileWriter out = new FileWriter(outputFile);
		temp.process(root, outWriter);
		outWriter.flush();
		outWriter.close();
	}
}


/*******************************************************************************
 * <B>Revision History</B><BR>
 * [type 'revision' and press Alt + / to insert revision block]<BR>
 * 
 * 
 * 
 * Copyright 2011 AcconSys All rights reserved.
 ******************************************************************************/
