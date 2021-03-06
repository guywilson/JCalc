package com.guy.calc.util;

import com.guy.calc.token.Token;
import com.guy.calc.type.Base;
import com.guy.log.Logger;

public class CalcTokenizer
{
	private int startIndex;
	private int endIndex;
	private int expressionLen;
	private String _expression;

	private Base _base;

	private Logger log = new Logger(CalcTokenizer.class);
	
	private int _findNextTokenPos()
	{
		int		i;
		char	ch;
		boolean	lookingForWhiteSpace = true;
		int		tokenLen = 0;

		log.entry();
		
		try {
			for (i = startIndex;i < (int)expressionLen;i++) {
				ch = _expression.charAt(i);

				if (lookingForWhiteSpace) {
					if (CharUtils.isWhiteSpace(ch)) {
						startIndex++;
						continue;
					}
					else {
						lookingForWhiteSpace = false;
					}
				}

				if (CharUtils.isWhiteSpace(ch)) {
					return i;
				}
				if (CharUtils.isToken(ch)) {
					/*
					** Do we have a token on it's own, or is it a delimiter...
					*/
					if (tokenLen > 0) {
						// The token is the delimiter to another token...
						return i;
					}
					else {
						/*
						** If this is the '-' character and if the next char is a digit (0-9)
						** and the previous char is not a ')' or a digit then this must be a -ve number,
						** not the '-' operator...
						*/
						if (ch == '-' && CharUtils.isDigit(_expression.charAt(i + 1))) {
							boolean isNegativeOperand = false;

							if (i > 0) {
								boolean isPreviousCharBrace = Token.isBraceRight(_expression.charAt(i - 1));
								boolean isPreviousCharDigit = CharUtils.isDigit(_expression.charAt(i - 1));

								if (!isPreviousCharBrace && !isPreviousCharDigit) {
									isNegativeOperand = true;
								}
								else {
									isNegativeOperand = false;
								}
							}
							else if (i == 0) {
								// We're at the beginning of the expression, must be
								// a -ve operand
								isNegativeOperand = true;
							}

							if (isNegativeOperand) {
								// Found a -ve number...
								continue;
							}
							else {
								return i + 1;
							}
						}
						else {
							// The token is the token we want to return...
							return i + 1;
						}
					}
				}

				tokenLen++;

				/*
				** If we haven't returned yet and we're at the end of
				** the expression, we must have an operand at the end
				** of the expression...
				*/
				if (i == (int)(expressionLen - 1)) {
					return i + 1;
				}
			}
		}
		catch (Exception e) {
			log.error("Error", e);
			log.trace(e);
		}
		catch (Throwable th) {
			log.fatal("Fatal error", th);
			log.trace(th);
		}
		finally {
			log.exit();
		}

		return -1;
	}

	public CalcTokenizer(String expression, Base b)
	{
		log.entry();
		
		try {
			this._expression = expression;
			startIndex = 0;
			endIndex = 0;
			expressionLen = expression.length();
		    this._base = b;
		}
		catch (Throwable th) {
			log.error("Error in constructor", th);
			log.trace(th);
		}
		finally {
			log.exit();
		}
	}

	public boolean hasMoreTokens()
	{
		log.entry();
		
		try {
			int pos = _findNextTokenPos();

			if (pos > 0) {
				endIndex = pos;
				return true;
			}
		}
		catch (Throwable th) {
			log.error("Error", th);
			log.trace(th);
		}
		finally {
			log.exit();
		}

		return false;
	}
		
	public Token nextToken() throws Exception
	{
		String		token;
		Token		t = null;

		log.entry();
		
		try {
			token = _expression.substring(startIndex, endIndex);

			startIndex = endIndex;

			t = TokenFactory.createToken(token, this._base);
		}
		catch (Throwable e) {
			log.error("Error getting next token", e);
			throw e;
		}
		finally {
			log.exit();
		}

		return t;
	}
}
