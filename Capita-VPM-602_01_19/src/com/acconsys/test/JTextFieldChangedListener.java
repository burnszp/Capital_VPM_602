package com.acconsys.test;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JTextFieldChangedListener {


	private JTextField jtf;
	private JFrame f;
	private JPanel contentPanel;

	public JTextFieldChangedListener() {
		 f = new JFrame();
		contentPanel = new JPanel();
		f.setContentPane(contentPanel);
		jtf = new JTextField(50);
		contentPanel.add(jtf);
		jtf.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
               // System.out.print("1");
                 System.out.println(jtf.getText());
            }

            public void removeUpdate(DocumentEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
               //  System.out.print("2");
                  System.out.println(jtf.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                //JOptionPane.showMessageDialog(null, "请填写完整！");
                 System.out.println(jtf.getText());
            }
        });
		f.setSize(400, 200);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	public static void main(String[] args) {
		JTextFieldChangedListener test = new JTextFieldChangedListener();
	}
}
