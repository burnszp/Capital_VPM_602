package com.acconsys.model;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
	private String headName[];
	private Object obj[][];
	private Class[] columnTypes = new Class[] { Boolean.class, Object.class,
			Object.class, Object.class, Object.class, Object.class,
			Object.class,Object.class,Object.class,Object.class };

	public MyTableModel() {
		super();
	}

	public MyTableModel(String[] headName, Object[][] obj) {
		this();
		this.headName = headName;
		this.obj = obj;
	}

	public int getColumnCount() {
		return headName.length;
	}

	public int getRowCount() {
		return obj.length;
	}

	public Object getValueAt(int r, int c) {
		return obj[r][c];
	}

	public String getColumnName(int c) {
		return headName[c];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return columnTypes[columnIndex].getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		/**
		 * 判断如果传入的columnIndex=0，即第一列是，对于此行的第四列，如果值为Pending未知状态或Released发布状态，则不可编辑。
		 */
		if(columnIndex == 0){
			String releaseLevel = getValueAt(rowIndex, 3).toString().trim();
			if("Pending未知状态".equals(releaseLevel)||"Released发布状态".equals(releaseLevel)){
				return false;
			}
		}
		return true;
	}

	public void setValueAt(Object value, int row, int col) {
		obj[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
