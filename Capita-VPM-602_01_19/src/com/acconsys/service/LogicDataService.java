/*******************************************************************************
 * @project: 602
 * @package: com.acconsys.plugins.service
 * @file: LogicDateService.java
 * @author: Administrator
 * @created: 2012-6-15
 * @purpose:
 * 
 * @version: 1.0
 * 
 * Revision History at the end of file.
 * 
 * Copyright 2012 AcconSys All rights reserved.
 ******************************************************************************/

package com.acconsys.service;

import java.util.Iterator;
import java.util.Set;

import com.acconsys.model.Terminal;
import com.acconsys.model.TerminalTableModel;
import com.mentor.chs.api.IXAbstractConductor;
import com.mentor.chs.api.IXAbstractPin;
import com.mentor.chs.api.IXConnectivity;
import com.mentor.chs.api.IXConnector;
import com.mentor.chs.api.IXDesign;
import com.mentor.chs.api.IXLibrariedObject;
import com.mentor.chs.api.IXLibrary;
import com.mentor.chs.api.IXLibraryComponentTypeCode;
import com.mentor.chs.api.IXLibraryHousingDefinition;
import com.mentor.chs.api.IXLibraryObject;
import com.mentor.chs.api.IXMulticore;
import com.mentor.chs.api.IXObject;
import com.mentor.chs.api.IXSplice;
import com.mentor.chs.api.IXWire;
import com.mentor.chs.plugin.IXApplicationContext;

