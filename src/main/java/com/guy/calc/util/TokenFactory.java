package com.guy.calc.util;

import com.guy.calc.token.Brace;
import com.guy.calc.token.Constant;
import com.guy.calc.token.Function;
import com.guy.calc.token.Operand;
import com.guy.calc.token.Operator;
import com.guy.calc.token.Token;
import com.guy.calc.type.Base;

public class TokenFactory
{
	public static Token createToken(String token, Base b) throws Exception
	{
		Token t;

		if (Token.isOperand(token)) {
			t = new Operand(token);
		}
		else if (Token.isOperator(token)) {
			t = new Operator(token);
		}
		else if (Token.isBrace(token)) {
			t = new Brace(token);
		}
		else if (Token.isConstant(token)) {
			t = new Constant(token);
		}
		else if (Token.isFunction(token)) {
			t = new Function(token);
		}
		else {
			throw new Exception("Invalid token found");
		}

		return t;
	}
}
