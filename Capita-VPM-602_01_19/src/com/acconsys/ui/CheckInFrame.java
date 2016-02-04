package com.acconsys.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import com.acconsys.model.CVCheckInTablePanel;
import com.mentor.chs.api.IXHarnessDesign;
import com.mentor.chs.api.IXIntegratorDesign;
import com.mentor.chs.api.IXLogicDesign;
import com.mentor.chs.api.IXProject;
import com.mentor.chs.api.IXTopologyDesign;
import com.mentor.chs.plugin.IXApplicationContext;
import com.mentor.chs.plugin.IXOutputWindow;

public class CheckInFrame extends JFrame implements ActionListener {

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;

	private IXApplicationContext context;
	private IXOutputWindow out;
	private IXProject currentProject;
	private List<IXLogicDesign> logicDesignList;
	private List<IXHarnessDesign> harnessDesignList;
	private List<IXIntegratorDesign> integratorDesignList;
	private List<IXTopologyDesign> topologyDesignList;
	private String[] headers = { "设计名称", "设计版本", "状态", "创建人", "创建时间", "最后修改人",
			"最后修改时间", "最后检入/出人", "最后检入/出时间", };
	private JTabbedPane designTabbedPane;
	private CVCheckInTablePanel logicPanel;

	public CheckInFrame(IXApplicationContext context) {
		this.context = context;
		this.out = context.getOutputWindow();
		this.currentProject = context.getCurrentProject();
		initData();
		initUI();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Set<IXLogicDesign> logicDesignSet = currentProject.getLogicDesigns();
		Set<IXHarnessDesign> harnessDesignSet = currentProject
				.getHarnessDesigns();
		Set<IXIntegratorDesign> integratorDesignSet = currentProject
				.getIntegratorDesigns();
		Set<IXTopologyDesign> topologyDesignSet = currentProject
				.getTopologyDesigns();
		logicDesignList = new ArrayList<IXLogicDesign>(logicDesignSet);
		harnessDesignList = new ArrayList<IXHarnessDesign>(harnessDesignSet);
		integratorDesignList = new ArrayList<IXIntegratorDesign>(
				integratorDesignSet);
		topologyDesignList = new ArrayList<IXTopologyDesign>(topologyDesignSet);
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		setTitle("设计检入");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 调用任意已注册WindowListener的对象后自动隐藏并释放该窗体。
		/**
		 * 创建主面板
		 */
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		setContentPane(contentPanel);

		/**
		 * 创建顶部区域
		 */
		designTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		designTabbedPane.setBorder(BorderFactory.createTitledBorder("当前项目"
				+ currentProject.getAttribute("Name") + "的相关设计信息如下："));
		contentPanel.add(designTabbedPane, BorderLayout.CENTER);

		/**
		 * logic面板
		 */
		logicPanel = new CVCheckInTablePanel(
				logicDesignList, headers);
		designTabbedPane.addTab("Logic", null, logicPanel, "Logic设计列表");
		logicPanel.setBorder(BorderFactory.createTitledBorder("当前项目"
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

		/**
		 * 创建底部区域
		 */
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(bottomPanel, BorderLayout.SOUTH);

		/**
		 * 文件区域
		 */
		JPanel filePanel = new JPanel();
		filePanel.setBorder(BorderFactory.createTitledBorder("文件区域"));
		filePanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;// 当格子有剩余空间时，填充空间；其他还有，HORIZONTAL:只在水平方向上填充，VERTICAL:只在垂直方向是填充，NONE:保持原状。
		constraints.anchor = GridBagConstraints.NORTHWEST;// 当组件没有空间大时，居中缩小：其他的还有NORTH:顶部缩小，NORTHEAST:左上角缩小，EAST;右侧缩小
		// constraints.weightx = 2;// x方向上的增量：当窗口缩放时，组件完全填充单元格，当为0.0时，保持不变
		// constraints.weighty = 3;
		constraints.insets = new Insets(1, 1, 1, 1);// 组件彼此间的间距
		constraints.ipadx = 0;// 组件内部填充空间，即给组件的最小宽度添加多大的空间
		constraints.ipady = 0;// 组件内部填充空间，即给组件的最小高度添加多大的空间
		JLabel xmlL = new JLabel("设计XML文件：");
		xmlL.setHorizontalAlignment(JLabel.RIGHT);
		JLabel pdfL = new JLabel("设计PDF图纸：");
		pdfL.setHorizontalAlignment(JLabel.RIGHT);
		JLabel excelL = new JLabel("零件明细表：");
		excelL.setHorizontalAlignment(JLabel.RIGHT);
		JProgressBar xmlPB = new JProgressBar();
		JProgressBar pdfPB = new JProgressBar();
		JProgressBar excelPB = new JProgressBar();
		add(filePanel, xmlL, constraints, 0, 0, 1, 2);
		add(filePanel, xmlPB, constraints, 1, 0, 0, 2);
		add(filePanel, pdfL, constraints, 0, 1, 1, 2);
		add(filePanel, pdfPB, constraints, 1, 1, 0, 2);
		add(filePanel, excelL, constraints, 0, 2, 1, 2);
		add(filePanel, excelPB, constraints, 1, 2, 0, 2);
		bottomPanel.add(filePanel, BorderLayout.CENTER);

		/**
		 * 按钮区域
		 */
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder("功能按钮区域"));
		JButton uploadOrCheckoutB = new JButton("上传/检入");
		uploadOrCheckoutB.setActionCommand("checkIn");
		uploadOrCheckoutB.addActionListener(this);
		JButton cancelB = new JButton("关闭");
		cancelB.setActionCommand("close");
		cancelB.addActionListener(this);
		buttonPanel.add(uploadOrCheckoutB);
		buttonPanel.add(cancelB);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

	}

	/**
	 * 文件区域布局
	 * 
	 * @param con
	 * @param com
	 * @param constraints
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	private void add(Container con, Component com,
			GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.weightx = x;
		constraints.weighty = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		con.add(com, constraints);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * 检入操作
		 */
		if ("checkIn".equals(e.getActionCommand())) {
//			if ("start".equalsIgnoreCase(e.getActionCommand())) {
//				// 如果用户点击“上传/检入”按钮，那么下面将会针对这三个JProgressBar(进度条)来创建三个线程。
//				int selectTabIndex = designTabbedPane.getSelectedIndex();
//				out.println("当前选中的的tab页是序列是:" + selectTabIndex);
//				if (selectTabIndex == 0) {
//					out.println("releaseLevel: "
//							+ logicPanel.getDesignReleasedLevel());
//					out.println("designInfo: "
//							+ logicPanel.getDesignInfo());
//					if (logicPanel.getDesignReleasedLevel() != null
//							&& logicPanel.getDesignReleasedLevel()
//									.equalsIgnoreCase("Draft")) {
//						String designInfo = logicPanel.getDesignInfo();
//						IXLogicDesign selectLogicDesign = logicMap
//								.get(designInfo);
//						selectDesign = selectLogicDesign;
//						// GenerateFilesUtil.getGenerateFilesUtil(context);
//						File baseDirFile = GenerateFilesUtil
//								.getGenerateFilesUtil(context).createBaseDir(
//										selectDesign);
//						// 这里将项目文件路径建立好。
//						if (baseDirFile != null) {
//							try {
//								// 这三个进度条是异步进行，每个进度条的三个事件是顺序进行。
//
//								/**
//								 * 设计文件进度条的事件，
//								 */
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										Thread dft = new Thread(
//												new DesignFileThread());
//										dft.start();
//									}
//								});
//								// 设计图纸进度条的事件，
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										Thread dgt = new Thread(
//												new DesignGraphThread());
//										dgt.start();
//									}
//								});
//								// 明细表进度条的事件，
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										DetailTableThread dtt = new DetailTableThread();
//										dtt.start();
//									}
//								});
//
//								// 三种文件生成结果展示,以及将相关生成数据传递给VPM系统，收取结果，显示最后检入结果。
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										Thread resultThread = new Thread() {
//											public void run() {
//												while (true) {
//													if (sjwjResult == 1
//															&& sjtzResult == 1
//															&& mxbResult == 1) {
//														// 这里之后调用VPM系统接口传递参数，
//														/**
//														 * 导出设计源文件、
//														 * PDF文件和明细表到文件服务器
//														 * ，成功则继续下一步，否则报错
//														 * 将以下参数传递给VPM
//														 * projectName
//														 * ——Capital项目名
//														 * ，对应VPM中的项目名称
//														 * designName
//														 * ——Capital设计名
//														 * ，对应VPM中的设计图号（零件号）
//														 * partNumber
//														 * ——Capital线束零件号
//														 * ，对应VPM中的线束零件号
//														 * （仅对Harness设计有效）
//														 * revision
//														 * ——Capital设计版本号
//														 * ，对应VPM中的设计版本号
//														 * 
//														 * xmlSrcLocation——
//														 * Capital设计源文件所在文件服务器路径
//														 * ，根据该参数获取设计源文件
//														 * designPDFLocation——
//														 * Capital设计PDF图纸所在文件服务器路径
//														 * ，根据该参数获取设计PDF图纸
//														 * detailItemLocation——
//														 * Logic设计零件明细表所在文件服务器路径
//														 * ，根据该参数获取零件明细表
//														 */
//														boolean draft2Pending = CISUtil
//																.updateDesignStatus(
//																		selectDesign
//																				.getAttribute("Name"),
//																		selectDesign
//																				.getAttribute("Revision"),
//																		currentProject
//																				.getAttribute("Name"),
//																		"Pending未知状态");
//														out.println("draft2Pending: "
//																+ draft2Pending);
//														if (draft2Pending) {
//															JOptionPane
//																	.showMessageDialog(
//																			null,
//																			"检入成功！",
//																			"检入执行结果",
//																			JOptionPane.INFORMATION_MESSAGE);
//															// out.println("检入成功");
//															JTable logicTable = logicPanel
//																	.getTable();
//															int selectedRow = logicTable
//																	.getSelectedRow();
//															logicTable
//																	.setValueAt(
//																			"Pending未知状态",
//																			selectedRow,
//																			2);
//															DefaultTableCellRenderer render = logicPanel
//																	.getRenderer();
//															logicTable
//																	.getColumn(
//																			2)
//																	.setCellRenderer(
//																			render);
//															break;
//														} else {
//															JOptionPane
//																	.showMessageDialog(
//																			null,
//																			"检入失败！",
//																			"检入执行结果",
//																			JOptionPane.INFORMATION_MESSAGE);
//															// out.println("检入成功");
//															break;
//														}
//													} else if (sjwjResult == -1
//															|| sjtzResult == -1
//															|| mxbResult == -1) {
//														out.println("sjwjResult: "
//																+ sjwjResult);
//														out.println("sjtzResult: "
//																+ sjtzResult);
//														out.println("mxbResult: "
//																+ mxbResult);
//														JOptionPane
//																.showMessageDialog(
//																		null,
//																		"检入失败！",
//																		"检入执行结果",
//																		JOptionPane.WARNING_MESSAGE);
//														// out.println("检入失败");
//														break;
//
//													} else {
//														try {
//															// 等待0.1秒
//															sleep(100);
//														} catch (InterruptedException e) {
//															e.printStackTrace();
//														}
//														// out.println("继续循环");
//														continue;
//													}
//
//												}
//											};
//										};
//										resultThread.start();
//									}
//								});
//
//							} catch (Exception e2) {
//								logger.info(e2.getMessage());
//							}
//							// 检入成功后，修改设计状态为Pending.
//							// CISUtil.startService();
//							/**
//							 * boolean checkinoutresult =
//							 * CISUtil.updateDesignStatus(
//							 * selectDesign.getAttribute("Name"),
//							 * selectDesign.getAttribute("Revision"),
//							 * currentProject.getAttribute("Name"),
//							 * selectDesign.getAttribute("Pending未知状态"));
//							 * out.println(Boolean.toString(checkinoutresult));
//							 * if (checkinoutresult) {
//							 * JOptionPane.showMessageDialog(
//							 * CapitalVPMMain_Table1Dialog.this, "检入成功", "提示",
//							 * JOptionPane.INFORMATION_MESSAGE); // return; }
//							 * else { JOptionPane.showMessageDialog(
//							 * CapitalVPMMain_Table1Dialog.this, "检入失败", "提示",
//							 * JOptionPane.WARNING_MESSAGE); // return; }
//							 */
//						} else {
//							JOptionPane
//									.showMessageDialog(
//											CapitalVPMMain_Table1Dialog.this,
//											"路径: "
//													+ baseDirFile
//															.getAbsoluteFile()
//													+ " 创建失败！或者CIS(Capital Integrator Server)服务未开启",
//											"提示", JOptionPane.WARNING_MESSAGE);
//							return;
//						}
//					} else {
//						JOptionPane.showMessageDialog(
//								CapitalVPMMain_Table1Dialog.this,
//								"没有选择一个设计或者，当前设计是不可检入状态，请选择一个Draft设计再操作", "提示",
//								JOptionPane.WARNING_MESSAGE);
//						return;
//					}
//				} else if (selectTabIndex == 1) {
//					// 这个是harness设计列表，harness没有零件明细表的生成。所以这里不用调用零件明细表的生成方法
//					out.println("releaseLevel: "
//							+ harnessTablePanel.getDesignReleasedLevel());
//					out.println("designInfo: "
//							+ harnessTablePanel.getDesignInfo());
//					if (harnessTablePanel.getDesignReleasedLevel() != null
//							&& harnessTablePanel.getDesignReleasedLevel()
//									.equalsIgnoreCase("Draft")) {
//						String designInfo = harnessTablePanel.getDesignInfo();
//						selectDesign = harnessMap.get(designInfo);
//						// GenerateFilesUtil.getGenerateFilesUtil(context);
//						// 这里将项目文件路径建立好。
//						File baseDirFile = GenerateFilesUtil
//								.getGenerateFilesUtil(context).createBaseDir(
//										selectDesign);
//						if (baseDirFile != null) {
//							try {
//								// 这三个进度条是异步进行，每个进度条的三个事件是顺序进行。
//
//								/**
//								 * 设计文件进度条的事件，
//								 */
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										Thread dft = new Thread(
//												new DesignFileThread());
//										dft.start();
//									}
//								});
//								// 设计图纸进度条的事件，
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										Thread dgt = new Thread(
//												new DesignGraphThread());
//										dgt.start();
//									}
//								});
//
//							} catch (Exception e2) {
//								logger.info(e2.getMessage());
//							}
//						} else {
//							JOptionPane
//									.showMessageDialog(
//											CapitalVPMMain_Table1Dialog.this,
//											"路径: "
//													+ baseDirFile
//															.getAbsoluteFile()
//													+ " 创建失败！或者CIS(Capital Integrator Server)服务未开启",
//											"提示", JOptionPane.WARNING_MESSAGE);
//						}
//					} else {
//						JOptionPane.showMessageDialog(
//								CapitalVPMMain_Table1Dialog.this,
//								"没有选择一个设计或者，当前设计是不可检入状态，请选择一个Draft设计再操作", "提示",
//								JOptionPane.WARNING_MESSAGE);
//					}
//				} else if (selectTabIndex == 2) {
//					// 这个是harness设计列表，harness没有零件明细表的生成。所以这里不用调用零件明细表的生成方法
//
//					// 这个是harness设计列表，harness没有零件明细表的生成。所以这里不用调用零件明细表的生成方法
//					out.println("releaseLevel: "
//							+ topologyAndIntegratorTablePanel
//									.getDesignReleasedLevel());
//					out.println("designInfo: "
//							+ topologyAndIntegratorTablePanel.getDesignInfo());
//					if (topologyAndIntegratorTablePanel
//							.getDesignReleasedLevel() != null
//							&& topologyAndIntegratorTablePanel
//									.getDesignReleasedLevel().equalsIgnoreCase(
//											"Draft")) {
//						String designInfo = topologyAndIntegratorTablePanel
//								.getDesignInfo();
//						IXTopologyDesign td = topologyMap.get(designInfo);
//						IXIntegratorDesign id = integratorMap.get(designInfo);
//						if (td != null) {
//							selectDesign = td;
//						}
//						if (id != null) {
//							selectDesign = id;
//						}
//						// GenerateFilesUtil.getGenerateFilesUtil(context);
//						File baseDirFile = GenerateFilesUtil
//								.getGenerateFilesUtil(context).createBaseDir(
//										selectDesign);
//						// 这里将项目文件路径建立好。
//						if (baseDirFile != null) {
//							try {
//								// 这三个进度条是异步进行，每个进度条的三个事件是顺序进行。
//
//								/**
//								 * 设计文件进度条的事件，
//								 */
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										Thread dft = new Thread(
//												new DesignFileThread());
//										dft.start();
//									}
//								});
//								// 设计图纸进度条的事件，
//								SwingUtilities.invokeLater(new Runnable() {
//
//									@Override
//									public void run() {
//										Thread dgt = new Thread(
//												new DesignGraphThread());
//										dgt.start();
//									}
//								});
//							} catch (Exception e2) {
//								logger.info(e2.getMessage());
//							}
//						} else {
//							JOptionPane
//									.showMessageDialog(
//											CapitalVPMMain_Table1Dialog.this,
//											"路径: "
//													+ baseDirFile
//															.getAbsoluteFile()
//													+ " 创建失败！或者CIS(Capital Integrator Server)服务未开启",
//											"提示", JOptionPane.WARNING_MESSAGE);
//						}
//					} else {
//						JOptionPane.showMessageDialog(
//								CapitalVPMMain_Table1Dialog.this,
//								"没有选择一个设计或者，当前设计是不可检入状态，请选择一个Draft设计再操作", "提示",
//								JOptionPane.WARNING_MESSAGE);
//					}
//
//				}
//
//			}
		}

		/**
		 * 关闭操作
		 */
		if ("close".equals(e.getActionCommand())) {
			this.dispose();
		}
	}

	public static void main(String[] args) {
		CheckInFrame checkInFrame = new CheckInFrame(null);
		checkInFrame.setVisible(true);
	}
}
