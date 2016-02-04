package com.acconsys.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.acconsys.cws.CisServer;
import com.acconsys.cws.SOAPClient;
import com.acconsys.cws.SOAPRequest;
import com.acconsys.cws.SOAPResponse;
import com.acconsys.service.LogicDataService;
import com.mentor.chs.api.IXDesign;
import com.mentor.chs.api.IXDiagram;
import com.mentor.chs.api.IXGraphicConversions.XPDFColorChoice;
import com.mentor.chs.api.IXGraphicConversions.XPDFContext;
import com.mentor.chs.api.IXGraphicConversions.XPDFMargins;
import com.mentor.chs.api.IXGraphicConversions.XPDFPageSizeChoice;
import com.mentor.chs.api.IXGraphicConversions.XPDFPaperOrientationChoice;
import com.mentor.chs.api.IXGraphicConversions.XPDFPrintableAreaChoice;
import com.mentor.chs.api.IXLogicDesign;
import com.mentor.chs.plugin.IXApplicationContext;
import com.mentor.chs.plugin.IXOutputWindow;

/**
 * 这个文件生成工具类主要用来生成设计文件（xml源文件),设计图纸(PDF),明细表(xls)
 * 
 * @author Administrator
 * 
 */
public class GenerateFilesUtil {

	private Logger logger = Logger.getLogger(GenerateFilesUtil.class);
	private static GenerateFilesUtil generateFilesUtil;
	private String baseDir;
	private String currentProjectName;
	private IXApplicationContext context;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SS");

	public File createBaseDir(IXDesign design) {
		String designName = design.getAttribute("Name");
		String revision = design.getAttribute("Revision");
		baseDir = PropertyUtil.getPropertyUtil().getValue(Constants.FILE_PATH)
				+ "/" + currentProjectName + "/" + designName + "/" + revision
				+ "/";
		File baseDirFile = new File(baseDir);
		if (!baseDirFile.exists()) {
			baseDirFile.mkdirs();
		}
		if (baseDirFile.exists()) {
			return baseDirFile;
		} else {
			System.out.println("无法创建路径:" + baseDirFile.getAbsolutePath());
			return null;
		}

	}

	public GenerateFilesUtil(IXApplicationContext context,
			String currentProjectName) {
		this.context = context;
		this.currentProjectName = currentProjectName;
	}

	/**
	 * 生成设计文件（源文件xml)
	 * 
	 * @param context
	 * @return 返回生成的文件
	 */
	public File createDesignXml(IXDesign design) {
		String designName = design.getAttribute("Name");
		String revision = design.getAttribute("Revision");
		File file = null;
		try {
			file = new File(baseDir
					+ designName+"_"+revision+"_"+sdf.format(new Date())
					+ PropertyUtil.getPropertyUtil().getValue(
							Constants.DESIGN_XML));
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
					// out.println(file.getAbsolutePath() + "路径创建失败");
					System.out.println(file.getAbsolutePath() + "路径创建失败");
				}
			}
			// out.println("DESIGNXML: " + file.getAbsolutePath());
			CisServer server = new CisServer(PropertyUtil.getPropertyUtil()
					.getValue(Constants.CIS_IP), Integer.parseInt(PropertyUtil
					.getPropertyUtil().getValue(Constants.CIS_PORT)));// 这里localhost可以用127.0.0.1来替换
			SOAPRequest request = new SOAPRequest();
			request.setUsername(PropertyUtil.getPropertyUtil().getValue(
					Constants.CIS_USERNAME));
			request.setPassword(PropertyUtil.getPropertyUtil().getValue(
					Constants.CIS_PASSWORD));
			request.setPayload("<wiringdesign name='"
					+ design.getAttribute("Name") + "' projectname='"
					+ currentProjectName + "'/>");
			request.build(true);
			SOAPClient client = new SOAPClient();
			SOAPResponse response = client.callService(request,
					server.getDesignExporterService());
			response.saveBody(file);
		} catch (Exception e) {
			logger.info(e.getMessage());
//			file = null;
			e.printStackTrace();
		}
		return file;

	}

	/**
	 * 生成设计图纸（图纸PDF,一个设计下可能会有多个图纸，一个图纸为一个PDF)
	 * 
	 * @param context
	 * @return 返回生成的文件
	 */
	public File createDesignPDF(IXDiagram diagram) {
		File file = new File(baseDir + diagram.getAttribute("Name")
				+ PropertyUtil.getPropertyUtil().getValue(Constants.DESIGN_PDF));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
				// out.println(file.getAbsolutePath() + "路径创建失败");
				System.out.println(file.getAbsolutePath() + "路径创建失败");
			}
		}
		// out.println("DESIGN_PDF: " + file.getAbsolutePath());
		try {
			getPDFFromDiagram(file, diagram);
		} catch (Exception e) {
			logger.info(e.getMessage());
			file = null;
			e.printStackTrace();
		}
		return file;

	}

	private void getPDFFromDiagram(File file, IXDiagram diagram)
			throws Exception {
		if (diagram != null) {
			XPDFContext pContext = getXPDFContext();
			OutputStream out = new FileOutputStream(file);
			diagram.getPDF(out, pContext);
			out.flush();
			out.close();
		}
	}

	private XPDFContext getXPDFContext() {
		XPDFContext pdfcontext = new XPDFContext();

		pdfcontext.pageSize = XPDFPageSizeChoice.AUTO_SELECT;
		pdfcontext.dashMultiplier = 1.0d;
		pdfcontext.paperOrientation = XPDFPaperOrientationChoice.LANDSCAPE;
		pdfcontext.printableArea = XPDFPrintableAreaChoice.FIT_TO_BORDER;
		pdfcontext.color = XPDFColorChoice.COLOR;
		pdfcontext.margins = new XPDFMargins(0, 0, 0, 0);
		return pdfcontext;
	}

	/**
	 * 产生零件明细表 这里只有IXLogicDesign可以有零件明细表。 即这里的design可以强转为IXLogicDesign
	 * 
	 * @param selectDesign
	 * @return
	 */
	public File createDesignXLS(IXLogicDesign design) {

		File file = new File(baseDir + design.getAttribute("Name")
				+ PropertyUtil.getPropertyUtil().getValue(Constants.DESIGN_XLS));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
				System.out.println(file.getAbsolutePath() + "文件路径出错");
				// out.println(file.getAbsolutePath() + "文件路径出错");
			}
		}
		// out.println("DESIGN_XLS: " + file.getAbsoluteFile());

		DGFreemarkerToDoc doc = new DGFreemarkerToDoc();
		LogicDataService dataService = new LogicDataService(context, design);
		if (file != null) {
			try {
				doc.outPutFile(dataService.getTerminalTableModel(),
						file.getAbsolutePath(), "../plugins/tpl",
						"ItemBom1.tpl");
			} catch (Exception e2) {
				file = null;
				logger.info(e2.getMessage());
				e2.printStackTrace();
			}
		}
		return file;
	}
}
