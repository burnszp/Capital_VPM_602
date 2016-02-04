package com.acconsys.test;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class GridBagLayoutTest1 {

	private static JFrame f ;
	private static JPanel contentPane;
	private static GridBagLayout layout;
	public static void main(String[] args) {
		f = new JFrame();
		layout = new GridBagLayout();
		contentPane = new JPanel(layout);
		f.setContentPane(contentPane);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		JLabel xmlL = new JLabel("xml:");
		JLabel pdfL = new JLabel("PDF:");
		JLabel excelL = new JLabel("Excel:");
		JProgressBar xmlPB = new JProgressBar();
		JProgressBar pdfPB = new JProgressBar();
		JProgressBar excelPB = new JProgressBar();
		JButton xmlB = new JButton("生成文件xml");
		JButton pdfB = new JButton("生成文件pdf");
		JButton excelB = new JButton("生成文件excel");
		JButton xmlB1 = new JButton("查看文件xml");
		JButton pdfB1 = new JButton("查看文件pdf");
		JButton excelB1 = new JButton("查看文件excel");
		
		contentPane.add(xmlL);
		contentPane.add(xmlPB);
		contentPane.add(xmlB);
		contentPane.add(xmlB1);
		contentPane.add(pdfL);
		contentPane.add(pdfPB);
		contentPane.add(pdfB);
		contentPane.add(pdfB1);
		contentPane.add(excelL);
		contentPane.add(excelPB);
		contentPane.add(excelB);
		contentPane.add(excelB1);
		
		addComponent(xmlL, constraints, 0, 0, 1);
		addComponent(xmlPB, constraints, 1, 0, 4);
		addComponent(xmlB, constraints, 0, 0, 1);
		addComponent(xmlB1, constraints, 0, 0, 0);
		addComponent(pdfL, constraints, 0, 0, 1);
		addComponent(pdfPB, constraints, 1, 0, 4);
		addComponent(pdfB, constraints, 0, 0, 1);
		addComponent(pdfB1, constraints, 0, 0, 0);
		addComponent(excelL, constraints, 0, 0, 1);
		addComponent(excelPB, constraints, 1, 0, 4);
		addComponent(excelB, constraints, 0, 0, 1);
		addComponent(excelB1, constraints, 0, 0, 0);
//		addComponent(xmlL, constraints, 0, 0, 1, 1);
//		addComponent(pdfL, constraints, 0, 1, 1, 1);
//		addComponent(excelL, constraints, 0, 2, 1, 1);
//		addComponent(xmlPB, constraints, 1, 0, 1, 1);
//		addComponent(pdfPB, constraints, 1, 1, 1, 1);
//		addComponent(excelPB, constraints, 1, 2, 1, 1);
//		addComponent(xmlB, constraints, 2, 0, 1, 1);
//		addComponent(pdfB, constraints, 2, 1, 1, 1);
//		addComponent(excelB, constraints, 2, 2, 1, 1);
//		addComponent(xmlB1, constraints, 3, 0, 0, 1);
//		addComponent(pdfB1, constraints, 3, 1, 0, 1);
//		addComponent(excelB1, constraints, 3, 2, 0, 1);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setLocationRelativeTo(null);
		f.setSize(800, 600);
		f.setVisible(true);
		
		
	}
	
//	public static void addComponent(Component c,GridBagConstraints constraints,int x,int y,int width,int height){
//		constraints.gridx = x;
//		constraints.gridy = y;
//		constraints.gridwidth = width;
//		constraints.gridheight = height;
//		contentPane.add(c, constraints);
//	}
	
	public static void addComponent(Component c,GridBagConstraints constraints,int weightx,int weighty,int gridwidth ){
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.gridwidth = gridwidth;
		layout.setConstraints(c, constraints);
	}
}
