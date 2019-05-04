package com.guy.calc.util;

import com.guy.calc.token.Brace;
import com.guy.calc.token.Constant;
import com.guy.calc.token.Function;
import com.guy.calc.token.Operand;
import com.guy.calc.token.Operator;
import com.guy.calc.token.Token;
import com.guy.calc.type.Base;
import com.guy.log.Logger;

public class TokenFactory
{
	public static Token createToken(String token, Base b) throws Exception
	{
		Logger log = new Logger(TokenFactory.class);
		
		Token t = null;

		log.entry();
		
		try {
			if (Token.isOperand(token)) {
				log.debug("Found Operand [" + token + "]");
				t = new Operand(token);
			}
			else if (Token.isOperator(token)) {
				log.debug("Found Operator [" + token + "]");
				t = new Operator(token);
			}
			else if (Token.isBrace(token)) {
				log.debug("Found Brace [" + token + "]");
				t = new Brace(token);
			}
			else if (Token.isConstant(token)) {
				log.debug("Found Constant [" + token + "]");
				t = new Constant(token);
			}
			else if (Token.isFunction(token)) {
				log.debug("Found Function [" + token + "]");
				t = new Function(token);
			}
			else {
				throw new Exception("Invalid token found");
			}
		}
		catch (Exception e) {
			log.error("Error creating token from [" + token + "]", e);
		}
		finally {
			log.exit();
		}

		return t;
	}
}
