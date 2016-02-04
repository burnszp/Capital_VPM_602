package com.acconsys.model;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ProgressBarCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -6546334664166791132L;
	private JProgressBar progressBar;

	public ProgressBarCellEditor() {
		super(new JTextField());
		this.setClickCountToStart(1);
		this.initProgressBar();
	}

	private void initProgressBar() {
		this.progressBar = new JProgressBar();
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(100);
//		this.progressBar.setStringPainted(true);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		return this.progressBar;
	}
	@Override
	public Component getComponent() {
		return this.progressBar;
	}

}
