package com.acconsys.test;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class test extends JFrame implements ActionListener {
	static int i = 0;
	JTable table = new JTable();
	JScrollPane src;

	// DefaultTableModel model;
	public test() {
		// JTable table=new JTable();
		this.setLayout(null);
		this.gettable();
		src = new JScrollPane(table);
		src.setBounds(0, 0, 500, 200);
		JButton b1 = new JButton("变化");
		b1.addActionListener(this);
		b1.setBounds(0, 220, 60, 25);
		this.add(src);
		this.add(b1);
		this.setSize(new Dimension(500, 300));
		this.setVisible(true);
	}

	public static void main(String args[]) {
		new test();
	}

	public void gettable() {
		// System.out.println("@@@@@@@@@@@@@@"+i);
		Vector v2 = new Vector();
		Vector v3 = new Vector();

		Vector v5 = new Vector();
		Vector v6 = new Vector();
		// String ss[]={"100","200","300","400"};
		if (i == 0) {
			for (int j = 0; j < 3; j++) {
				Vector v1 = new Vector();
				v1.addElement((j + 1) * 10);
				v1.addElement((j + 2) * 100);
				v2.addElement(v1);
			}
			v3.addElement("A");
			v3.addElement("B");
			DefaultTableModel model = new DefaultTableModel(v2, v3);
			// System.out.println("###############"+model.toString());
			table.setModel(model);
			table.repaint();
			table.updateUI();
			// table=new JTable(v2,v3);
		}
		if (i == 1) {
			for (int k = 2; k < 4; k++) {
				Vector v4 = new Vector();
				v4.addElement((k + 5) * 10);
				v4.addElement((k + 6) * 100);
				v5.addElement(v4);
			}
			v6.addElement("C");
			v6.addElement("D");
			DefaultTableModel model = new DefaultTableModel(v5, v6);
			table.setModel(model);
			table.repaint();
			table.updateUI();
		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("变化")) {
			i = 1;
			this.gettable();
		}
	}
}