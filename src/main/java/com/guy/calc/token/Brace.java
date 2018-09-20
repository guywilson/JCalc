package com.guy.calc.token;

import com.guy.calc.type.BraceType;

public class Brace extends Token
{
	private BraceType type;

	public Brace(String token)
	{
		super(token, "Brace");
		
		if (Token.isBraceLeft(token)) {
			type = BraceType.Open;
		}
		else if (Token.isBraceRight(token)) {
			type = BraceType.Close;
		}
	}

	public BraceType getType()
	{
		return type;
	}

	public Boolean isBrace() {
		return true;
	}
}
