package com.guy.calc.util.console;

import java.io.PrintWriter;

public class Cursor
{
	private int 		currentPos;
	private int			eolPos;
	private PrintWriter writer;
	
	private static Cursor instance = null;
	
	private Cursor(PrintWriter writer) {
		this.writer = writer;
		
		currentPos = 0;
		eolPos = 0;
	}
	
	public static Cursor getInstance(PrintWriter writer)
	{
		if (instance == null) {
			instance = new Cursor(writer);
		}
		
		return instance;
	}

	
	public int getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
		this.eolPos = currentPos;
	}

	public int getEolPos() {
		return eolPos;
	}

	public void setEolPos(int eolPos) {
		this.eolPos = eolPos;
	}

	public void left() {
		currentPos--;

		if (currentPos < 0) {
			currentPos = 0;
			return;
		}
		
		writer.write(CalcTerminal.CHAR_ESCAPE);
		writer.write(CalcTerminal.CHAR_CSI);
		writer.write(CalcTerminal.CHAR_LEFT);
		writer.flush();
	}
	
	public void left(int n) {
		int i = 0;
		
		if (n < 0) {
			n = 0;
		}
		
		for (i = 0;i < n;i++) {
			left();
		}
	}
	
	public void right() {
		currentPos++;

		if (currentPos > eolPos) {
			currentPos = eolPos;
			return;
		}
		
		writer.write(CalcTerminal.CHAR_ESCAPE);
		writer.write(CalcTerminal.CHAR_CSI);
		writer.write(CalcTerminal.CHAR_RIGHT);
		writer.flush();
	}
	
	public void right(int n) {
		int i = 0;
		
		if (n < 0) {
			n = 0;
		}
		
		for (i = 0;i < n;i++) {
			right();
		}
	}
	
	public void clearLine() {
		while (currentPos > 0) {
			left();
		}
	}
	
	public void typeChar(char ch) {
		writer.print(ch);
		writer.flush();
		
		currentPos++;
		eolPos++;
	}
	
	public void newLine() {
		writer.println();
		writer.flush();
		
		currentPos = 0;
		eolPos = 0;
	}
}
