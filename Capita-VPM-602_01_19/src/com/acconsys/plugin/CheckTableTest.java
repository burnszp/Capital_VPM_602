package com.acconsys.plugin;

import javax.swing.Icon;

import com.acconsys.test.CheckButtonTableTest;
import com.mentor.chs.plugin.IXApplicationContext;
import com.mentor.chs.plugin.action.IXApplicationAction;

public class CheckTableTest implements IXApplicationAction{

	@Override
	public boolean execute(IXApplicationContext context) {
		CheckButtonTableTest checkInFrame = new CheckButtonTableTest(context);
		return true;
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return getDescription();
	}

	@Override
	public Integer getMnemonicKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Icon getSmallIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trigger[] getTriggers() {
		// TODO Auto-generated method stub
		return Trigger.values();
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public String getName() {
		return "测试checkButton";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public boolean isAvailable(IXApplicationContext arg0) {
		return true;
	}

}
