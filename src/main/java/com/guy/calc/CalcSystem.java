package com.guy.calc;

import java.math.MathContext;
import java.math.RoundingMode;

import com.guy.calc.token.Operand;
import com.guy.calc.type.Base;
import com.guy.log.Logger;

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
	
	private Logger log = new Logger(this.getClass());
	
	private static CalcSystem instance = null;
	
	private CalcSystem()
	{
		log.entry();
		
		try {
			this.mc = new MathContext(MAX_DISPLAY_PRECISION, RoundingMode.HALF_UP);
			this.base = Base.Dec;
			this.scale = DEFAULT_SCALE;
		}
		catch (Exception e) {
			log.error("Error in constructor", e);
			log.stack();
		}
		catch (Throwable th) {
			log.fatal("Error in constructor", th);
			log.stack();
		}
		finally {
			log.exit();
		}
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
		Operand op = null;
		
		log.entry();
		
		try {
			if (this.memory[index] == null) {
				this.memory[index] = new Operand(0.0);
			}
			
			op = this.memory[index];
		}
		catch (Exception e) {
			log.error("Error retrieving memory [" + index + "]", e);
			log.trace(e);
		}
		catch (Throwable th) {
			log.fatal("Error retrieving memory [" + index + "]", th);
			log.trace(th);
		}
		finally {
			log.exit();
		}
		
		return op;
	}
	
	public void setMemoryValueAt(int index, Operand value)
	{
		log.entry();
		
		try {
			this.memory[index] = value;
		}
		catch (Exception e) {
			log.error("Error storing memory [" + index + "]", e);
			log.trace(e);
		}
		catch (Throwable th) {
			log.fatal("Error storing memory [" + index + "]", th);
			log.trace(th);
		}
		finally {
			log.exit();
		}
	}
    
	public String getModeStr()
	{
	    String mode = null;
	    
		log.entry();
		
		try {
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
		            
		        default:
		        	throw new Exception("Got invalid base");
		    }
		}
		catch (Exception e) {
			log.error("Error getting mode string", e);
			log.trace(e);
		}
		finally {
			log.exit();
		}
	    
	    return mode;
	}
}
