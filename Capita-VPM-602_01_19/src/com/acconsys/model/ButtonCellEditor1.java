package com.acconsys.model;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ButtonCellEditor1 extends DefaultCellEditor {

	private static final long serialVersionUID = -6546334664166791132L;
	private JButton button;
	private JTable table;
	int col;

	public ButtonCellEditor1(JTable table,int col) {
		super(new JTextField());
		this.setClickCountToStart(1);
		this.initButton();
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
		this.table = table;
	}

	private void initButton() {
		this.button = new JButton();
		this.button.setSize(50, 15);
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ButtonCellEditor1.this.fireEditingCanceled();
				System.out.println("Selected Column:"
						+ table.getSelectedColumn()
						+ ",row:"
						+ table.getSelectedRow()
						+ ",and filePath:"
						+ table.getModel()
								.getValueAt(table.getSelectedRow(), col));
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		return this.button;
	}

	@Override
	public Object getCellEditorValue() {
		return this.button.getText();
	}
}