public class LogicDataService {
	private IXDesign design ;
	private IXApplicationContext context;
	private TerminalTableModel tableModel;
	private IXLibrary library ;
	public LogicDataService(IXApplicationContext context) {
		this.context = context ;
		this.design = context.getCurrentDesign();
		this.library = context.getLibrary() ;
		tableModel = new TerminalTableModel();
	}
	public LogicDataService(IXApplicationContext context, IXDesign design) {
		this.context = context ;
		this.design = design;
		this.library = context.getLibrary() ;
		tableModel = new TerminalTableModel();
	}
	public TerminalTableModel getTerminalTableModel() {
		
		if(design != null) {
			tableModel.setProjectName(context.getCurrentProject().getAttribute("Name"));
			tableModel.setDesignName(design.getAttribute("Name"));
			tableModel.setRevision(design.getAttribute("Revision"));
			tableModel.setDescription(design.getAttribute("Description"));
			tableModel.setShortDescription(design.getProperty("ChineseName")!= null ?design.getProperty("ChineseName") :" ");
			IXConnectivity connectivitie = design.getConnectivity(); 
			Set<IXMulticore> multicores = connectivitie.getMulticores();
			getMulticores(multicores);
			Set<IXWire> wires = connectivitie.getWires();
			setWires(wires);
			Set<IXSplice> splices = connectivitie.getSplices();
			getSplices(splices);
			Set<IXConnector> connectors = connectivitie.getConnectors();
			getConnectors(connectors);
			tableModel.updateModel();
		}
		return tableModel;
	}
	/**
	 * 添加单线 内芯线不计算
	 * @param wires
	 */
	public void setWires(Set<IXWire> wires ) {
		for(IXWire wire :wires) {
			if(wire.getMulticore() == null) {
				Terminal terminal = new Terminal();
				terminal.setPartNumber(wire.getAttribute("partNumber"));
				terminal.setName(wire.getAttribute("Name"));
				terminal.setDescripton(getDescription(wire));
				terminal.setType(wire.getProperty("Type"));
				terminal.setLength(wire.getAttribute("WireLength") != null ? ("".equals(wire.getAttribute("WireLength")) ? "0" :wire.getAttribute("WireLength") ) :"0");
				terminal.setWeight(getWeight(wire));
				tableModel.getStmatList().add(terminal);
			}
		}
	}
	/**
	 * 添加多芯线
	 * @param multicores
	 */
	public void getMulticores(Set<IXMulticore> multicores) {
		for(IXMulticore multicore : multicores) {
			Terminal terminal = new Terminal();
			terminal.setPartNumber(multicore.getAttribute("partNumber"));
			terminal.setName(multicore.getAttribute("Name"));
			terminal.setDescripton(getDescription(multicore));
			terminal.setType(multicore.getProperty("Type"));
			terminal.setLength(getMulticoreLength(multicore));
			terminal.setWeight(getWeight(multicore));
			tableModel.getStmatList().add(terminal);
		}
	}
	/**
	 * 添加Connector
	 * @param connectors
	 */
	public void  getConnectors(Set<IXConnector> connectors) {
		for(IXConnector connector : connectors) {
			String type = connector.getProperty("Type");
			if("STProd".equals(type)) {
				if(connector.getProperty("BelongTo") == null) {
					Terminal terminal = new Terminal();
					terminal.setPartNumber(connector.getAttribute("partNumber"));
					terminal.setName(connector.getAttribute("Name"));
					terminal.setDescripton(getDescription(connector));
					terminal.setType(connector.getProperty("Type"));
					terminal.setLength(connector.getAttribute("WireLength"));
					terminal.setWeight(getWeight(connector));
					tableModel.getStprodList().add(terminal);
				}else if(design.getAttribute("Name").equals(connector.getProperty("BelongTo"))) {
					Terminal terminal = new Terminal();
					terminal.setPartNumber(connector.getAttribute("partNumber"));
					terminal.setName(connector.getAttribute("Name"));
					terminal.setDescripton(getDescription(connector));
					terminal.setType(connector.getProperty("Type"));
					terminal.setLength(connector.getAttribute("WireLength"));
					terminal.setWeight(getWeight(connector));
					tableModel.getStprodList().add(terminal);
				}
			}else if(connector!= null) {
				Set<IXAbstractPin> pins = connector.getPins();
				boolean isTerminal = false ;
				for(IXAbstractPin pin : pins) {
					if("circle".equals(pin.getAttribute("PinGraphic"))) {
						isTerminal = true ;
					}
				}
				if(isTerminal) {
					Terminal terminal = new Terminal();
					terminal.setPartNumber(connector.getAttribute("partNumber"));
					terminal.setName(connector.getAttribute("Name"));
					terminal.setDescripton(getDescription(connector));
					terminal.setTerNum("1");
					tableModel.getScompList().add(terminal);
					if(Boolean.valueOf(connector.getAttribute("SealsRequired"))) {
						 getHousingTer(connector.getAttribute("PartNumber"));
					}
				}
				String pn = connector.getProperty("OtherPart");
				if((!"".equals(pn)) && !(pn==null) ) {
					Terminal terminal = new Terminal();
					terminal.setPartNumber(pn);
					terminal.setName(connector.getAttribute("Name"));
					terminal.setDescripton(getDescription(pn));
					terminal.setTerNum("1");
					tableModel.getScompList().add(terminal);
				}
			}
			
		}
	}
	/**
	 * 对于splice，Type Code=DIOH，则计入2个PN为：20# 
	 * @param splices
	 */
	private void getSplices(Set<IXSplice> splices) {
		for(IXSplice splice : splices) {
			if(!"".equals(splice.getAttribute("partNumber"))) {
			
				IXLibraryObject libObjSplice = library.getLibraryObject(splice.getAttribute("partNumber"));
				IXLibraryComponentTypeCode ty = libObjSplice.getComponentTypeCode() ;
				String typeCode = ty.getAttribute("TypeCode");
				if("DIOH".equals(typeCode)) {
					getSplice();
				}
				
			}
		}
	}
	/**
	 * 获得对象的description的值
	 * @param partnumber
	 * @return
	 */
	private  String getDescription(String partnumber) {
		String result = "" ;
		if(partnumber != null && !"".equals(partnumber)) {
			IXLibraryObject libraryObject = library.getLibraryObject(partnumber);
			 return libraryObject.getAttribute("Description");
		}
		return result;
	}
	private String getDescription(IXObject object) {
		String result = "" ;
		if( object instanceof IXLibrariedObject) {
			 IXLibrariedObject libed = (IXLibrariedObject)object;
			 if(libed.getLibraryObject()!= null){
				 IXLibraryObject lib = libed.getLibraryObject();
				 return lib.getAttribute("Description");
			 }
		}
		return result;
	}
	private String getWeight(IXObject object) {
		String result = "" ;
		if( object instanceof IXLibrariedObject) {
			 IXLibrariedObject libed = (IXLibrariedObject)object;
			 if(libed.getLibraryObject()!= null){
				 IXLibraryObject lib = libed.getLibraryObject();
				 return lib.getAttribute("Weight");
			 }
		}
		return result;
	}
	private void getHousingTer(String pn) {
		String result = "" ;
		if( pn != null && !"".equals(pn)){
				 IXLibraryObject lib =library.getLibraryObject(pn);
				 Set<IXLibraryHousingDefinition> 	ixLibraryHousingDefinitions = 	lib.getHousingDefinitions();
				 Iterator iterator = ixLibraryHousingDefinitions.iterator();
				 if(iterator.hasNext()) {
					 IXLibraryHousingDefinition housingDefinition = (IXLibraryHousingDefinition) iterator.next();
					 IXLibraryObject libraryObjecthousing =  housingDefinition.getSubComponent();
					 Terminal terminal = new Terminal();
					 terminal.setPartNumber(libraryObjecthousing.getAttribute("partNumber"));
					 terminal.setName(libraryObjecthousing.getAttribute("Name"));
					 terminal.setDescripton(libraryObjecthousing.getAttribute("Description"));
					 terminal.setTerNum("1");
					 tableModel.getScompList().add(terminal);
				 }
		}
	}
	private void getSplice() {
		
		for(int i = 0 ;i<2 ;i++) {
			IXLibraryObject libraryObject = library.getLibraryObject("20#");
			if(libraryObject != null ) {
				Terminal terminal = new Terminal();
				terminal.setPartNumber(libraryObject.getAttribute("partNumber"));
				terminal.setName(libraryObject.getAttribute("Name"));
				terminal.setDescripton(libraryObject.getAttribute("Description"));
				terminal.setType(libraryObject.getProperty("Type"));
				terminal.setLength(libraryObject.getAttribute("WireLength"));
				terminal.setWeight(getWeight(libraryObject));
				tableModel.getStprodList().add(terminal);
			}
			
			
		}
	}
	private String  getMulticoreLength(IXMulticore multicore ) {
		float length  = 0f ;
		float temp = 0f;
		if(multicore != null) {
			Set<IXAbstractConductor> wires = multicore.getConductors();
			for(IXAbstractConductor conductor : wires) {
				conductor.getAttribute("WireLength");
				if(!"".equals(conductor.getAttribute("WireLength"))) {
					try {
						temp = Float.valueOf(conductor.getAttribute("WireLength"));
					} catch (Exception e) {
						// TODO: handle exception
						temp = 0f; 
					}
					if(length < temp) {
						length =temp ;
					}
					
				}
			}
		}
		return String.valueOf(length);
	}
	
}


/*******************************************************************************
 * <B>Revision History</B><BR>
 * [type 'revision' and press Alt + / to insert revision block]<BR>
 * 
 * 
 * 
 * Copyright 2012 AcconSys All rights reserved.
 ******************************************************************************/
