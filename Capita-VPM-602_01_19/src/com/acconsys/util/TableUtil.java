package com.acconsys.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class TableUtil {

	public static TableCellRenderer getTableCellRenderer() {
		/**
		 * 表格根据行中某列值的特点，给整行加颜色
		 */
		TableCellRenderer renderer;
		renderer = new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (row % 2 == 0)
					setBackground(Color.white); // 设置奇数行底色
				else if (row % 2 == 1)
					setBackground(new Color(206, 231, 255)); // 设置偶数行底色
				if (table == null) {
					JOptionPane.showMessageDialog(this, "table为空");
				}
				System.out.println("table:  " + table.toString());
				// if (table.getValueAt(row, 2) == null) {
				// JOptionPane.showMessageDialog(this, "第三列为空");
				// }
				String column2 = table.getValueAt(row, 3).toString();
				if (column2.equalsIgnoreCase("Pending未知状态")
						|| column2.equalsIgnoreCase("Released发布状态")) {
					setForeground(Color.RED);
				} else {
					setForeground(Color.BLACK);
				}
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}
		};
		return renderer;

	}
	
}
