package com.acconsys.ui;

import java.awt.Dimension;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mentor.chs.api.IXLogicDesign;

public class ShowLogicPdfInfoDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static String[] headers = { "名称", "路径", "所在文件夹", "最后修改事件",
			"TotalSpace/UsableSpace" };
	Map<IXLogicDesign, List<File>> logicPdfMap;
	List<File> pdfList;
	private Object[][] data;

	public ShowLogicPdfInfoDialog(Map<IXLogicDesign, List<File>> logicPdfMap) {
		this.logicPdfMap = logicPdfMap;
		initUI();
		setTitle("设计图纸生成信息详情");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);//等价于this.setModal(true);
		setModal(true);
		
	}


	private void initUI() {
		data = getDataByList();
		JTable logicInfoTable = new JTable(data, headers);
//		JTable logicInfoTable = new JTable(10, 10);
		JScrollPane jScrollPane = new JScrollPane(logicInfoTable);
		this.add(jScrollPane);
		
	}
	private Object[][] getDataByList() {
		pdfList = new ArrayList<File>();
		for (IXLogicDesign logicDesign : logicPdfMap.keySet()) {
			List<File> list = logicPdfMap.get(logicDesign);
			pdfList.addAll(list);
		}
		int row = pdfList.size();
		int col = headers.length;
		Object[][] fileInfo = new Object[row][col];
		for(int i=0;i<row;i++){
			File file = pdfList.get(i);
			fileInfo[i][0] = file.getName();
			fileInfo[i][1] = file.getPath();
//			fileInfo[i][2] = file.getAbsolutePath();
//			try {
//				fileInfo[i][3] = file.getCanonicalPath();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			fileInfo[i][2] = file.getParent();
			fileInfo[i][3] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(new Date(file.lastModified())) ;
			fileInfo[i][4] = file.getTotalSpace()+"/"+file.getUsableSpace();
			
		}
		return fileInfo;
	}

//	public static void main(String[] args) {
//		ShowLogicXmlInfoDialog slxiDialog = new ShowLogicXmlInfoDialog(null,null);
//		slxiDialog.setVisible(true);
//	}

}
