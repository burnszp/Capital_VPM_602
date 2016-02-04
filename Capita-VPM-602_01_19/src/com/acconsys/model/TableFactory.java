package com.acconsys.model;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.acconsys.util.TableUtil;

public class TableFactory {

	// private MyTableModel logicTableModel;
	// private JTable logicTable;

	private static TableFactory tableFactory;

	public static TableFactory getTableFactory() {
		if (tableFactory == null) {
			tableFactory = new TableFactory();
		}
		return tableFactory;
	}

	public static JTable getLogicTable(String[] headName, Object[][] obj) {
		MyTableModel logicTableModel = new MyTableModel(headName, obj);
		JTable logicTable = new JTable(logicTableModel);

		/**
		 * 设置默认编辑器
		 */
		logicTable.getColumnModel().getColumn(0)
				.setCellEditor(logicTable.getDefaultEditor(Boolean.class));
		logicTable.getColumnModel().getColumn(0)
				.setCellRenderer(logicTable.getDefaultRenderer(Boolean.class));
		/**
		 * 设置初始化列宽
		 */
		logicTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		logicTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(7).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(8).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(9).setPreferredWidth(150);

		// start表格排序
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(
				logicTableModel);
		logicTable.setRowSorter(sorter);
		// end表格排序

		// 设置行高
		logicTable.setRowHeight(15);

		/**
		 * 表格根据行中某列值的特点，给整行加颜色
		 */
		TableCellRenderer renderer = TableUtil.getTableCellRenderer();
		for (int i = 1; i < logicTable.getColumnCount(); i++) {
			logicTable.getColumn(logicTableModel.getColumnName(i))
					.setCellRenderer(renderer);
		}

		return logicTable;

	}

}
