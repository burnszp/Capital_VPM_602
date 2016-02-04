package com.acconsys.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.acconsys.model.ButtonCellRenderer;
import com.acconsys.model.FileTableModel;
import com.acconsys.model.MyTableModel;
import com.acconsys.model.ProgressBarCellEditor;
import com.acconsys.model.ProgressBarCellRenderer;
import com.acconsys.util.DesignUtil;
import com.acconsys.util.GenerateFilesUtil;
import com.acconsys.util.TableUtil;
import com.mentor.chs.api.IXHarnessDesign;
import com.mentor.chs.api.IXIntegratorDesign;
import com.mentor.chs.api.IXLogicDesign;
import com.mentor.chs.api.IXProject;
import com.mentor.chs.api.IXTopologyDesign;
import com.mentor.chs.plugin.IXApplicationContext;
import com.mentor.chs.plugin.IXOutputWindow;

public class CheckInFrameTable extends JDialog implements ActionListener {

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
	public List<File> logicXmlList ;
	private IXProject currentProject;

	public CheckInFrameTable(IXApplicationContext context) {
//		super(context.getParentFrame(),"123456", true);
		setModal(true);
		this.context = context;
		this.currentProject = context.getCurrentProject();
		CheckInFrameTable.out = context.getOutputWindow();
		out.println("开始：context" + context.toString());
		initData();
		initUI();
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				CheckInFrameTable.this.dispose();
//				super.windowClosing(e);
//			}
//		});
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
//		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);// 调用任意已注册WindowListener的对象后自动隐藏并释放该窗体。
//		setModalityType(JDialog.DEFAULT_MODALITY_TYPE);//setModal(true);
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
		filePanel = new JScrollPane();
		filePanel.setPreferredSize(new Dimension(1000, 200));
		filePanel.setBorder(BorderFactory.createTitledBorder("文件区域"));
		Object[][] fileTableData = { { "设计文件：", null, null, null },
				{ "设计图纸：", null, null, null }, { "明细表：", null, null, null } };
		fileTable = new JTable(new FileTableModel(fileTableHeader,
				fileTableData));
		renderFileTable();

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
		buttonPanel.setBorder(BorderFactory.createTitledBorder("功能按钮区域"));
		JButton uploadOrCheckoutB = new JButton("上传/检入");
		uploadOrCheckoutB.setActionCommand("checkIn");
		uploadOrCheckoutB.addActionListener(this);
		JButton cancelB = new JButton("关闭");
		cancelB.setActionCommand("close");
		cancelB.addActionListener(this);
		buttonPanel.add(uploadOrCheckoutB);
		buttonPanel.add(cancelB);
		bottomPanel.add(buttonPanel, BorderLayout.CENTER);

	}

	private void renderFileTable() {
		/**
		 * 设置列宽
		 */
		fileTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		fileTable.getColumnModel().getColumn(1).setPreferredWidth(700);
		fileTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		fileTable.getColumnModel().getColumn(3).setPreferredWidth(100);

		fileTable.getColumnModel().getColumn(1)
				.setCellRenderer(new ProgressBarCellRenderer());
		fileTable.getColumnModel().getColumn(1)
				.setCellEditor(new ProgressBarCellEditor());
		fileTable.getColumnModel().getColumn(2)
				.setCellRenderer(new ButtonCellRenderer(2));
		fileTable.getColumnModel().getColumn(2)
				.setCellEditor(new ButtonCellEditor(2));
		fileTable.getColumnModel().getColumn(3)
				.setCellRenderer(new ButtonCellRenderer(3));
		fileTable.getColumnModel().getColumn(3)
				.setCellEditor(new ButtonCellEditor(3));
		// 设置行高
		fileTable.setRowHeight(25);
		filePanel.setViewportView(fileTable);
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
		// filterCB.addItemListener(this);
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
//					logicTable.updateUI();
//					SwingUtilities.updateComponentTreeUI(logicTable);
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

	class ButtonCellEditor extends DefaultCellEditor implements ActionListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton button;

		public ButtonCellEditor(int col) {
			super(new JTextField());
			this.button = new JButton();
			this.setClickCountToStart(1);
			if (2 == col) {
				this.button.setText("生成文件");
			} else if (3 == col) {
				this.button.setText("查看详情");
			}
			this.button.addActionListener(this);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			return this.button;
		}

		@Override
		public Object getCellEditorValue() {
			return this.button.getText();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			createFile();
		}
	}

