package com.guy.calc.token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import com.guy.calc.CalcSystem;
import com.guy.calc.type.Base;

public class Operand extends Token
{
	private BigDecimal _decimalValue = null;
	private BigInteger _integerValue = null;
	
	private MathContext mc = CalcSystem.getInstance().getMathContext();
	
	protected void setValue(BigDecimal x) {
		_decimalValue = x;
	}
    
	protected void setValue(BigInteger x) {
		_integerValue = x;
	}

	public Operand(String token, String className) {
		super(token, className);
	}


	public Operand(Operand src)
	{
		super(src.getClassName());
		
		if (src.isDecimal()) {
			setValue(src.getDoubleValue());
		}
		if (src.isInteger()) {
			setValue(src.getIntValue());
		}
	}
	
	public Operand(String token)
	{
		super(token, "Operand");
		
		switch (CalcSystem.getInstance().getBase()) {
			case Dec:
				setValue(new BigDecimal(token, mc));
				break;
				
			case Hex:
				setValue(new BigInteger(token, 16));
				break;
				
			case Oct:
				setValue(new BigInteger(token, 8));
				break;
				
			case Bin:
				setValue(new BigInteger(token, 2));
		}
	}
	
	public Operand(BigDecimal x) {
		setValue(x);
	}
	public Operand(BigInteger x) {
		setValue(x);
	}
    public Operand(double d) {
    	setValue(BigDecimal.valueOf(d));
    }
    public Operand(int i) {
    	setValue(BigInteger.valueOf(i));
    }
    public Operand(long l) {
    	setValue(BigInteger.valueOf(l));
    }

	public Operand add(Operand rhs) throws Exception
	{
		Operand o = null;
		
		if (CalcSystem.getInstance().getBase() == Base.Dec && isDecimal()) {
			o = new Operand(this._decimalValue.add(rhs.getDoubleValue(), mc));
		}
		else {
			o = new Operand(this._integerValue.add(rhs.getIntValue()));
		}
		
		return o;
	}
	public Operand subtract(Operand rhs)
	{
		Operand o = null;
		
		if (CalcSystem.getInstance().getBase() == Base.Dec && isDecimal()) {
			o = new Operand(this._decimalValue.subtract(rhs.getDoubleValue(), mc));
		}
		else {
			o = new Operand(this._integerValue.subtract(rhs.getIntValue()));
		}
		
		return o;
	}
	public Operand multiply(Operand rhs)
	{
		Operand o = null;
		
		if (CalcSystem.getInstance().getBase() == Base.Dec && isDecimal()) {
			o = new Operand(this._decimalValue.multiply(rhs.getDoubleValue(), mc));
		}
		else {
			o = new Operand(this._integerValue.multiply(rhs.getIntValue()));
		}
		
		return o;
	}
	public Operand divide(Operand rhs)
	{
		Operand o = null;
		
		if (CalcSystem.getInstance().getBase() == Base.Dec && isDecimal()) {
			o = new Operand(this._decimalValue.divide(rhs.getDoubleValue(), mc));
		}
		else {
			o = new Operand(this._integerValue.divide(rhs.getIntValue()));
		}
		
		return o;
	}
	public Operand mod(Operand rhs)
	{
		Operand o = null;
		
		if (CalcSystem.getInstance().getBase() == Base.Dec && isDecimal()) {
			o = new Operand(this._decimalValue.remainder(rhs.getDoubleValue(), mc));
		}
		else {
			o = new Operand(this._integerValue.remainder(rhs.getIntValue()));
		}
		
		return o;
	}
	public Operand pow(Operand rhs)
	{
		Operand o = null;
		
		if (CalcSystem.getInstance().getBase() == Base.Dec && isDecimal()) {
			o = new Operand(this._decimalValue.pow(rhs.getIntValue().intValue(), mc));
		}
		else {
			o = new Operand(this._integerValue.pow(rhs.getIntValue().intValue()));
		}
		
		return o;
	}
	public Operand and(Operand rhs)
	{
		BigInteger bi = getIntValue();
		Operand o = new Operand(bi.and(rhs.getIntValue()));
		
		return o;
	}
	public Operand or(Operand rhs)
	{
		BigInteger bi = getIntValue();
		Operand o = new Operand(bi.or(rhs.getIntValue()));
		
		return o;
	}
	public Operand xor(Operand rhs)
	{
		BigInteger bi = getIntValue();
		Operand o = new Operand(bi.xor(rhs.getIntValue()));
		
		return o;
	}
    
	public boolean isDecimal() {
		return (_decimalValue != null);
	}
    
	public boolean isInteger() {
		return (_integerValue != null);
	}

	public BigDecimal getDoubleValue()
	{
		if (this._decimalValue == null) {
			this._decimalValue = new BigDecimal(this._integerValue);
		}
		
		return this._decimalValue;
	}
        
	public static BigDecimal getDoubleValue(BigDecimal value)
	{
		return value;
	}
	
	public BigInteger getIntValue()
	{
		if (this._integerValue == null) {
			this._integerValue = BigInteger.valueOf(this._decimalValue.intValue());
		}
		
		return this._integerValue;
	}
		
	public static BigInteger getIntValue(BigInteger value)
	{
		return value;
	}

	public Boolean isOperand() {
		return true;
	}

    public String toString(Base b)
    {
		String s = null;
		
		switch (b) {
			case Dec:
				if (isDecimal()) {
					s = getDoubleValue().setScale(CalcSystem.getInstance().getScale(), RoundingMode.HALF_UP).toPlainString();
				}
				else if (isInteger()) {
					s = getIntValue().toString();
				}
				break;
				
			case Hex:
				s = getIntValue().toString(16).toUpperCase();
				break;
				
			case Oct:
				s = getIntValue().toString(8);
				break;
				
			case Bin:
				s = getIntValue().toString(2);
				break;
		}
		
    	return s;
    }
        
    public static String toString(BigDecimal value, Base b)
    {
		String s = null;
		
		switch (b) {
			case Dec:
				s = value.toString();
				break;
				
			case Hex:
				s = value.toBigInteger().toString(16).toUpperCase();
				break;
				
			case Oct:
				s = value.toBigInteger().toString(8);
				break;
				
			case Bin:
				s = value.toBigInteger().toString(2);
				break;
		}
		
    	return s;
    }
}
