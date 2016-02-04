package com.acconsys.test;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import com.mentor.chs.plugin.IXApplicationContext;

public class CheckButtonTableTest {

	private JTable table;
	private JScrollPane scrollPane_1;

	public static void main(String[] args) {
		CheckButtonTableTest buttonTableTest = new CheckButtonTableTest(null);
	}

	public CheckButtonTableTest(IXApplicationContext context) {
		JFrame f = new JFrame();
		scrollPane_1 = new JScrollPane();
		f.setSize(800, 600);
		
		String[] headNames = new String[] { "\u9009\u62E9",
				"\u6587\u4EF6\u540D", "\u5927\u5C0F\uFF08KB\uFF09",
				"\u6587\u4EF6\u65E5\u671F", "\u4F5C\u8005", "", "\u4E0B\u8F7D" };
		Object[][] tableDatas = new Object[][] {
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null }, };

		table = new JTable(new DocsTableModel(headNames, tableDatas));

		table.getColumnModel().getColumn(0)
				.setCellEditor(table.getDefaultEditor(Boolean.class));
		table.getColumnModel().getColumn(0)
				.setCellRenderer(table.getDefaultRenderer(Boolean.class));

		table.getColumnModel().getColumn(0).setPreferredWidth(38);
		table.getColumnModel().getColumn(1).setPreferredWidth(206);
		table.getColumnModel().getColumn(2).setPreferredWidth(53);
		table.getColumnModel().getColumn(3).setPreferredWidth(134);
		table.getColumnModel().getColumn(4).setPreferredWidth(58);

		// hide the 5th column, it contans the filepath
		// table.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);//
		// hide
		// table.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);//
		// hide
		table.getTableHeader().getColumnModel().getColumn(5)
				.setPreferredWidth(0);
		table.getTableHeader().getColumnModel().getColumn(5).setWidth(0);

		table.getColumnModel().getColumn(6)
				.setCellRenderer(new ButtonCellRenderer());
		table.getColumnModel().getColumn(6)
				.setCellEditor(new ButtonCellEditor(table));

		scrollPane_1.setViewportView(table);
		f.setContentPane(scrollPane_1);
		f.pack();
		f.setVisible(true);
	}

}

class DocsTableModel extends AbstractTableModel {
	private String headName[];
	private Object obj[][];
	private Class[] columnTypes = new Class[] { Boolean.class, Object.class,
			Object.class, Object.class, Object.class, Object.class,
			Object.class };

	public DocsTableModel() {
		super();
	}

	public DocsTableModel(String[] headName, Object[][] obj) {
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
		return true;
	}

	public void setValueAt(Object value, int row, int col) {
		obj[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}

class ButtonCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -6546334664166791132L;
	private JButton button;
	private JTable table;

	public ButtonCellEditor(JTable table) {
		super(new JTextField());
		this.setClickCountToStart(1);
		this.initButton();
		this.table = table;
	}

	private void initButton() {
		this.button = new JButton();
		this.button.setSize(50, 15);
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ButtonCellEditor.this.fireEditingCanceled();
				System.out.println("Selected Column:"
						+ table.getSelectedColumn()
						+ ",row:"
						+ table.getSelectedRow()
						+ ",and filePath:"
						+ table.getModel()
								.getValueAt(table.getSelectedRow(), 5));
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.button.setText("下载");
		return this.button;
	}

	@Override
	public Object getCellEditorValue() {
		return this.button.getText();
	}
}

class ButtonCellRenderer implements TableCellRenderer {
	private JButton button;

	public ButtonCellRenderer() {
		this.button = new JButton();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		this.button.setText("下载");
		return this.button;
	}

}