	public void createFile(){
		System.out.println(context.getCurrentUser().getFullName());
		System.out.println(context.getCurrentProject());
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
		JOptionPane.showMessageDialog(CheckInFrameTable.this, b.toString());
		
		int selectRow = fileTable.getSelectedRow();
		int selectcolumn = fileTable.getSelectedColumn();
		out.println("文件区域选中行和列" + selectRow + selectcolumn);
		if (selectRow == 0 && selectcolumn == 2) {
			out.println("开始生成设计文件XML");
			JOptionPane.showMessageDialog(CheckInFrameTable.this,
					"开始生成设计文件XML");
			ProgressBarCellEditor logicXmlProgressBarCellEditor =  (ProgressBarCellEditor) fileTable.getCellEditor(0, 1);
			JProgressBar logicXmlProgressBar = (JProgressBar) logicXmlProgressBarCellEditor.getTableCellEditorComponent(fileTable, "开始生成logic源文件", true, 0, 1);
			if(logicXmlProgressBar != null){
				logicXmlList = new ArrayList<File>();
				List<String> logicDesignNameAndRevision = new ArrayList<String>(selectedLogicDesignMap.keySet());
				int logicDesignCount = logicDesignNameAndRevision.size();
				for (int i=0;i<logicDesignNameAndRevision.size();i++) {
					String key = logicDesignNameAndRevision.get(i);
					IXLogicDesign logicDesign = selectedLogicDesignMap
							.get(key);
					logicXmlProgressBar.setString("正在生成设计【"+key+"】，当前完成"+i+"/"+logicDesignCount);
					GenerateFilesUtil generateFilesUtil = new GenerateFilesUtil(
							context, currentProject.getAttribute("Name"));
					File designBaseDir = generateFilesUtil
							.createBaseDir(logicDesign);
					if (designBaseDir != null) {
						File logicXml = generateFilesUtil
								.createDesignXml(logicDesign);
						logicXmlList.add(logicXml);
						logicXmlProgressBar.setValue((int) (((float)i+1/(float)logicDesignCount)*100));
					}
					
				}
				logicXmlProgressBar.setString(logicDesignCount+"个设计的源文件已经生成完毕！");
				out.println("共生成logicXml文件个数为:" + logicXmlList.size()
						+ "名称分别是：" + "\n");
				for (File file : logicXmlList) {
					out.println(file.getName() + ":" + file.getAbsolutePath()
							+ "\n");
				}
				JOptionPane.showMessageDialog(CheckInFrameTable.this,
						"设计源文件生成完成，共有【" + logicXmlList.size() + "】个文件生成！");
				
			}
			
		} else if (selectRow == 1 && selectcolumn == 2) {
			out.println("开始生成设计图纸PDF");
			JOptionPane.showMessageDialog(CheckInFrameTable.this,
					"开始生成设计图纸PDF");
		} else if (selectRow == 2 && selectcolumn == 2) {
			out.println("开始生成明细表Excel");
			JOptionPane.showMessageDialog(CheckInFrameTable.this,
					"开始生成明细表Excel");
		} else if (selectRow == 0 && selectcolumn == 3) {
			out.println("查看生成的设计文件XML");
			JOptionPane.showMessageDialog(CheckInFrameTable.this,
					"查看生成的设计文件XML");
			if (logicXmlList != null && !logicXmlList.isEmpty()) {
				/**
				 * 弹出查看logic源文件窗口。
				 */
				out.println("logicXmlList数据不为空!" + logicXmlList.size());
				ShowLogicXmlInfoDialog slxiDialog = new ShowLogicXmlInfoDialog(logicXmlList);
				slxiDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(CheckInFrameTable.this,
						"logicXmlList为空");
			}
		} else if (selectRow == 1 && selectcolumn == 3) {
			out.println("查看生成的设计图纸PDF");
			JOptionPane.showMessageDialog(CheckInFrameTable.this,
					"查看生成的设计图纸PDF");
		} else if (selectRow == 2 && selectcolumn == 3) {
			out.println("查看生成的明细表Excel");
			JOptionPane.showMessageDialog(CheckInFrameTable.this,
					"查看生成的明细表Excel");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
