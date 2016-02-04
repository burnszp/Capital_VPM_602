package com.acconsys.test;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class JTableTest {
	
	public static void main(String[] args) {
		JTableTest jttest = new JTableTest();
		
	}

	public JTableTest() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(600, 400));
		f.setLocationRelativeTo(null);
		TableModel dataModel = new AbstractTableModel() {
			public int getColumnCount() {
				return 10;
			}

			public int getRowCount() {
				return 10;
			}

			public Object getValueAt(int row, int col) {
				return new Integer(row * col);
			}
		};
		JTable table = new JTable(dataModel);
		JScrollPane scrollpane = new JScrollPane(table);
		f.setContentPane(scrollpane);
		f.pack();
		f.setVisible(true);
	}
}
