package com.guy.calc.util.console;

import java.io.IOException;
import java.io.PrintWriter;

import org.jline.utils.NonBlockingReader;

public class LineReader
{
	CalcTerminal terminal = null;
	
	private NonBlockingReader 	reader;
	private PrintWriter 		writer;
	private Cursor 				cursor;
	private StringBuffer		line;

	public LineReader()
	{
		terminal = CalcTerminal.getInstance();
		
		reader = terminal.getReader();
		writer = terminal.getWriter();
		
		cursor = terminal.getCursor();
		
		line = new StringBuffer();
	}
	
	protected int readch() throws IOException
	{
		int ch = 0;
		
		try {
			ch = reader.read();
		}
		catch (IOException e) {
			throw e;
		}
		
		return ch;
	}
	
	public String readLine() throws Exception
	{
		String			ln = null;
		int				ch = 0;
		boolean			go = true;
		
		while (go) {
			try {
				ch = readch();
			}
			catch (IOException e) {
				System.out.println("Caught exception: " + e.getMessage());
				throw e;
			}

			try {
				switch (ch) {
					case CalcTerminal.CHAR_ESCAPE:
						handleEscape();
						break;
					
					case '\r':
					case CalcTerminal.CHAR_NEWLINE:
						ln = line.toString();
						line.setLength(0);
						terminal.addCommand(ln);
						cursor.newLine();
						go = false;
						break;
						
					case CalcTerminal.CHAR_DELETE:
						handleDelete();
						break;
						
					default:
						handleInsert((char)ch);
						break;
				}
			}
			catch (Exception e1) {
				System.out.println("Caught exception: " + e1.getMessage());
				throw e1;
			}
		}
		
		
		return ln;
	}
	
	private void handleEscape() throws Exception
	{
		int		ch = 0;
		
		try {
			ch = readch();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		switch (ch) {
			case CalcTerminal.CHAR_CSI:
				handleCSI();
				break;
				
			default:
				break;
		}
	}
	
	private void handleCSI() throws Exception
	{
		String	cmd = null;
		int		ch = 0;
		
		try {
			ch = readch();
		}
		catch (IOException e) {
			System.out.println("Caught exception: " + e.getMessage());
			throw e;
		}
		
		try {
			switch (ch) {
				case CalcTerminal.CHAR_UP:
					cmd = terminal.getNextCommand();

					terminal.getCursor().clearLine();
					writer.print(cmd);
					writer.flush();
					
					line.setLength(0);
					line.append(cmd);

					cursor.setCurrentPos(cmd.length());
					break;
					
				case CalcTerminal.CHAR_DOWN:
					cmd = terminal.getNextCommand();

					terminal.getCursor().clearLine();
					writer.print(cmd);
					writer.flush();
					
					line.setLength(0);
					line.append(cmd);

					cursor.setCurrentPos(cmd.length());
					break;
					
				case CalcTerminal.CHAR_LEFT:
					terminal.getCursor().left();
					break;
					
				case CalcTerminal.CHAR_RIGHT:
					terminal.getCursor().right();
					break;
					
				default:
					break;
			}
		}
		catch (Exception e1) {
			System.out.println("Caught exception: " + e1.getMessage());
			throw e1;
		}
	}
	
	private void handleDelete()
	{
		line.deleteCharAt(cursor.getCurrentPos() - 1);

		cursor.left();
		writer.print(' ');
		writer.flush();
		cursor.left();

		if (cursor.getEolPos() > 0) {
			cursor.setEolPos(cursor.getEolPos() - 1);
		}
	}
	
	private void handleInsert(char ch)
	{
		if (cursor.getCurrentPos() == cursor.getEolPos()) {
			line.append(ch);
		}
		else {
			line.insert(cursor.getCurrentPos() + 1, ch);
		}
		
		cursor.typeChar(ch);
	}
}
