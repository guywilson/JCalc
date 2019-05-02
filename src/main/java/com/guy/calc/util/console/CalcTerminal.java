package com.guy.calc.util.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import com.guy.calc.CalcSystem;

public class CalcTerminal
{
	public final static byte CHAR_ESCAPE 		= 0x1B;
	public final static byte CHAR_CSI 			= 0x5B;
	public final static byte CHAR_UP 			= 0x41;
	public final static byte CHAR_DOWN 			= 0x42;
	public final static byte CHAR_LEFT 			= 0x44;
	public final static byte CHAR_RIGHT 		= 0x43;
	public final static byte CHAR_BACKSPACE 	= 0x08;
	public final static byte CHAR_DELETE 		= 0x7F;
	public final static byte CHAR_NEWLINE 		= 0x0A;
	public final static byte CHAR_ERASEINLINE 	= 0x4B;
	
	private Terminal terminal = null;
	private NonBlockingReader reader;
	private PrintWriter writer;
	
	private LinkedList<String> commandList = null;
	private int currentCommandPos = 0;
	
	private Cursor cursor;
	
	private static CalcTerminal instance = null;
	
	private CalcTerminal()
	{
		try {
			terminal = TerminalBuilder.builder().jna(true).system(true).build();
			terminal.enterRawMode();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		reader = terminal.reader();
		writer = terminal.writer();
		
		commandList = new LinkedList<>();
		
		cursor = Cursor.getInstance(writer);
	}
	
	public static CalcTerminal getInstance()
	{
		if (instance == null) {
			instance = new CalcTerminal();
		}
		
		return instance;
	}
	
	public NonBlockingReader getReader() {
		return reader;
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public Cursor getCursor() {
		return cursor;
	}
	
	public void addCommand(String cmd) {
		commandList.addFirst(cmd);
	}
	
	public String getNextCommand() {
		if (commandList.size() == 0) {
			return "";
		}
		
		if (currentCommandPos < 0) {
			currentCommandPos = 0;
		}
		else if (currentCommandPos >= commandList.size()) {
			currentCommandPos = commandList.size() - 1;
		}
		
		return commandList.get(currentCommandPos++);
	}
	
	public String getPreviousCommand() {
		if (commandList.size() == 0) {
			return "";
		}
		
		if (currentCommandPos < 0) {
			currentCommandPos = 0;
		}
		else if (currentCommandPos >= commandList.size()) {
			currentCommandPos = commandList.size() - 1;
		}
		
		return commandList.get(currentCommandPos--);
	}
	
	public void prompt() {
		String prompt = "calc [" + CalcSystem.getInstance().getModeStr() + "]> ";
		
		writer.print(prompt);
		writer.flush();
	}
}
