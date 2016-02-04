package com.acconsys.plugin;

import javax.swing.Icon;

import com.acconsys.ui.CheckInFrame;
import com.mentor.chs.plugin.IXApplicationContext;
import com.mentor.chs.plugin.action.IXApplicationAction;

public class CheckInAction implements IXApplicationAction{

	@Override
	public boolean execute(IXApplicationContext context) {
		CheckInFrame checkInFrame = new CheckInFrame(context);
		checkInFrame.setVisible(true);
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
		return "602设计检入";
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
