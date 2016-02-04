根据Capital-VPM集成方案开发接口定义【2015_12_22】.xlsx分析如下
1:需要设置的包如下：
com.acconsys.vpm.model
com.acconsys.vpm.plugin
com.acconsys.vpm.ui
com.acconsys.vpm.util
com.acconsys.vpm.test

602.properties中的配置信息如下：

------------------设计检入阶段---------------------

vpmUser：vpm系统登录用户名
vpmPassword：vpm系统登录密码
验证用户名和密码，如果通过验证返回loginResult = True, 否则返回False【接口】


releaselevel：draft--可检入的设计
导出设计源文件、PDF文件和明细表到文件服务器
projectName——Capital项目名，对应VPM中的项目名称
designName——Capital设计名，对应VPM中的设计图号（零件号）
partNumber——Capital线束零件号，对应VPM中的线束零件号（仅对Harness设计有效）
revision——Capital设计版本号，对应VPM中的设计版本号

xmlSrcLocation——Capital设计源文件所在文件服务器路径，根据该参数获取设计源文件
designPDFLocation——Capital设计PDF图纸所在文件服务器路径，根据该参数获取设计PDF图纸
detailItemLocation——Logic设计零件明细表所在文件服务器路径，根据该参数获取零件明细表


如果接收到isSuccess = True，则完成检入流程，将当前设计release Level从draft改为Pending
如果接受到isSuccess = False，则显示errorMsg的内容，设计保持draft状态

------------------设计检出阶段---------------------
显示VPM登陆对话框，用户输入登录信息，确定后传递项目名称（projectName），用户名（vpmUsername），密码（vpmPassword）至VPM
如果登陆信息正确，给出项目下的设计名称和版本列表（release level 为工作中）

如果获得xmlSrcLocation，则根据值去文件服务器寻找对应文件，如果没找到则给出错误信息，如果找到则在项目下导入该设计，将designName作为设计名称，将revision作为新设计版本号
传递checkoutSuccess = True给VPM



------------------设计releaselevel修改---------------------
1.
VPM中进行设计审签流程，最终设计被驳回修改，则传递changeReleaselevel=[projectName, designName, revision, releaselevel]给Capital,此处releaselevel为draft
Capital获取changeReleaselevel属性，找到对应的project , design, revision，更改其releaselevel为draft，如果没有找到对应的proejct ,design, revision，则报错，如果当前releaselevel不为pending，也报错，并且不进行修改
2.
VPM中进行设计审签流程，最终设计通过审核，归档，则传递changeReleaselevel=[projectName, designName, revision, releaselevel]给Capital,此处releaselevel为released
Capital获取changeReleaselevel属性，找到对应的project , design, revision，更改其releaselevel为released，如果没有找到对应的proejct ,design, revision，则报错，如果当前releaselevel不为pending，也报错，并且不进行修改

