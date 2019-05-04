package com.guy.calc.util.console;

import java.io.PrintWriter;
import java.util.LinkedList;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import com.guy.calc.CalcSystem;
import com.guy.log.Logger;

public class CalcTerminal
{
	public final static byte CHAR_ESCAPE 			= 0x1B;
	public final static byte CHAR_CSI 				= 0x5B;		// '['
	public final static byte CHAR_UP 				= 0x41;		// 'A'
	public final static byte CHAR_DOWN 				= 0x42;		// 'B'
	public final static byte CHAR_LEFT 				= 0x44;		// 'D'
	public final static byte CHAR_RIGHT 			= 0x43;		// 'C'
	public final static byte CHAR_BACKSPACE 		= 0x08;
	public final static byte CHAR_DELETE 			= 0x7F;
	public final static byte CHAR_NEWLINE 			= 0x0A;
	public final static byte CHAR_CARRIAGERETURN	= 0x0D;
	public final static byte CHAR_ERASEINLINE 		= 0x4B;		// 'K'
	
	private Terminal terminal = null;
	private NonBlockingReader reader;
	private PrintWriter writer;
	
	private StringBuffer userInput = null;
	
	private LinkedList<String> commandList = null;
	private int currentCommandPos = 0;
	
	private Logger log = new Logger(this.getClass());
	
	private Cursor cursor;
	
	private static CalcTerminal instance = null;
	
	protected class Cursor
	{
		private int 		currentPos;
		private int			eolPos;
		
		private Cursor() {
			currentPos = 0;
			eolPos = 0;
		}

