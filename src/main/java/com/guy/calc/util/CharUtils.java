package com.guy.calc.util;

public class CharUtils
{
	private final static String decDigits = "0123456789";
	private final static String hexDigits = "0123456789ABCDEF";
	private final static String octDigits = "01234567";
	private final static String	whiteSpace = " \t\n\r";
	private final static String	tokens = " \t\n\r+-*/^%&|~()[]{}";
	private final static String validChars = "abcdefghijklmnopqrstuvwxyz" + decDigits + whiteSpace + tokens;

	public static boolean isToken(char ch) {
		return (tokens.indexOf(ch) >= 0);
	}
		
	public static boolean isWhiteSpace(char ch) {
		return (whiteSpace.indexOf(ch) >= 0);
	}
	
	public static boolean isDigit(char ch) {
		return (decDigits.indexOf(ch) >= 0);
	}
	
	public static boolean isHexDigit(char ch) {
		return (hexDigits.indexOf(ch) >= 0);
	}
	
	public static boolean isOctDigit(char ch) {
		return (octDigits.indexOf(ch) >= 0);
	}
	
	public static boolean isValid(char ch) {
		return (validChars.indexOf(ch) >= 0);
	}
}
