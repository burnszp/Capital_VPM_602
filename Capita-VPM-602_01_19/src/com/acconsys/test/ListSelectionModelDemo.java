package com.acconsys.test;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ListSelectionModelDemo implements ListSelectionListener {
	String[] headings = { "Name", "Customer ID", "Order #", "Status" };

	Object[][] data = { { "A", new Integer(3), "0", new Date() },
			{ "B", new Integer(6), "4", new Date() },
			{ "C", new Integer(9), "9", new Date() },
			{ "D", new Integer(7), "1", new Date() },
			{ "E", new Integer(4), "1", new Date() },
			{ "F", new Integer(8), "2", new Date() },
			{ "G", new Integer(6), "1", new Date() } };

	JTable jtabOrders = new JTable(data, headings);
	TableModel tm;

	public ListSelectionModelDemo() {
		JFrame jfrm = new JFrame("JTable Event Demo");
		jfrm.setSize(400, 200);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 返回此表视口的首选大小。 preferred优先
		jtabOrders.setPreferredScrollableViewportSize(new Dimension(420, 62));

		// 此接口表示任何组件的当前选择状态，该组件显示一个具有稳定索引的值列表。
		ListSelectionModel rowSelMod = jtabOrders.getSelectionModel();// 定义一个选择对像
		ListSelectionModel colSelMod = jtabOrders.getColumnModel()
				.getSelectionModel();

		rowSelMod.addListSelectionListener(this);// 自动调用valueChanged方法
		colSelMod.addListSelectionListener(this);

		tm = jtabOrders.getModel();// 把tm与本表连接，初始化

		tm.addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent tme) {
				if (tme.getType() == TableModelEvent.UPDATE) {
					System.out.println("Cell " + tme.getFirstRow() + ", "
							+ tme.getColumn() + " changed."
							+ " The new value: "
							+ tm.getValueAt(tme.getFirstRow(), tme.getColumn()));
				}
			}
		});

		jfrm.add(new JScrollPane(jtabOrders));
		jfrm.setVisible(true);

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		String str = "选择了行(s):";
		int[] rows = jtabOrders.getSelectedRows();// 把它获得的行交给rows 行!
		for (int i = 0; i < rows.length; i++) {// 循环行交给str
			str += rows[i] + " ";
		}

		str += "选择了列(s)：";
		int[] cols = jtabOrders.getSelectedColumns();// 把获得的列交给cols数组中

		for (int i = 0; i < cols.length; i++) {// 循环列交给str 列!
			str += cols[i] + " ";
		}

		str += "选择的单元格：" + jtabOrders.getSelectedRow() + ","
				+ jtabOrders.getSelectedColumn();
		System.out.println(str);
	}

	public static void main(String[] args) {
		new ListSelectionModelDemo();
	}

}
