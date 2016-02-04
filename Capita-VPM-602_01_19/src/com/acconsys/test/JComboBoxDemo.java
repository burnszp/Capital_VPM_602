package com.acconsys.test;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JComboBoxDemo extends JFrame implements ItemListener {
	JComboBox jb;
	JPanel p = new JPanel();

	public JComboBoxDemo() {
		jb = new JComboBox();
		jb.addItem("1");
		jb.addItem("2");
		jb.addItem("3");
		jb.addItemListener(this);

		p.add(jb);
		this.getContentPane().add(p);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(360, 260);
		this.setVisible(true);
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String s = (String) jb.getSelectedItem();
			System.out.println(s);
		}
	}

	public static void main(String args[]) {
		new JComboBoxDemo();
	}
}
