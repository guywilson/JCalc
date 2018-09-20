package com.guy.calc.token;

import com.guy.calc.CalcSystem;
import com.guy.calc.util.CharUtils;
import com.guy.calc.util.DebugHelper;

public abstract class Token
{
	private String _token;
	private String _className;
        
    private static boolean isDecDigit(char digit)
    {
    	return (CharUtils.isDigit(digit) || digit == '.' || digit == '-');
    }
    private static boolean isHexDigit(char digit)
    {
    	return (CharUtils.isHexDigit(Character.toUpperCase(digit)));
    }
    private static boolean isBinDigit(char digit)
    {
    	return (digit == '0' || digit == '1');
    }
    private static boolean isOctDigit(char digit)
    {
    	return (CharUtils.isOctDigit(digit));
    }

    protected Token() {}
    
	protected Token(String token, String className)
	{
		this(token);
		
		DebugHelper dbg = DebugHelper.getInstance();

		if (dbg.isDebugOn()) {
			System.out.println("Creating Token of type " + className + " with token '" + token + "'");
		}
		
		setClass(className);
	}

	protected void setClass(String className)
	{
		this._className = className;
	}

	public Token(String token)
	{
		this._token = token;
	}

	public String getToken()
	{
		return this._token;
	}
	public String getClassName()
	{
		return this._className;
	}
        
	public Boolean isOperand() {
		return false;
	}
	public Boolean isOperator() {
		return false;
	}
	public Boolean isBrace() {
		return false;
	}
	public Boolean isConstant() {
		return false;
	}
	public Boolean isFunction() {
		return false;
	}

	public static Boolean isOperand(String token)
	{
		int		i;
		boolean	ret = true;
		int	tokenLength = token.length();

		if (token.charAt(0) == '-' && tokenLength == 1) {
			// Must be the '-' operator...
			ret = false;
		}
		else {
			for (i = 0;i < tokenLength;i++) {
	            switch (CalcSystem.getInstance().getBase()) {
	                case Dec:
	                    if (!isDecDigit(token.charAt(i))) {
	                        ret = false;
	                        break;
	                    }
	                    break;
	                    
	                case Hex:
	                    if (!isHexDigit(token.charAt(i))) {
	                        ret = false;
	                        break;
	                    }
	                    break;
	                    
	                case Bin:
	                    if (!isBinDigit(token.charAt(i))) {
	                        ret = false;
	                        break;
	                    }
	                    break;
	                    
	                case Oct:
	                    if (!isOctDigit(token.charAt(i))) {
	                        ret = false;
	                        break;
	                    }
	                    break;
	            }
	            
				if (!ret) {
					break;
				}
			}
		}

		return ret;
	}
	
	public static Boolean isOperatorPlus(String token) {
		return token.charAt(0) == '+';
	}
	
	public static Boolean isOperatorMinus(String token)
	{
		boolean	ret = true;
		
		int	tokenLength = token.length();

		if (token.charAt(0) == '-') {
			if (tokenLength > 1 && CharUtils.isDigit(token.charAt(1))) {
				ret = false;
			}
		}
		else {
			ret = false;
		}

		return ret;
	}
	
	public static Boolean isOperatorMultiply(String token) {
		return token.charAt(0) == '*';
	}
	public static Boolean isOperatorDivide(String token) {
		return token.charAt(0) == '/';
	}
	public static Boolean isOperatorPower(String token) {
		return token.charAt(0) == '^';
	}
	public static Boolean isOperatorMod(String token) {
		return token.charAt(0) == '%';
	}
	public static Boolean isOperatorAND(String token) {
		return token.charAt(0) == '&';
	}
	public static Boolean isOperatorOR(String token) {
		return token.charAt(0) == '|';
	}
	public static Boolean isOperatorXOR(String token) {
		return token.charAt(0) == '~';
	}
	
	public static Boolean isOperator(String token)
	{
		return (
				isOperatorPlus(token) ||
				isOperatorMinus(token) ||
				isOperatorMultiply(token) ||
				isOperatorDivide(token) ||
				isOperatorPower(token)) ||
				isOperatorMod(token) ||
				isOperatorAND(token) ||
				isOperatorOR(token) ||
				isOperatorXOR(token);
	}
	
	public static Boolean isBraceLeft(char ch) {
		return (ch == '(' || ch == '[' || ch == '{');
	}
	public static Boolean isBraceRight(char ch) {
		return (ch == ')' || ch == ']' || ch == '}');
	}
	public static Boolean isBrace(char ch) {
		return (isBraceLeft(ch) || isBraceRight(ch));
	}
	public static Boolean isBraceLeft(String token) {
		char ch0 = token.charAt(0);
		return (isBraceLeft(ch0));
	}
	public static Boolean isBraceRight(String token) {
		char ch0 = token.charAt(0);
		return (isBraceRight(ch0));
	}
	public static Boolean isBrace(String token) {
		return (isBraceLeft(token) || isBraceRight(token));
	}
	public static Boolean isConstantPi(String token) {
		return (token.startsWith("pi"));
	}
	public static Boolean isConstantC(String token) {
		return (token.charAt(0) == 'c' && token.length() == 1);
	}
	public static Boolean isConstant(String token) {
		return (isConstantPi(token) || isConstantC(token));
	}
	public static Boolean isFunctionSine(String token) {
		return (token.startsWith("sin"));
	}
	public static Boolean isFunctionCosine(String token) {
		return (token.startsWith("cos"));
	}
	public static Boolean isFunctionTangent(String token) {
		return (token.startsWith("tan"));
	}
	public static Boolean isFunctionArcSine(String token) {
		return (token.startsWith("asin"));
	}
	public static Boolean isFunctionArcCosine(String token) {
		return (token.startsWith("acos"));
	}
	public static Boolean isFunctionArcTangent(String token) {
		return (token.startsWith("atan"));
	}
	public static Boolean isFunctionSquareRoot(String token) {
		return (token.startsWith("sqrt"));
	}
	public static Boolean isFunctionLogarithm(String token) {
		return (token.startsWith("log"));
	}
	public static Boolean isFunctionNaturalLog(String token) {
		return (token.startsWith("ln"));
	}
	public static Boolean isFunctionFactorial(String token) {
		return (token.startsWith("fact"));
	}
	public static Boolean isFunctionMemory(String token) {
		return (token.startsWith("mem"));
	}
	public static Boolean isFunction(String token) {
		return (
				isFunctionSine(token) ||
				isFunctionCosine(token) ||
				isFunctionTangent(token) ||
				isFunctionArcSine(token) ||
				isFunctionArcCosine(token) ||
				isFunctionArcTangent(token) ||
				isFunctionSquareRoot(token) ||
				isFunctionLogarithm(token) ||
				isFunctionNaturalLog(token) ||
				isFunctionFactorial(token) ||
				isFunctionMemory(token));
	}
}
