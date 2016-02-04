/*******************************************************************************
 * @project: testXML
 * @package: com.acconsys.chs.docgen.excel
 * @file: TerminalTableModel.java
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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
public class TerminalTableModel {
	public static Logger log = Logger.getLogger(TerminalTableModel.class);
	private String projectName;  //型号
	private String designName;  //图号
	private String description;  //名称
	private String revision;  //本版
	private String modelType;//型号
	private String shortDescription;
	private String totalweight;
	private List<Terminal> scompList;
	private List<Terminal> stprodList;
	private List<Terminal> stmatList;
	private List<Terminal> sbList ;
	public TerminalTableModel() {
		scompList = new ArrayList<Terminal>();
		stprodList = new ArrayList<Terminal>();
		stmatList = new ArrayList<Terminal>();
		sbList = new ArrayList<Terminal>();
	}
	public List<Terminal> getSbList() {
		return sbList;
	}
	public void setSbList(List<Terminal> sbList) {
		this.sbList = sbList;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	
	public String getTotalweight() {
		return totalweight;
	}
	public void setTotalweight(String totalweight) {
		this.totalweight = totalweight;
	}
	public List<Terminal> getScompList() {
		return scompList;
	}
	public void setScompList(List<Terminal> scompList) {
		this.scompList = scompList;
	}
	public List<Terminal> getStprodList() {
		return stprodList;
	}
	public void setStprodList(List<Terminal> stprodList) {
		this.stprodList = stprodList;
	}
	public List<Terminal> getStmatList() {
		return stmatList;
	}
	public void setStmatList(List<Terminal> stmatList) {
		this.stmatList = stmatList;
	}
	public void addItem(Terminal item) {
		if("STComp".equals(item.getType())) {
			scompList.add(item);
		}
		if("STProd".equals(item.getType())) {
			stprodList.add(item);
		}
		if("STMat".equals(item.getType())) {
			stmatList.add(item);
		}
		if(item.getSharedbelong().equals(designName)) {
			sbList.add(item);
		}
	}
	public String getDesignName() {
		return designName;
	}
	public void setDesignName(String designName) {
		this.designName = designName;
	}
	public void updateModel() {
		updateScompList();
		updateStprodList();
		updateStmatList();
		updataSbList();
		updateTotalWeight();
		updateStmatListLength();
	}
	private void updateStmatListLength() {
		for(Terminal terminal :stmatList) {
			String length = terminal.getQuantity();
			if(!"".equals(length) && length != null) {
				float l = Float.valueOf(length);
				String sdouble = dealFloatUtil(l/1000,4);
				log.error("length :---:"+sdouble);
				terminal.setQuantity(sdouble);
			}
			
		}
	}
	public void updateTotalWeight() {
		float  weight =0;
		float wei  = 0;
		float len = 0 ;
		int sum = 0;
		for(Terminal ter:scompList) {
			if(!"".equals(ter.getWeight())&&ter.getWeight()!=null) {
				 wei = Float.valueOf(ter.getWeight());
				 sum = Integer.valueOf(ter.getQuantity());
			}else{
				 wei = 0;
			}
			weight =weight+(wei*sum);
			String sdouble = dealFloatUtil(wei,4);
			log.error(ter.getPartNumber() + "   weight :---:"+sdouble);
			ter.setWeight(sdouble);
		}
		for(Terminal ter:stprodList) {
			if(!"".equals(ter.getWeight())&&ter.getWeight()!=null) {
				 wei = Float.valueOf(ter.getWeight());
				 sum = Integer.valueOf(ter.getQuantity());
			}else{
				 wei = 0;
			}
			weight =weight+(wei*sum);
			String sdouble = dealFloatUtil(wei,4);
			log.error(ter.getPartNumber() + "   weight :---:"+sdouble);
			ter.setWeight(sdouble);
		}
		for(Terminal ter:stmatList) {
			if(!"".equals(ter.getWeight())&&ter.getWeight()!=null) {
				 wei = Float.valueOf(ter.getWeight());
				 len = Float.valueOf(ter.getQuantity());
			}else{
				 wei = 0;
			}
			wei=wei * len;
			weight =(weight+wei);
			String sdouble = dealFloatUtil(wei,4);
			log.error(ter.getPartNumber() + "   weight :---:"+sdouble);
			ter.setWeight(sdouble);
		}
		for(Terminal ter:sbList) {
			if(!"".equals(ter.getWeight())) {
				 wei = Float.valueOf(ter.getWeight());
				 sum = Integer.valueOf(ter.getQuantity());
			}else{
				 wei = 0;
			}
			weight =weight+(wei*sum);
			String sdouble = dealFloatUtil(wei,4);
			log.error(ter.getPartNumber() + "   weight :---:"+sdouble);
			ter.setWeight(sdouble);
		}
		
		totalweight = dealFloatUtil(weight,4);
		log.error( "总重量     weight :---:"+totalweight);
	}
	
	
	public void updateScompList() {
		Map<String, ItemCounter> maps = new HashMap<String, ItemCounter>();
		
		ItemCounter counter = null;
		for (Terminal item : scompList) {
			counter = maps.get(item.getPartNumber());
			if (counter == null) {
				counter = new ItemCounter(item);
				if(item.getPartNumber()!=null||"".equals(item.getPartNumber())) 
				maps.put(item.getPartNumber(), counter);
			}
			counter.increase(item);
		}
		scompList.clear();
		for (Map.Entry<String, ItemCounter> entry : maps.entrySet()) {
			entry.getValue().count();
			scompList.add(entry.getValue().getItem());
		}
		Collections.sort(scompList);
	}
	public void updataSbList() {
		Map<String, ItemCounter> maps = new HashMap<String, ItemCounter>();
		ItemCounter counter = null;
		for (Terminal item : sbList) {
			counter = maps.get(item.getPartNumber());
			if (counter == null) {
				counter = new ItemCounter(item);
				maps.put(item.getPartNumber(), counter);
			}
			counter.increase(item);
		}
		sbList.clear();
		for (Map.Entry<String, ItemCounter> entry : maps.entrySet()) {
			entry.getValue().count();
			sbList.add(entry.getValue().getItem());
		}
	}
	public void updateStprodList() {
		Map<String, ItemCounter> maps = new HashMap<String, ItemCounter>();
		ItemCounter counter = null;
		for (Terminal item : stprodList) {
			counter = maps.get(item.getPartNumber());
			if (counter == null) {
				counter = new ItemCounter(item);
				maps.put(item.getPartNumber(), counter);
			}
			counter.increase(item);
		}
		stprodList.clear();
		for (Map.Entry<String, ItemCounter> entry : maps.entrySet()) {
			entry.getValue().count();
			stprodList.add(entry.getValue().getItem());
		}
	}
	public void updateStmatList() {
		Map<String, ItemCounter> maps = new HashMap<String, ItemCounter>();
		Iterator<Terminal> itemIt = stmatList.iterator();
		Terminal item = null;
		ItemCounter counter = null;
		while (itemIt.hasNext()) {
			item = itemIt.next();
			counter = maps.get(item.getPartNumber());
			if (counter == null) {
				counter = new ItemCounter(item);
				maps.put(item.getPartNumber(), counter);
				continue;
			}
			counter.asmLength(item);
			// 删掉
			itemIt.remove();
		}
		for (Map.Entry<String, ItemCounter> entry : maps.entrySet()) {
			item = entry.getValue().getItem();
			if (!"".equals(item.getLength())) {
				entry.getValue().length(entry.getValue().unit);
			}
		}
	}
	
	
	
	
	
	class ItemCounter {
		private int count;
		private float length;
		private String unit;
		private Terminal item;

		/**
		 * @param item
		 */
		public ItemCounter(Terminal item) {
			String s1 = item.getLength();
			if(s1 == null) {
				s1 = "";
			}
			String ss1 = "";
			String ss2 = "";
			String vowel = "\\d+.\\d+|\\p{Punct}|[a-z]+";
			Pattern p = Pattern.compile(vowel);
			Matcher m = p.matcher(s1);
			int i = 0;
			while (m.find()) {
				if (i == 0) {
					ss1 = m.group();
					i++;
				} else {
					ss2 = m.group();
				}
			}
			if ("mm".equals(ss2)) {
				this.unit = "mm";
			} else if ("g".equals(ss2)){
				this.unit = "g";
			}else {
				this.unit = "";
			}
			this.item = item;
			this.count = 0;
			
			try {
				this.length = "".equals(Float.valueOf(ss1)) ? 0.000f : Float
						.valueOf(ss1);
				
			} catch (Exception e) {
				// TODO: handle exception
				
			}
		}

		/**
		 * 
		 */
		public void increase(Terminal item2) {
			if((!"".equals(item2.getTerNum()))&&( item2.getTerNum() != null)) {
				this.count = this.count + Integer.valueOf(item2.getTerNum());
			}else {
				this.count++;
			}
		}

		/**
		 * @param item2
		 */
		public void asmLength(Terminal item2) {
			if (!"".equals(item2.getLength())) {
				float len2 = Float.parseFloat(item2.getLength());
				length = length + len2;
			//System.out.println(length);
			}else {
				length = length + 0;
			}
		}

		/**
		 * 
		 */

		public void count() {
			item.setQuantity(String.valueOf(count));
		}

		public void length(String str) {
			if ("mm".equals(str)) {
				String wei = String.valueOf(length*Float.valueOf(item.getWeight()));
				item.setWeight(wei);
				item.setQuantity(String.valueOf(length));
			} else if("g".equals(str)){
				item.setQuantity(String.valueOf(length));
			}else {
				item.setQuantity(String.valueOf(length));
			}
		}

		/**
		 * @param item
		 */
		public void setItem(Terminal item) {
			this.item = item;
		}

		/**
		 * @return
		 */
		public Terminal getItem() {
			return item;
		}
	}
	
	public String  dealFloatUtil(float f,int i) {
		BigDecimal bd = new BigDecimal(f);
		bd= bd.setScale(i, BigDecimal.ROUND_HALF_UP);
		NumberFormat formatter = NumberFormat.getNumberInstance();
		  formatter.setMaximumFractionDigits(i);
		  String sdouble = formatter.format(bd);
		  return sdouble;
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
