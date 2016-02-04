package com.acconsys.model;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressBarCellRenderer implements TableCellRenderer {
	private JProgressBar progressBar;

	public ProgressBarCellRenderer() {
		this.progressBar = new JProgressBar();
		this.progressBar.setToolTipText("生成文件");
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		return this.progressBar;
	}

}
