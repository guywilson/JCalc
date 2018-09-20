package com.guy.calc.token;

import java.math.BigDecimal;

import com.guy.calc.type.Const;

public class Constant extends Operand
{
	private Const constant;

	private static BigDecimal _pi()
	{
		BigDecimal pi = BigDecimal.valueOf(Math.PI);
		
		return pi;
	}

	public Constant(String token)
	{
		super(constToStrValue(token));
	}

	public Const getConstant()
	{
		return constant;
	}

	public Boolean isConstant() {
		return true;
	}
                        
    public static String constToStrValue(String constant)
    {
    	String value = null;
    	
    	if (Token.isConstantPi(constant)) {
    		value = _pi().toString();
    	}
    	else if (Token.isConstantC(constant)) {
    		value = "299792458";
    	}
    	
    	return value;
    }
}
