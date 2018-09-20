package com.guy.calc.util;

public class DebugHelper
{
	private boolean isDebugOn;
	private static DebugHelper instance = null;
		
	private DebugHelper() {
		setDebugOff();
	}
		
	public static DebugHelper getInstance()
	{
		if (instance == null) {
			instance = new DebugHelper();
		}
		
		return instance;
	}
	
	public void setDebugOn() {
		this.isDebugOn = true;
	}
	public void setDebugOff() {
		this.isDebugOn = false;
	}
	public void setDebug(boolean toggle) {
		this.isDebugOn = toggle;
	}
	
	public boolean isDebugOn() {
		return this.isDebugOn;
	}
}
