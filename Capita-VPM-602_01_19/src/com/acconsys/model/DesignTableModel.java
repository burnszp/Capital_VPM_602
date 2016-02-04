package com.acconsys.model;

import javax.swing.table.DefaultTableModel;

public class DesignTableModel extends DefaultTableModel {


	public DesignTableModel(Object[][] data, Object[] headers) {
		super(data, headers);
	}
	
	/**
	 * 单元格是否可编辑
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
}