		public void left() throws Exception
		{
			log.entry();
			
			try {
				left(true);
			}
			catch (Exception e) {
				log.error("Error moving cursor left", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error moving cursor left", th);
				log.trace(th);
				throw new Exception("Failed to move cursor");
			}
			finally {
				log.exit();
			}
		}
		
		public void left(boolean updatePos) throws Exception
		{
			log.entry();
			
			try {
				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
				
				if (updatePos) {
					currentPos--;

					if (currentPos < 0) {
						currentPos = 0;
						return;
					}
				}
				
				writer.write(CalcTerminal.CHAR_ESCAPE);
				writer.write(CalcTerminal.CHAR_CSI);
				writer.write(CalcTerminal.CHAR_LEFT);
				writer.flush();

				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
			}
			catch (Exception e) {
				log.error("Error moving cursor left", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error moving cursor left", th);
				log.trace(th);
				throw new Exception("Failed to move cursor");
			}
			finally {
				log.exit();
			}
		}
		
		public void left(boolean updatePos, int n) throws Exception
		{
			int i = 0;
			
			if (n < 0) {
				n = 0;
			}
			
			log.debug("Moving cursor left [" + n + "] spaces");
			
			for (i = 0;i < n;i++) {
				left(updatePos);
			}
		}
		
		public void right() throws Exception
		{
			log.entry();
			
			try {
				right(true);
			}
			catch (Exception e) {
				log.error("Error moving cursor right", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error moving cursor right", th);
				log.trace(th);
				throw new Exception("Failed to move cursor");
			}
			finally {
				log.exit();
			}
		}
		
		public void right(boolean updatePos) throws Exception
		{
			log.entry();
			
			try {
				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");

				if (updatePos) {
					currentPos++;

					if (currentPos > eolPos) {
						currentPos = eolPos;
						return;
					}
				}
				
				writer.write(CalcTerminal.CHAR_ESCAPE);
				writer.write(CalcTerminal.CHAR_CSI);
				writer.write(CalcTerminal.CHAR_RIGHT);
				writer.flush();

				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
			}
			catch (Exception e) {
				log.error("Error moving cursor right", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error moving cursor right", th);
				log.trace(th);
				throw new Exception("Failed to move cursor");
			}
			finally {
				log.exit();
			}
		}
		
		public void right(boolean updatePos, int n) throws Exception
		{
			int i = 0;
			
			if (n < 0) {
				n = 0;
			}
			
			for (i = 0;i < n;i++) {
				right(updatePos);
			}
		}
		
		public void clearLine() throws Exception
		{
			log.entry();
			
			try {
				while (currentPos > 0) {
					deleteChar();
				}
			}
			catch (Exception e) {
				log.error("Error clearing line", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error clearing line", th);
				log.trace(th);
				throw new Exception("Failed to clear line");
			}
			finally {
				log.exit();
			}
		}
		
		public void typeChar(char ch) throws Exception
		{
			log.entry();
			
			try {
				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
				log.debug("Printing char [" + ch + "]");
				
				writer.print(ch);
				writer.flush();
				
				currentPos++;
				eolPos++;

				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
			}
			catch (Exception e) {
				log.error("Error wrting char", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error writing char", th);
				log.trace(th);
				throw new Exception("Failed to write char");
			}
			finally {
				log.exit();
			}
		}
		
		public void typeString(String s) throws Exception
		{
			log.entry();
			
			try {
				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
				log.debug("Printing string [" + s + "]");
				
				writer.print(s);
				writer.flush();
				
				currentPos += s.length();
				eolPos += s.length();

				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
			}
			catch (Exception e) {
				log.error("Error wrting string", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error writing string", th);
				log.trace(th);
				throw new Exception("Failed to write string");
			}
			finally {
				log.exit();
			}
		}
		
		public void deleteChar() throws Exception
		{
			log.entry();
			
			try {
				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");

				left();
				writer.print(' ');
				writer.flush();
				left(false);

				if (eolPos > 0) {
					eolPos--;
				}

				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");
			}
			catch (Exception e) {
				log.error("Error deleting char", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error deleting char", th);
				log.trace(th);
				throw new Exception("Failed to delete char");
			}
			finally {
				log.exit();
			}
		}
		
		public void newLine() throws Exception
		{
			log.entry();
			
			try {
				log.debug("cur [" + currentPos + "], eol [" + eolPos + "]");

				writer.println();
				writer.flush();
				
				currentPos = 0;
				eolPos = 0;
			}
			catch (Exception e) {
				log.error("Error issuing new line", e);
				throw e;
			}
			catch (Throwable th) {
				log.fatal("Error issuing new line", th);
				log.trace(th);
				throw new Exception("Failed to issue new line");
			}
			finally {
				log.exit();
			}
		}
	}
	
	private CalcTerminal()
	{
		log.entry();
		
		try {
			terminal = TerminalBuilder.builder().jna(true).system(true).build();
			terminal.enterRawMode();

			reader = terminal.reader();
			writer = terminal.writer();
			
			userInput = new StringBuffer();
			
			commandList = new LinkedList<>();
			
			cursor = new Cursor();
		}
		catch (Exception e) {
			log.error("Error initialising", e);
		}
		catch (Throwable th) {
			log.fatal("Error initialising", th);
			log.trace(th);
		}
		finally {
			log.exit();
		}
	}
	
	public static CalcTerminal getInstance()
	{
		if (instance == null) {
			instance = new CalcTerminal();
		}
		
		return instance;
	}
	
	private void addCommand(String cmd) throws Exception
	{
		log.entry();
		
		try {
			log.debug("Adding cmd : " + cmd);
			commandList.addFirst(cmd);
		}
		catch (Exception e) {
			log.error("Error adding new command", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error adding new command", th);
			log.trace(th);
			throw new Exception("Failed to add new line");
		}
		finally {
			log.exit();
		}
	}
	
	private String getNextCommand() throws Exception
	{
		String cmd;
		
		log.entry();
		
		try {
			if (commandList.size() == 0) {
				return "";
			}
			
			if (currentCommandPos < 0) {
				currentCommandPos = 0;
			}
			else if (currentCommandPos >= commandList.size()) {
				currentCommandPos = commandList.size() - 1;
			}
			
			cmd = commandList.get(currentCommandPos++);
			
			log.debug("Got command : " + cmd);
		}
		catch (Exception e) {
			log.error("Error getting next command", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error getting next command", th);
			log.trace(th);
			throw new Exception("Failed to get next command");
		}
		finally {
			log.exit();
		}
		
		return cmd;
	}
	
	private String getPreviousCommand() throws Exception
	{
		String cmd;
		
		log.entry();
		
		try {
			if (commandList.size() == 0) {
				return "";
			}
			
			if (currentCommandPos < 0) {
				currentCommandPos = 0;
			}
			else if (currentCommandPos >= commandList.size()) {
				currentCommandPos = commandList.size() - 1;
			}
			
			cmd = commandList.get(currentCommandPos--);
			
			log.debug("Got command : " + cmd);
		}
		catch (Exception e) {
			log.error("Error getting previous command", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error getting previous command", th);
			log.trace(th);
			throw new Exception("Failed to get previous command");
		}
		finally {
			log.exit();
		}
		
		return cmd;
	}
	
	public void print(char ch) throws Exception
	{
		log.entry();
		
		try {
			cursor.typeChar(ch);
		}
		catch (Exception e) {
			log.error("Error printing char", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error prnting char", th);
			log.trace(th);
			throw new Exception("Failed to print char");
		}
		finally {
			log.exit();
		}
	}
	
	public void print(String str) throws Exception
	{
		log.entry();
		
		try {
			cursor.typeString(str);
		}
		catch (Exception e) {
			log.error("Error printing string", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error prnting string", th);
			log.trace(th);
			throw new Exception("Failed to print string");
		}
		finally {
			log.exit();
		}
	}
	
	public char readch() throws Exception
	{
		char		ch = 0x00;
		
		log.entry();
		
		try {
			ch = (char)reader.read();
		}
		catch (Exception e) {
			log.error("Error reading char", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error reading char", th);
			log.trace(th);
			throw new Exception("Failed to read char");
		}
		finally {
			log.exit();
		}
		
		return ch;
	}

	public String readLine() throws Exception
	{
		String			ln = null;
		char			ch = 0;
		boolean			go = true;
		
		log.entry();
		
		try {
			prompt();
			
			while (go) {
				ch = readch();
	
				switch (ch) {
					case CalcTerminal.CHAR_ESCAPE:
						log.debug("Read escape char");
						handleEscape();
						break;
					
					case CalcTerminal.CHAR_CARRIAGERETURN:
					case CalcTerminal.CHAR_NEWLINE:
						log.debug("Read new line char");
						ln = userInput.toString();
						userInput.setLength(0);
						addCommand(ln);
						cursor.newLine();
						go = false;
						break;
						
					case CalcTerminal.CHAR_DELETE:
						log.debug("Read delete char");
						handleDelete();
						break;
						
					default:
						log.debug("Read char [" + ch + "]");
						handleInsert((char)ch);
						break;
				}
			}
		}
		catch (Exception e) {
			log.error("Error reading line", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error reading line", th);
			log.trace(th);
			throw new Exception("Failed to read line");
		}
		finally {
			log.exit();
		}
		
		return ln;
	}

	public void prompt() throws Exception
	{
		log.entry();
		
		try {
			String prompt = "calc [" + CalcSystem.getInstance().getModeStr() + "]> ";
			
			log.debug("Writing prompt : " + prompt);
			
			writer.print(prompt);
			writer.flush();
		}
		catch (Exception e) {
			log.error("Error writing prompt", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error writing prompt", th);
			log.trace(th);
			throw new Exception("Failed to write prompt");
		}
		finally {
			log.exit();
		}
	}

	
	private void handleEscape() throws Exception
	{
		char	ch = 0;
		
		log.entry();
		
		try {
			ch = readch();
			
			switch (ch) {
				case CalcTerminal.CHAR_CSI:
					log.debug("Handling CSI char");
					handleCSI();
					break;
					
				default:
					throw new Exception("Expecting CSI char but read [" + ch + "]");
			}
		}
		catch (Exception e) {
			log.error("Error handling char", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error handling char", th);
			log.trace(th);
			throw new Exception("Failed to handle char");
		}
		finally {
			log.exit();
		}
	}
	
	private void handleCSI() throws Exception
	{
		String	cmd = null;
		char	ch = 0;
		
		log.entry();
		
		try {
			ch = readch();
			
			log.debug("Read char [" + ch + "]");

			switch (ch) {
				case CalcTerminal.CHAR_UP:
					log.debug("Read UP char");
					cmd = getNextCommand();

					cursor.clearLine();
					print(cmd);
					
					userInput.setLength(0);
					userInput.append(cmd);
					break;
					
				case CalcTerminal.CHAR_DOWN:
					log.debug("Read DOWN char");
					cmd = getPreviousCommand();

					cursor.clearLine();
					print(cmd);
					
					userInput.setLength(0);
					userInput.append(cmd);
					break;
					
				case CalcTerminal.CHAR_LEFT:
					log.debug("Read LEFT char");
					cursor.left();
					break;
					
				case CalcTerminal.CHAR_RIGHT:
					log.debug("Read RIGHT char");
					cursor.right();
					break;
					
				default:
					throw new Exception("Read unexpected character [" + ch + "]");
			}
		}
		catch (Exception e) {
			log.error("Error handling char", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error handling char", th);
			log.trace(th);
			throw new Exception("Failed to handle char");
		}
		finally {
			log.exit();
		}
	}
	
	private void handleDelete() throws Exception
	{
		int		cursorLeftCount = 0;
		
		log.entry();
		
		try {
			userInput.deleteCharAt(cursor.currentPos - 1);
			cursor.deleteChar();
			
			log.debug("Writing substring [" + userInput.substring(cursor.currentPos) + "]");

			while (cursor.currentPos < cursor.eolPos) {
				writer.print(userInput.charAt(cursor.currentPos));
				writer.flush();
				
				cursor.currentPos++;
				cursorLeftCount++;
			}
			
			writer.print(' ');
			writer.flush();
			cursorLeftCount++;
			
			cursor.left(true, cursorLeftCount);
		}
		catch (Exception e) {
			log.error("Error deleting char", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error deleting char", th);
			log.trace(th);
			throw new Exception("Failed to delete char");
		}
		finally {
			log.exit();
		}
	}
	
	private void handleInsert(char ch) throws Exception
	{
		int		cursorLeftCount = 0;
		
		log.entry();
		
		try {
			if (cursor.currentPos == cursor.eolPos) {
				userInput.append(ch);
				cursor.typeChar(ch);
			}
			else {
				userInput.insert(cursor.currentPos + 1, ch);
				cursor.typeChar(ch);
				
				log.debug("Writing substring [" + userInput.substring(cursor.currentPos + 1) + "]");

				while (cursor.currentPos + 1 < cursor.eolPos) {
					writer.print(userInput.charAt(cursor.currentPos + 1));
					writer.flush();
					
					cursor.currentPos++;
					cursorLeftCount++;
				}
				
				cursor.left(true, cursorLeftCount);
			}
		}
		catch (Exception e) {
			log.error("Error inserting char", e);
			throw e;
		}
		catch (Throwable th) {
			log.fatal("Error inserting char", th);
			log.trace(th);
			throw new Exception("Failed to insert char");
		}
		finally {
			log.exit();
		}
	}
}
