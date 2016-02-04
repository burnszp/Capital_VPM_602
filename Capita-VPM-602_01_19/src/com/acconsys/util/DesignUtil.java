package com.acconsys.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mentor.chs.api.IXLogicDesign;

public class DesignUtil {
	
	private static DesignUtil designUtil;
	
	public static DesignUtil getDesignUtil(){
		if(designUtil == null){
			designUtil = new DesignUtil();
		}
		return designUtil;
	}

	/**
	 * logic设计转化成JTable数据对象。
	 * @param logicDesignList
	 * @param headers
	 * @return
	 */
	public static  Object[][] getDataByList(List<IXLogicDesign> logicDesignList,
			int columnCount) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String longtime = logicList.get(i).getAttribute("ModifiedOn");
		// logicTableData[i][4] = df.format(new Date(new Long(longtime)));
		Object[][] logicData = new Object[logicDesignList.size()][columnCount];
		for (int i = 0; i < logicDesignList.size(); i++) {
			IXLogicDesign logicDesign = logicDesignList.get(i);
			logicData[i][0] = Boolean.FALSE;
			logicData[i][1] = logicDesign.getAttribute("Name");
			logicData[i][2] = logicDesign.getAttribute("Revision");
			logicData[i][3] = logicDesign.getAttribute("ReleaseLevel");
			logicData[i][4] = logicDesign.getAttribute("CreatedBy");
			logicData[i][5] = df.format(new Date(new Long(logicDesign
					.getAttribute("CreatedOn"))));
			logicData[i][6] = logicDesign.getAttribute("ModifiedBy");
			logicData[i][7] = df.format(new Date(new Long(logicDesign
					.getAttribute("ModifiedOn"))));
			logicData[i][8] = logicDesign.getProperty("CheckInOrOutBy") == null ? "无"
					: logicDesign.getProperty("CheckInOrOutBy");
			logicData[i][9] = logicDesign.getProperty("CheckInOrOutOn") == null ? "无"
					: df.format(new Date(new Long(logicDesign
							.getAttribute("CheckInOrOutOn"))));
		}
		return logicData;
	}
	
	public static Map<String,IXLogicDesign> getLogicMap(List<IXLogicDesign> logicDesignList){
		Map<String,IXLogicDesign> logicMap = new HashMap<String, IXLogicDesign>();
		for (IXLogicDesign logicDesign : logicDesignList) {
			String designName = logicDesign.getAttribute("Name");
			String releaseLevel = logicDesign.getAttribute("Revision");
			logicMap.put(designName+releaseLevel, logicDesign);
		}
		return logicMap;
	}
	
}
