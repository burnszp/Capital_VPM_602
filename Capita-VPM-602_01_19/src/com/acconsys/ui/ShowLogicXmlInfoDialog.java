package com.acconsys.ui;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
/**
 * 展示生成文件信息的
 * @author Administrator
 *
 */
public class ShowLogicXmlInfoDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static String[] headers = {"名称","路径","所在文件夹","最后修改事件","TotalSpace/UsableSpace"};
	private List<File> logicXmlList;
	private Object[][] data;
	public ShowLogicXmlInfoDialog(List<File> logicXmlList) {
		this.logicXmlList = logicXmlList;
		initUI();
		setTitle("设计生成信息详情");
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
		int row = logicXmlList.size();
		int col = headers.length;
		Object[][] fileInfo = new Object[row][col];
		for(int i=0;i<row;i++){
			File file = logicXmlList.get(i);
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
