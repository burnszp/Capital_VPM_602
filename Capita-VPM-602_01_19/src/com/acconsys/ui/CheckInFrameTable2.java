package com.acconsys.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.acconsys.model.MyTableModel;
import com.acconsys.util.Constants;
import com.acconsys.util.DesignUtil;
import com.acconsys.util.GenerateFilesUtil;
import com.acconsys.util.PropertyUtil;
import com.acconsys.util.TableUtil;
import com.acconsys.util.XmlFileFilter;
import com.mentor.chs.api.IXDiagram;
import com.mentor.chs.api.IXHarnessDesign;
import com.mentor.chs.api.IXIntegratorDesign;
import com.mentor.chs.api.IXLogicDesign;
import com.mentor.chs.api.IXProject;
import com.mentor.chs.api.IXTopologyDesign;
import com.mentor.chs.plugin.IXApplicationContext;
import com.mentor.chs.plugin.IXOutputWindow;

public class CheckInFrameTable2 extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 600;

	public IXApplicationContext context;
	public static IXOutputWindow out;
	public static List<IXLogicDesign> logicDesignList;
	public static List<IXHarnessDesign> harnessDesignList;
	public static List<IXIntegratorDesign> integratorDesignList;
	public static List<IXTopologyDesign> topologyDesignList;
	public static String[] headers = { "是否选中", "设计名称", "设计版本", "状态", "创建人",
			"创建时间", "最后修改人", "最后修改时间", "最后检入/出人", "最后检入/出时间", };
	public JTabbedPane designTabbedPane;
	public Object[][] logicTableData;
	public JTable logicTable;
	public JScrollPane logicScrollPane;
	public JPanel logicPane;
	public JPanel logicBottomPane;
	public JPanel logicTopPane;
	public MyTableModel logicTableModel;
	public JTable fileTable;
	public JComboBox filterCB;
	private Set<IXLogicDesign> logicDesignSet;
	private Set<IXHarnessDesign> harnessDesignSet;
	private Set<IXIntegratorDesign> integratorDesignSet;
	private Set<IXTopologyDesign> topologyDesignSet;
	private JScrollPane filePanel;
	private String[] fileTableHeader = { "文件类型", "进度", "生成文件", "查看详情" };
	private Map<String, IXLogicDesign> logicDeisgnMap;
	private Map<String, IXLogicDesign> selectedLogicDesignMap;
	public List<File> logicXmlList;
	public Map<IXLogicDesign, List<File>> logicPdfMap;
	public List<File> logicExcelList;
	private IXProject currentProject;
	private GridBagLayout fileGridBagLayout;
	private JProgressBar xmlPB;
	private JProgressBar pdfPB;
	private JProgressBar excelPB;
	private JButton xmlGenB;
	private JButton pdfGenB;
	private JButton excelGenB;
	private JButton xmlInfoB;
	private JButton pdfInfoB;
	private JButton excelInfoB;
	private JFileChooser xmlFileChooser;
	private JTextField xmlPathTF;
	private String xmlPath;
	private JButton uploadOrCheckoutB;
	private JButton cancelB;

	public CheckInFrameTable2(IXApplicationContext context) {
		// super(context.getParentFrame(),"123456", true);
		setModal(true);
		this.context = context;
		this.currentProject = context.getCurrentProject();
		CheckInFrameTable2.out = context.getOutputWindow();
		out.println("开始：context" + context.toString());
		initData();
		initUI();
		// addWindowListener(new WindowAdapter() {
		// @Override
		// public void windowClosing(WindowEvent e) {
		// CheckInFrameTable.this.dispose();
		// super.windowClosing(e);
		// }
		// });
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		logicDesignSet = currentProject.getLogicDesigns();
		harnessDesignSet = currentProject.getHarnessDesigns();
		integratorDesignSet = currentProject.getIntegratorDesigns();
		topologyDesignSet = currentProject.getTopologyDesigns();
		logicDesignList = new ArrayList<IXLogicDesign>(logicDesignSet);
		harnessDesignList = new ArrayList<IXHarnessDesign>(harnessDesignSet);
		integratorDesignList = new ArrayList<IXIntegratorDesign>(
				integratorDesignSet);
		topologyDesignList = new ArrayList<IXTopologyDesign>(topologyDesignSet);
		logicDeisgnMap = DesignUtil.getLogicMap(logicDesignList);
	}

	/**
	 * 初始化界面
	 */
	public void initUI() {
		setTitle("设计检入");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(context.getParentFrame());
		// setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//
		// 调用任意已注册WindowListener的对象后自动隐藏并释放该窗体。
		// setModalityType(JDialog.DEFAULT_MODALITY_TYPE);//setModal(true);
		/**
		 * 创建主面板
		 */
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		setContentPane(contentPanel);

		/**
		 * 创建顶部区域
		 */
		JSplitPane centerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		centerSplitPane.resetToPreferredSizes();
		centerSplitPane.setResizeWeight(0.7);
		/*
		 * 设置JSplitPane是否可以展开或收起(如同文件总管一般)，设为true表示打开此功能。
		 */
		centerSplitPane.setOneTouchExpandable(true);
		centerSplitPane.setDividerSize(10);// 设置分隔线宽度的大小，以pixel为计算单位。
		designTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		designTabbedPane.setPreferredSize(new Dimension(1000, 350));
		designTabbedPane.setBorder(BorderFactory.createTitledBorder("当前项目"
				+ currentProject.getAttribute("Name") + "的相关设计信息如下："));
		contentPanel.add(centerSplitPane, BorderLayout.CENTER);

		/**
		 * logic面板
		 */
		logicPane = new JPanel(new BorderLayout());
		logicTableData = DesignUtil.getDataByList(logicDesignList,
				headers.length);
		logicTableModel = new MyTableModel(headers, logicTableData);
		logicTable = new JTable(logicTableModel);
		renderLogicTable();
		// 获得logic的顶部查询面板
		logicTopPane = getLogicTopPane();
		// 表格面板
		logicScrollPane = new JScrollPane(logicTable);
		logicBottomPane = getLogicBottomPane();
		logicPane.add(logicTopPane, BorderLayout.NORTH);
		logicPane.add(logicScrollPane, BorderLayout.CENTER);
		logicPane.add(logicBottomPane, BorderLayout.SOUTH);
		designTabbedPane.addTab("Logic", null, logicPane, "Logic设计列表");
		logicScrollPane.setBorder(BorderFactory.createTitledBorder("当前项目"
				+ currentProject.getAttribute("Name") + "的Logic设计信息如下："));

		/**
		 * harness面板
		 */
		JPanel harnessTab = new JPanel();
		designTabbedPane.addTab("Harness", null, harnessTab, "Harness设计列表");
		harnessTab.setBorder(BorderFactory.createTitledBorder("当前项目"
				+ currentProject.getAttribute("Name") + "的Harness设计信息如下："));
		/**
		 * integrator/topology面板
		 */
		JPanel integratorAndTopologyTab = new JPanel();
		designTabbedPane.addTab("Integrator/Topology", null,
				integratorAndTopologyTab, "Integrator和Topology设计列表");
		integratorAndTopologyTab.setBorder(BorderFactory
				.createTitledBorder("当前项目"
						+ currentProject.getAttribute("Name")
						+ "的Integrator和Topology设计信息如下："));
		// ---------------------------------------------------------------------------------

		/**
		 * 文件区域
		 */
		filePanel = new JScrollPane(getFilePane());
		/**
		 * 设置设计表格和文件表格到页面中间位置的上下部分。
		 */
		centerSplitPane.setTopComponent(designTabbedPane);
		centerSplitPane.setBottomComponent(filePanel);

		/**
		 * 创建底部区域
		 */
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(1000, 50));
		bottomPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(bottomPanel, BorderLayout.SOUTH);

		/**
		 * 按钮区域
		 */
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createTitledBorder("功能按钮区域"));

		// 放置生成文件的存放位置（这个文件是xml文件，保存的是要上传到vpm系统的信息结构。
		JPanel buttonTopPanel = new JPanel(new BorderLayout());
		JLabel xmlPathL = new JLabel("生成文件的存放位置:");
		xmlPathTF = new JTextField(50);
		xmlPathTF.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				xmlPath = xmlPathTF.getText();
			}

			public void removeUpdate(DocumentEvent e) {
				xmlPath = xmlPathTF.getText();
			}

			public void changedUpdate(DocumentEvent e) {
				xmlPath = xmlPathTF.getText();
			}
		});
		xmlPath = (PropertyUtil.getPropertyUtil().getValue(Constants.FILE_PATH)
				+ "/" + currentProject.getAttribute("Name") + "/"
				+ currentProject.getAttribute("Name") + ".xml").replace("/", "\\");
		xmlPathTF.setText(xmlPath);
		JButton xmlPathB = new JButton("路径...");
		xmlPathB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 以c:\\为打开文件对话框的默认路径
				xmlFileChooser = new JFileChooser(PropertyUtil
						.getPropertyUtil().getValue(Constants.FILE_PATH)
						+ "/"
						+ currentProject.getAttribute("Name"));
				// 利用addChoosableFileFilter()方法加入欲过滤的文件类型
				// 使用addChoosableFileFilter()可以加入多种文件类型
				// 若只需要过滤出一种文件类型，可使用setFileFilter()方法
				// xmlFileChooser.addChoosableFileFilter(new
				// XmlFileFilter("xml"));
				xmlFileChooser.setFileFilter(new XmlFileFilter("xml"));
				xmlFileChooser.setSelectedFile(new File(xmlPath));
				int result = xmlFileChooser
						.showSaveDialog(CheckInFrameTable2.this);
				// 如果单击"确定"按钮的话，则可以打开现有的文件
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = xmlFileChooser.getSelectedFile();
					String fileName = file.getName();
					out.println("file.getAbsolutePath():"
							+ file.getAbsolutePath());
					out.println("fileName:" + fileName);
					int index = fileName.lastIndexOf(".");
					if (index > 0 && index < fileName.length() - 1) {
						String extension = fileName.substring(index + 1)
								.toLowerCase();// 表示文件名称不为".xxx"或"xxx."类型
						out.println("extension:" + extension);
						if (extension.equals("xml")) {
							xmlPathTF.setText(file.getAbsolutePath());
						}
					} else {
						xmlPathTF.setText(file.getAbsolutePath() + ".xml");
					}
				}
			}
		});
		buttonTopPanel.add(xmlPathL, BorderLayout.WEST);
		buttonTopPanel.add(xmlPathTF, BorderLayout.CENTER);
		buttonTopPanel.add(xmlPathB, BorderLayout.EAST);

		JPanel buttonCenterPanel = new JPanel();
		uploadOrCheckoutB = new JButton("上传/检入");
		uploadOrCheckoutB.setActionCommand("checkIn");
		uploadOrCheckoutB.addActionListener(this);
		cancelB = new JButton("取消");
		cancelB.setActionCommand("close");
		cancelB.addActionListener(this);
		buttonCenterPanel.add(uploadOrCheckoutB);
		buttonCenterPanel.add(cancelB);

		bottomPanel.add(buttonTopPanel, BorderLayout.NORTH);
		bottomPanel.add(buttonCenterPanel, BorderLayout.CENTER);

	}

	/**
	 * 生成文件区域的内容
	 * 
	 * @return
	 */
	private JPanel getFilePane() {
		fileGridBagLayout = new GridBagLayout();
		JPanel filePane = new JPanel();
		filePane.setBorder(BorderFactory.createTitledBorder("文件区域"));
		filePane.setPreferredSize(new Dimension(800, 100));
		filePane.setLayout(fileGridBagLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridheight = 4;
		JLabel xmlL = new JLabel("设计源文件xml:");
		JLabel pdfL = new JLabel("设计图纸PDF:");
		JLabel excelL = new JLabel("明细表Excel:");
		xmlPB = new JProgressBar();
		pdfPB = new JProgressBar();
		excelPB = new JProgressBar();
		xmlGenB = new JButton("生成文件xml");
		xmlGenB.addActionListener(this);
		pdfGenB = new JButton("生成文件pdf");
		pdfGenB.addActionListener(this);
		excelGenB = new JButton("生成文件excel");
		excelGenB.addActionListener(this);
		xmlInfoB = new JButton("查看文件xml");
		xmlInfoB.addActionListener(this);
		pdfInfoB = new JButton("查看文件pdf");
		pdfInfoB.addActionListener(this);
		excelInfoB = new JButton("查看文件excel");
		excelInfoB.addActionListener(this);

		filePane.add(xmlL);
		filePane.add(xmlPB);
		filePane.add(xmlGenB);
		filePane.add(xmlInfoB);
		filePane.add(pdfL);
		filePane.add(pdfPB);
		filePane.add(pdfGenB);
		filePane.add(pdfInfoB);
		filePane.add(excelL);
		filePane.add(excelPB);
		filePane.add(excelGenB);
		filePane.add(excelInfoB);

		addComponent(xmlL, constraints, 0, 0, 1);
		addComponent(xmlPB, constraints, 1, 0, 4);
		addComponent(xmlGenB, constraints, 0, 0, 1);
		addComponent(xmlInfoB, constraints, 0, 0, 0);
		addComponent(pdfL, constraints, 0, 0, 1);
		addComponent(pdfPB, constraints, 1, 0, 4);
		addComponent(pdfGenB, constraints, 0, 0, 1);
		addComponent(pdfInfoB, constraints, 0, 0, 0);
		addComponent(excelL, constraints, 0, 0, 1);
		addComponent(excelPB, constraints, 1, 0, 4);
		addComponent(excelGenB, constraints, 0, 0, 1);
		addComponent(excelInfoB, constraints, 0, 0, 0);

		return filePane;
	}

	public void addComponent(Component c, GridBagConstraints constraints,
			int weightx, int weighty, int gridwidth) {
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.gridwidth = gridwidth;
		fileGridBagLayout.setConstraints(c, constraints);
	}

	/**
	 * 对表格进行渲染
	 */
	private void renderLogicTable() {
		/**
		 * 设置默认编辑器
		 */
		logicTable.getColumnModel().getColumn(0)
				.setCellEditor(logicTable.getDefaultEditor(Boolean.class));
		logicTable.getColumnModel().getColumn(0)
				.setCellRenderer(logicTable.getDefaultRenderer(Boolean.class));
		/**
		 * 设置初始化列宽
		 */
		logicTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		logicTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(7).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(8).setPreferredWidth(100);
		logicTable.getColumnModel().getColumn(9).setPreferredWidth(150);

		// start表格排序
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(
				logicTableModel);
		logicTable.setRowSorter(sorter);
		// end表格排序

		// 设置行高
		logicTable.setRowHeight(20);

		/**
		 * 表格根据行中某列值的特点，给整行加颜色
		 */
		TableCellRenderer renderer = TableUtil.getTableCellRenderer();
		for (int i = 1; i < logicTable.getColumnCount(); i++) {
			logicTable.getColumn(logicTableModel.getColumnName(i))
					.setCellRenderer(renderer);
		}
	}

	/**
	 * 包含Logic的查询功能。
	 * 
	 * @return
	 */
	public JPanel getLogicTopPane() {
		String[] filterStrArr = { "显示所有设计", "只显示可检入的设计", "只显示不可检入的设计",
		// "只显示ReleaseLevel为Draft的设计", "只显示ReleaseLevel为deprecated的设计",
		// "只显示ReleaseLevel为Pending的设计", "只显示ReleaseLevel为Release的设计"
		};
		JPanel logicTopPanel = new JPanel(new FlowLayout());
		logicTopPanel.setBorder(BorderFactory.createTitledBorder("模糊查询区域"));
		JLabel label = new JLabel("输入设计名称");
		JTextField textField = new JTextField(50);
		JButton logicSearchB = new JButton("查询");
		JLabel filterL = new JLabel("过滤条件:");
		filterCB = new JComboBox(filterStrArr);
		filterCB.setSelectedItem(filterStrArr[0]);
		filterCB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String s = (String) filterCB.getSelectedItem();
					out.println("s:" + s);
					Object[][] allowCheckInLogicDesignTableData;
					if ("只显示可检入的设计".equals(s)) {
						List<Integer> allowCheckInList = new ArrayList<Integer>();
						for (int i = 0; i < logicDesignList.size(); i++) {
							if ("Draft".equals(logicTableData[i][3])) {
								allowCheckInList.add(i);
							}
						}
						allowCheckInLogicDesignTableData = new Object[allowCheckInList
								.size()][headers.length];
						for (int i = 0; i < allowCheckInList.size(); i++) {
							for (int j = 0; j < headers.length; j++) {
								allowCheckInLogicDesignTableData[i][j] = logicTableData[allowCheckInList
										.get(i)][j];
							}
						}

					} else if ("显示所有设计".equals(s)) {
						allowCheckInLogicDesignTableData = logicTableData;
					} else {
						List<Integer> allowCheckInList = new ArrayList<Integer>();
						for (int i = 0; i < logicDesignList.size(); i++) {
							if (!"Draft".equals(logicTableData[i][3])) {
								allowCheckInList.add(i);
							}
						}
						allowCheckInLogicDesignTableData = new Object[allowCheckInList
								.size()][headers.length];
						for (int i = 0; i < allowCheckInList.size(); i++) {
							for (int j = 0; j < headers.length; j++) {
								allowCheckInLogicDesignTableData[i][j] = logicTableData[allowCheckInList
										.get(i)][j];
							}
						}
					}
					logicTableModel = new MyTableModel(headers,
							allowCheckInLogicDesignTableData);
					logicTable.setModel(logicTableModel);
					renderLogicTable();
					logicTable.repaint();
					// logicTable.updateUI();
					// SwingUtilities.updateComponentTreeUI(logicTable);
				}

			}
		});
		logicTopPanel.add(label);
		logicTopPanel.add(textField);
		logicTopPanel.add(logicSearchB);
		logicTopPanel.add(filterL);
		logicTopPanel.add(filterCB);
		return logicTopPanel;
	}

	/**
	 * 获得logic底部的Jpanel，这个Jpanel，包含内容如下： （1）对应tab页的总设计数 （2)每页显示的设计数量 （3）当前页
	 * （4）总页数 （5）上一页按钮 （6）下一页按钮
	 * 
	 * @return
	 */
	private JPanel getLogicBottomPane() {
		JPanel logicBottomPanel = new JPanel();
		logicBottomPanel.setBorder(BorderFactory.createTitledBorder("分页功能区域"));
		Integer[] logicPerPageCountArr = { 5, 10, 15, 20, 50, 100 };
		int logicDesignCount = logicDesignList.size();
		int totalPage = logicDesignCount / 10 + logicDesignCount % 10 == 0 ? 0
				: 1;
		JLabel logicDesignCountL = new JLabel("【Logic设计总数：" + logicDesignCount
				+ "】");
		JLabel logicDesignPerPageCountL = new JLabel("logic设计每页显示数量:");
		JComboBox logicDesignPerPageCountCB = new JComboBox(
				logicPerPageCountArr);
		logicDesignPerPageCountCB.setSelectedIndex(1);
		JLabel currentPageAndTotalPage = new JLabel("【当前页:" + 1 + "|" + "总页数:"
				+ totalPage + "】");
		JButton firstPageB = new JButton("<<首页");
		JButton prePageB = new JButton("<上一页");
		JButton nextPageB = new JButton("下一页>");
		JButton lastPageB = new JButton("尾页>>");
		logicBottomPanel.add(logicDesignCountL);
		logicBottomPanel.add(logicDesignPerPageCountL);
		logicBottomPanel.add(logicDesignPerPageCountCB);
		logicBottomPanel.add(currentPageAndTotalPage);
		logicBottomPanel.add(firstPageB);
		logicBottomPanel.add(prePageB);
		logicBottomPanel.add(nextPageB);
		logicBottomPanel.add(lastPageB);
		return logicBottomPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * 如果选中的按钮是生成xml源文件的按钮
		 */
		if (e.getSource() == xmlGenB) {
			selectedLogicDesignMap = new HashMap<String, IXLogicDesign>();
			// 根据选中的设计生成设计文件。
			int logicTableRow = logicTable.getRowCount();
			for (int i = 0; i < logicTableRow; i++) {
				Boolean isCheckOrNot = (Boolean) logicTable.getValueAt(i, 0);
				out.println("行:" + i + "列:" + 0 + "的值:" + isCheckOrNot);
				if (isCheckOrNot) {
					String designName = (String) logicTable.getValueAt(i, 1);
					String releaseLevel = (String) logicTable.getValueAt(i, 2);
					String key = designName + releaseLevel;
					selectedLogicDesignMap.put(key, logicDeisgnMap.get(key));
				}
			}
			StringBuffer b = new StringBuffer("选中的设计有如下：\n");
			for (String designNameAndReleaseLevel : selectedLogicDesignMap
					.keySet()) {
				b.append(designNameAndReleaseLevel + "\n");
			}
			JOptionPane
					.showMessageDialog(CheckInFrameTable2.this, b.toString());
			// 开始生成xml源文件。
			GeneralXmlTask generalXmlTask = new GeneralXmlTask();
			Thread generalXmlTaskThread = new Thread(generalXmlTask);
			generalXmlTaskThread.start();
		}
		/**
		 * 查看生成的设计源文件的信息。
		 */
		if (e.getSource() == xmlInfoB) {
			if (logicXmlList != null && !logicXmlList.isEmpty()) {
				/**
				 * 弹出查看logic源文件窗口。
				 */
				out.println("logicXmlList数据不为空!" + logicXmlList.size());
				ShowLogicXmlInfoDialog slxiDialog = new ShowLogicXmlInfoDialog(
						logicXmlList);
				slxiDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(CheckInFrameTable2.this,
						"logicXmlList为空");
			}
		}
		/**
		 * 如果选中的按钮是生成设计图纸PDF的按钮
		 */
		if (e.getSource() == pdfGenB) {
			selectedLogicDesignMap = new HashMap<String, IXLogicDesign>();
			// 根据选中的设计生成设计文件。
			int logicTableRow = logicTable.getRowCount();
			for (int i = 0; i < logicTableRow; i++) {
				Boolean isCheckOrNot = (Boolean) logicTable.getValueAt(i, 0);
				out.println("行:" + i + "列:" + 0 + "的值:" + isCheckOrNot);
				if (isCheckOrNot) {
					String designName = (String) logicTable.getValueAt(i, 1);
					String releaseLevel = (String) logicTable.getValueAt(i, 2);
					String key = designName + releaseLevel;
					selectedLogicDesignMap.put(key, logicDeisgnMap.get(key));
				}
			}
			StringBuffer b = new StringBuffer("选中的设计有如下：\n");
			for (String designNameAndReleaseLevel : selectedLogicDesignMap
					.keySet()) {
				b.append(designNameAndReleaseLevel + "\n");
			}
			JOptionPane
					.showMessageDialog(CheckInFrameTable2.this, b.toString());
			// 开始生成设计图纸（pdf)。
			GeneralPdfTask generalPdfTask = new GeneralPdfTask();
			Thread generalPdfTaskThread = new Thread(generalPdfTask);
			generalPdfTaskThread.start();
		}
		/**
		 * 查看生成的设计图纸PDF的信息。
		 */
		if (e.getSource() == pdfInfoB) {
			if (logicPdfMap != null && !logicPdfMap.isEmpty()) {
				/**
				 * 弹出查看logic源文件窗口。
				 */
				out.println("logicXmlList数据不为空!" + logicPdfMap.size());
				ShowLogicPdfInfoDialog slxiDialog = new ShowLogicPdfInfoDialog(
						logicPdfMap);
				slxiDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(CheckInFrameTable2.this,
						"logicXmlList为空");
			}
		}
		/**
		 * 如果选中的按钮是生成设计明细表Excel的按钮
		 */
		if (e.getSource() == excelGenB) {
			selectedLogicDesignMap = new HashMap<String, IXLogicDesign>();
			// 根据选中的设计生成设计文件。
			int logicTableRow = logicTable.getRowCount();
			for (int i = 0; i < logicTableRow; i++) {
				Boolean isCheckOrNot = (Boolean) logicTable.getValueAt(i, 0);
				out.println("行:" + i + "列:" + 0 + "的值:" + isCheckOrNot);
				if (isCheckOrNot) {
					String designName = (String) logicTable.getValueAt(i, 1);
					String releaseLevel = (String) logicTable.getValueAt(i, 2);
					String key = designName + releaseLevel;
					selectedLogicDesignMap.put(key, logicDeisgnMap.get(key));
				}
			}
			StringBuffer b = new StringBuffer("选中的设计有如下：\n");
			for (String designNameAndReleaseLevel : selectedLogicDesignMap
					.keySet()) {
				b.append(designNameAndReleaseLevel + "\n");
			}
			JOptionPane
					.showMessageDialog(CheckInFrameTable2.this, b.toString());
			// 开始生成设计图纸（pdf)。
			GeneralExcelTask generalExcelTask = new GeneralExcelTask();
			Thread generalExcelTaskThread = new Thread(generalExcelTask);
			generalExcelTaskThread.start();
		}
		/**
		 * 查看生成的设计图纸PDF的信息。
		 */
		if (e.getSource() == excelInfoB) {
			if (logicExcelList != null && !logicExcelList.isEmpty()) {
				/**
				 * 弹出查看logic源文件窗口。
				 */
				out.println("logicXmlList数据不为空!" + logicExcelList.size());
				ShowLogicExcelInfoDialog slxiDialog = new ShowLogicExcelInfoDialog(
						logicExcelList);
				slxiDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(CheckInFrameTable2.this,
						"logicXmlList为空");
			}
		}
		
		/**
		 * 执行上传/检入操作
		 */
		if(e.getSource() == uploadOrCheckoutB){
			//
		}
	}

	/**
	 * 生成设计源文件的线程。
	 * 
	 * @author Administrator
	 * 
	 */
	private class GeneralXmlTask implements Runnable {

		@Override
		public void run() {
			logicXmlList = new ArrayList<File>();
			// 这个设置进度条上显示设置的文本信息。
			xmlPB.setStringPainted(true);
			List<String> logicDesignNameAndRevision = new ArrayList<String>(
					selectedLogicDesignMap.keySet());
			int logicDesignCount = logicDesignNameAndRevision.size();
			out.println("logicDesignNameAndRevision.size():"
					+ logicDesignNameAndRevision.size());
			for (int i = 1; i <= logicDesignNameAndRevision.size(); i++) {
				String key = logicDesignNameAndRevision.get(i - 1);
				IXLogicDesign logicDesign = selectedLogicDesignMap.get(key);
				GenerateFilesUtil generateFilesUtil = new GenerateFilesUtil(
						context, currentProject.getAttribute("Name"));
				File designBaseDir = generateFilesUtil
						.createBaseDir(logicDesign);
				if (designBaseDir != null) {
					File logicXml = generateFilesUtil
							.createDesignXml(logicDesign);
					logicXmlList.add(logicXml);
				}
				xmlPB.setString("正在生成设计：【" + key + "】，文件完成进度：" + i + "/"
						+ logicDesignCount);
				xmlPB.setValue((int) (((float) i / (float) logicDesignCount) * 100));

			}
			xmlPB.setString(logicDesignCount + "个设计的源文件已经生成完毕！");
			out.println("共生成logicXml文件个数为:" + logicXmlList.size() + "名称分别是："
					+ "\n");
			for (File file : logicXmlList) {
				out.println(file.getName() + ":" + file.getAbsolutePath()
						+ "\n");
			}
			JOptionPane.showMessageDialog(CheckInFrameTable2.this,
					"设计源文件生成完成，共有【" + logicXmlList.size() + "】个文件生成！");
		}

	}

	/**
	 * 生成设计图纸PDF的线程。
	 * 
	 * @author Administrator
	 * 
	 */
	private class GeneralPdfTask implements Runnable {

		@Override
		public void run() {
			logicPdfMap = new HashMap<IXLogicDesign, List<File>>();
			// 这个设置进度条上显示设置的文本信息。
			pdfPB.setStringPainted(true);
			List<String> logicDesignNameAndRevision = new ArrayList<String>(
					selectedLogicDesignMap.keySet());
			int logicDesignCount = logicDesignNameAndRevision.size();
			out.println("logicDesignNameAndRevision.size():"
					+ logicDesignNameAndRevision.size());
			for (int i = 1; i <= logicDesignNameAndRevision.size(); i++) {
				String key = logicDesignNameAndRevision.get(i - 1);
				IXLogicDesign logicDesign = selectedLogicDesignMap.get(key);
				GenerateFilesUtil generateFilesUtil = new GenerateFilesUtil(
						context, currentProject.getAttribute("Name"));
				File designBaseDir = generateFilesUtil
						.createBaseDir(logicDesign);
				List<File> logicPdfList = new ArrayList<File>();
				List<IXDiagram> diagramList = new ArrayList<IXDiagram>(
						logicDesign.getDiagrams());
				if (designBaseDir != null && !diagramList.isEmpty()) {
					for (IXDiagram diagram : diagramList) {
						File logicPdf = generateFilesUtil
								.createDesignPDF(diagram);
						logicPdfList.add(logicPdf);

					}
				}
				logicPdfMap.put(logicDesign, logicPdfList);
				pdfPB.setString("正在生成设计：【" + key + "】，文件完成进度：" + i + "/"
						+ logicDesignCount);
				pdfPB.setValue((int) (((float) i / (float) logicDesignCount) * 100));

			}
			pdfPB.setString(logicDesignCount + "个设计的源文件已经生成完毕！");
			out.println("共生成logicXml文件个数为:" + logicXmlList.size() + "名称分别是："
					+ "\n");
			for (IXLogicDesign logicDesign : logicPdfMap.keySet()) {
				List<File> logicPdfList = new ArrayList<File>();
				for (File file2 : logicPdfList) {
					out.println(logicDesign.getAttribute("Name") + "设计的图纸有:"
							+ file2.getAbsolutePath() + "\n");
				}
			}
			JOptionPane.showMessageDialog(CheckInFrameTable2.this,
					"设计图纸生成完成，共有【" + logicXmlList.size() + "】个文件生成！");
		}

	}

	/**
	 * 生成设计明细表Excel的线程。
	 * 
	 * @author Administrator
	 * 
	 */
	private class GeneralExcelTask implements Runnable {

		@Override
		public void run() {
			logicExcelList = new ArrayList<File>();
			// 这个设置进度条上显示设置的文本信息。
			excelPB.setStringPainted(true);
			List<String> logicDesignNameAndRevision = new ArrayList<String>(
					selectedLogicDesignMap.keySet());
			int logicDesignCount = logicDesignNameAndRevision.size();
			out.println("logicDesignNameAndRevision.size():"
					+ logicDesignNameAndRevision.size());
			for (int i = 1; i <= logicDesignNameAndRevision.size(); i++) {
				String key = logicDesignNameAndRevision.get(i - 1);
				IXLogicDesign logicDesign = selectedLogicDesignMap.get(key);
				GenerateFilesUtil generateFilesUtil = new GenerateFilesUtil(
						context, currentProject.getAttribute("Name"));
				File designBaseDir = generateFilesUtil
						.createBaseDir(logicDesign);
				if (designBaseDir != null) {
					File excelXml = generateFilesUtil
							.createDesignXLS(logicDesign);
					logicExcelList.add(excelXml);
				}
				excelPB.setString("正在生成设计：【" + key + "】，文件完成进度：" + i + "/"
						+ logicDesignCount);
				excelPB.setValue((int) (((float) i / (float) logicDesignCount) * 100));

			}
			excelPB.setString(logicDesignCount + "个设计的源文件已经生成完毕！");
			out.println("共生成logicExcel文件个数为:" + logicExcelList.size()
					+ "名称分别是：" + "\n");
			for (File file : logicExcelList) {
				out.println(file.getName() + ":" + file.getAbsolutePath()
						+ "\n");
			}
			JOptionPane.showMessageDialog(CheckInFrameTable2.this,
					"设计明细表生成完成，共有【" + logicExcelList.size() + "】个文件生成！");
		}

	}

}
