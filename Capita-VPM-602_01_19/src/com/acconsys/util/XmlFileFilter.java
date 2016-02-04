package com.acconsys.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 以JAVAFileFilter类继承FileFilter抽象类，并实现accept()和getDescription()方法
 * 
 * @author Administrator
 * 
 */
public class XmlFileFilter extends FileFilter {

	String ext;

	public XmlFileFilter(String ext) {
		this.ext = ext;
	}

	@Override
	public boolean accept(File file) {
		// 在accept()方法中，当程序返回的是一个目录而不是一个文件时，返回true,表示将此目录显示出来
		if (file.isDirectory()) {
			return true;
		}
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".");
		if (index > 0 && index < fileName.length() - 1) {
			String extension = fileName.substring(index + 1).toLowerCase();// 表示文件名称不为".xxx"或"xxx."类型
			if (extension.equals(ext)) {
				// 若返回的文件扩展名等于所设置的扩展名，则返回true，表示将此文件显示出来，否则返回false
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		// 实现getDescription()方法，返回描述文件的说明字符串
		if (ext.equals("xml")) {
			return "XML文件(*.xml)";
		}
		return "";
	}

}