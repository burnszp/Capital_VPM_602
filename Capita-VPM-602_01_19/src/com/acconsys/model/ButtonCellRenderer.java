package com.acconsys.model;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonCellRenderer implements TableCellRenderer {
	private JButton button;
	private int col;

	public ButtonCellRenderer(int col) {
		this.button = new JButton();
		this.button.setText("生成文件");
		this.col = col;
		if(2==col){
			this.button.setText("生成文件");
		}else if(3==col){
			this.button.setText("查看详情");
		}
//		if("createXML".equals(buttonValue)){
//			this.button.setText("生成源文件");
//		}else if("createPDF".equals(buttonValue)){
//			this.button.setText("生成图纸");
//		}else if("createExcel".equals(buttonValue)){
//			this.button.setText("生成明细表");
//		}else{
//			this.button.setText("生成文件(未知类型)");
//		}
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		return this.button;
	}

}
