/*******************************************************************************
 * @project: testXML
 * @package: com.acconsys.chs.docgen.excel
 * @file: Terminal.java
 * @author: Administrator
 * @created: 2011-7-14
 * @purpose:
 * 
 * @version: 1.0
 * 
 * Revision History at the end of file.
 * 
 * Copyright 2011 AcconSys All rights reserved.
 ******************************************************************************/

package com.acconsys.model;


public class Terminal implements Comparable<Terminal>{
	private String name ;
	private String partNumber;
	private String descripton;
	private String weight;
	private String dimensions;//外形尺寸
	private String type;
	private String length;
	private String quantity;
	private String sharedbelong;
	private String terNum;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSharedbelong() {
		return sharedbelong;
	}
	public void setSharedbelong(String sharedbelong) {
		this.sharedbelong = sharedbelong;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getDescripton() {
		return descripton;
	}
	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTerNum() {
		return terNum;
	}
	public void setTerNum(String terNum) {
		this.terNum = terNum;
	}
	@Override
	public int compareTo(Terminal ter) {
		// TODO Auto-generated method stub
		return this.descripton.compareTo(ter.getDescripton());
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
