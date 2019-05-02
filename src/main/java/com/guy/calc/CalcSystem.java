package com.guy.calc;

import java.math.MathContext;
import java.math.RoundingMode;

import com.guy.calc.token.Operand;
import com.guy.calc.type.Base;
import com.guy.calc.util.console.CalcTerminal;
import com.guy.calc.util.console.LineReader;

public class CalcSystem
{
	public final static int MAX_DISPLAY_PRECISION = 64;
	public final static int MAX_DISPLAY_SCALE = 32;
	public final static int DEFAULT_SCALE = 2;
	public final static int NUM_MEMORY_SLOTS = 10;
	
	private Operand memory[] = new Operand[NUM_MEMORY_SLOTS];
	private MathContext mc = null;
	private Base base = Base.Dec;
	private int scale = MAX_DISPLAY_SCALE;
	
	private LineReader reader = null;
	
	private static CalcSystem instance = null;
	
	private CalcSystem()
	{
		this.mc = new MathContext(MAX_DISPLAY_PRECISION, RoundingMode.HALF_UP);
		this.base = Base.Dec;
		this.scale = DEFAULT_SCALE;
		
		this.reader = new LineReader();
	}
	
	public static CalcSystem getInstance()
	{
		if (instance == null) {
			instance = new CalcSystem();
		}
		
		return instance;
	}
	
	public MathContext getMathContext() {
		return this.mc;
	}
	
	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public void setBase(Base b) {
		this.base = b;
	}
	
	public Base getBase() {
		return this.base;
	}
	
	public Operand getMemoryValueAt(int index)
	{
		if (this.memory[index] == null) {
			this.memory[index] = new Operand(0.0);
		}
		
		return this.memory[index];
	}
	
	public void setMemoryValueAt(int index, Operand value) {
		this.memory[index] = value;
	}
    
	public String getModeStr()
	{
	    String mode = null;
	    
	    switch (getBase()) {
	        case Dec:
	            mode = "DEC";
	            break;
	            
	        case Hex:
	            mode = "HEX";
	            break;
	            
	        case Bin:
	            mode = "BIN";
	            break;
	            
	        case Oct:
	            mode = "OCT";
	            break;
	    }
	    
	    return mode;
	}
	
	public String getUserInput() throws Exception
	{
		String input = null;
		
	    try {
			CalcTerminal.getInstance().prompt();
			
			input = reader.readLine();
		}
	    catch (Exception e) {
			throw new Exception("Caught Exception - " + e.getMessage());
		}
	    
	    return input;
	}
}
