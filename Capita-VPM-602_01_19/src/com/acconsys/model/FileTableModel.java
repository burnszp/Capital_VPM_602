package com.acconsys.model;

import javax.swing.table.AbstractTableModel;

public class FileTableModel extends AbstractTableModel {
	private String[] header;
	private Object[][] data;
	private Class[] columnTypes = new Class[] { String.class,
			Object.class, Object.class, Object.class};
	
	public FileTableModel(String[] fileTableHeader, Object[][] fileTableData) {
		this.header = fileTableHeader;
		this.data = fileTableData;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	
	public Object getValueAt(int r, int c) {
		return data[r][c];
	}

	public String getColumnName(int c) {
		return header[c];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return columnTypes[columnIndex].getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	@Override
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	

}
