package com.acconsys.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.mentor.chs.api.IXLogicDesign;

/**
 * 自定义swing表格
 * 
 * @author Administrator
 * 
 */
public class CVCheckInTablePanel extends JPanel implements TableModelListener,
		ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String designInfo;
	private String designReleasedLevel;
	private JTable table;
	private MyTableModel model;
	private DefaultTableCellRenderer renderer;
	private Object[][] data;
	private String[] headers;

	public CVCheckInTablePanel(List<IXLogicDesign> logicDesignList,
			String[] headers) {
		super(new GridLayout(1, 0));
		this.headers = headers;
		data = getDataByList(logicDesignList, headers);// 从list集合中获取data数据
		model = new MyTableModel(headers,data );
		table = new JTable(model);

		/**
		 * 设置checkButton
		 */
		table.getColumnModel().getColumn(0)
				.setCellEditor(table.getDefaultEditor(Boolean.class));
		table.getColumnModel().getColumn(0)
				.setCellRenderer(table.getDefaultRenderer(Boolean.class));

		/**
		 * 添加行监听器
		 */
		table.getSelectionModel().addListSelectionListener(new RowListener());
		// 设置默认选中一行，
		table.setRowSelectionAllowed(true);
		// table.setColumnSelectionAllowed(false);
		// table.setCellSelectionEnabled(false);
		// end

		// 设置默认只能选中一行
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// end
		// table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.setFillsViewportHeight(true);
		// start表格排序
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		table.setRowSorter(sorter);
		// end表格排序
		add(new JScrollPane(table));

		/**
		 * 表格根据行中某列值的特点，给整行加颜色
		 */
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
				if (table.getValueAt(row, 2) == null) {
					JOptionPane.showMessageDialog(this, "第三列为空");
				}
				String column2 = table.getValueAt(row, 2).toString();
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

		for (int i = 1; i < table.getColumnCount(); i++) {
			System.out.println(table.getColumnCount());
			table.getColumn(model.getColumnClass(i)).setCellRenderer(renderer);
			table.getColumn(i).setCellRenderer(renderer);
		}

	}

	private Object[][] getDataByList(List<IXLogicDesign> logicDesignList,
			String[] headers) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String longtime = logicList.get(i).getAttribute("ModifiedOn");
		// logicTableData[i][4] = df.format(new Date(new Long(longtime)));
		Object[][] logicData = new String[logicDesignList.size()][headers.length];
		for (int i = 0; i < logicDesignList.size(); i++) {
			IXLogicDesign logicDesign = logicDesignList.get(i);
			logicData[i][0] = null;
			logicData[i][1] = logicDesign.getAttribute("Name");
			logicData[i][2] = logicDesign.getAttribute("Revision");
			logicData[i][3] = logicDesign.getAttribute("ReleaseLevel");
			logicData[i][4] = logicDesign.getAttribute("CreatedBy");
			logicData[i][5] = df.format(new Date(new Long(logicDesign
					.getAttribute("CreatedOn"))));
			logicData[i][6] = logicDesign.getAttribute("ModifiedBy");
			logicData[i][7] = df.format(new Date(new Long(logicDesign
					.getAttribute("ModifiedOn"))));
			logicData[i][8] = logicDesign.getProperty("CheckInOrOutBy") == null ? "无"
					: logicDesign.getProperty("CheckInOrOutBy");
			logicData[i][8] = logicDesign.getProperty("CheckInOrOutOn") == null ? "无"
					: df.format(new Date(new Long(logicDesign
							.getAttribute("CheckInOrOutOn"))));
		}
		return logicData;
	}

	private void outputSelection() {
		int rowIndex = table.getSelectionModel().getLeadSelectionIndex();
		String designName = model.getValueAt(rowIndex, 1).toString();
		String designRevision = model.getValueAt(rowIndex, 2).toString();
		designReleasedLevel = model.getValueAt(rowIndex, 3).toString();
		designInfo = designName + "[" + designRevision + "]";
	}

	private class RowListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			// if (event.getValueIsAdjusting()) {
			// return;
			// }
			outputSelection();
		}
	}

	public String getDesignInfo() {
		return designInfo;
	}

	public String getDesignReleasedLevel() {
		return designReleasedLevel;
	}

	/**
	 * 监听表格数据变化
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		// int row = e.getFirstRow();//获得所选数据的行数【数据改变的行数】
		// String releaseLevel = (String) table.getValueAt(row, 3);
		// if
	}

	public JTable getTable() {
		return table;
	}

	public DefaultTableCellRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(DefaultTableCellRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		table.getSelectionModel();
	}

}
