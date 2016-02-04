package com.acconsys.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class JDialogAndJFrameTest {
	public static void main(String[] args) {
		JDialog frame = new JDialog();
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		JButton b = new JButton("打开弹出框");
		frame.add(b);
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame d = new JFrame();
//				d.setModal(true);
				d.setSize(200, 100);
				d.setVisible(true);
//				d.setLocationRelativeTo(frame);
				
			}
		});
	}
}