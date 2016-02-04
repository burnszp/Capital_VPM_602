package com.acconsys.test;

import java.awt.*;

import javax.swing.*;

public class GridBagDemo extends JFrame {
	public static void main(String args[]) {
		GridBagDemo demo = new GridBagDemo();
	}
	private static GridBagLayout layout;

	public GridBagDemo() {
		init();
		this.setSize(600, 600);
		this.setVisible(true);
	}

	public void init() {
		j1 = new JButton("打开1");
		j2 = new JButton("保存2");
		j3 = new JButton("另存为3");
		j4 = new JPanel();
		j4.setBorder(BorderFactory.createTitledBorder("4"));
		String[] str = { "java笔记5", "C#笔记5", "HTML5笔记5" };
		j5 = new JComboBox(str);
		j6 = new JTextField("6");
		j7 = new JButton("清空7");
		j8 = new JList(str);
		j9 = new JTextArea("9");
		j9.setBackground(Color.PINK);// 为了看出效果，设置了颜色
		layout = new GridBagLayout();
		this.setLayout(layout);
		this.add(j1);// 把组件添加进jframe
		this.add(j2);
		this.add(j3);
		this.add(j4);
		this.add(j5);
		this.add(j6);
		this.add(j7);
		this.add(j8);
		this.add(j9);
		GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，
		// 是用来控制添加进的组件的显示位置
		s.fill = GridBagConstraints.BOTH;
		// 该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
		// NONE：不调整组件大小。
		// HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
		// VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
		// BOTH：使组件完全填满其显示区域。
//		addComponent(Component c,GridBagConstraints constraint,int weightx,int weighty,int gridwidth );
		addComponent(j1, s, 0, 0, 1);
		addComponent(j2, s, 0, 0, 1);
		addComponent(j3, s, 0, 0, 1);
		addComponent(j4, s, 0, 0, 0);
		addComponent(j5, s, 0, 0, 2);
		addComponent(j6, s, 1, 0, 4);
		addComponent(j7, s, 0, 0, 0);
		addComponent(j8, s, 0, 1, 2);
		addComponent(j9, s, 0, 1, 5);
//		s.gridwidth = 1;// 该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
//		s.weightx = 0;// 该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
//		s.weighty = 0;// 该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
//		layout.setConstraints(j1, s);// 设置组件
		
//		s.gridwidth = 1;
//		s.weightx = 0;
//		s.weighty = 0;
//		layout.setConstraints(j2, s);
		
//		s.gridwidth = 1;
//		s.weightx = 0;
//		s.weighty = 0;
//		layout.setConstraints(j3, s);
		
//		s.gridwidth = 0;// 该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
//		s.weightx = 0;// 不能为1，j4是占了4个格，并且可以横向拉伸，
//		// 但是如果为1，后面行的列的格也会跟着拉伸,导致j7所在的列也可以拉伸
//		// 所以应该是跟着j6进行拉伸
//		s.weighty = 0;
//		layout.setConstraints(j4, s);
		
//		s.gridwidth = 2;
//		s.weightx = 0;
//		s.weighty = 0;
//		layout.setConstraints(j5, s);
		
//		s.gridwidth = 4;
//		s.weightx = 1;
//		s.weighty = 0;
//		layout.setConstraints(j6, s);
		
//		s.gridwidth = 0;
//		s.weightx = 0;
//		s.weighty = 0;
//		layout.setConstraints(j7, s);
		
//		s.gridwidth = 2;
//		s.weightx = 0;
//		s.weighty = 1;
//		layout.setConstraints(j8, s);
//		;
//		s.gridwidth = 5;
//		s.weightx = 0;
//		s.weighty = 1;
//		layout.setConstraints(j9, s);
	}

	public static void addComponent(Component c,GridBagConstraints constraints,int weightx,int weighty,int gridwidth ){
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.gridwidth = gridwidth;
		layout.setConstraints(c, constraints);
	}
	JButton j1;
	JButton j2;
	JButton j3;
	JPanel j4;
	JComboBox j5;
	JTextField j6;
	JButton j7;
	JList j8;
	JTextArea j9;
}