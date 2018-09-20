package com.guy.calc.token;

import com.guy.calc.CalcSystem;
import com.guy.calc.type.Associativity;
import com.guy.calc.type.Base;
import com.guy.calc.type.Op;

public class Operator extends Token
{
	private int precedence;
	private Associativity assoc;
	private Op op;

	protected void setPrecedence(int precedence) {
		this.precedence = precedence;
	}

	public Operator(String token) throws Exception
	{
		super(token, "Operator");
		
		if (Token.isOperatorPlus(token)) {
			op = Op.Plus;
			precedence = 2;
			assoc = Associativity.Left;
		}
		else if (Token.isOperatorMinus(token)) {
			op = Op.Minus;
			precedence = 2;
			assoc = Associativity.Left;
		}
		else if (Token.isOperatorMultiply(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            throw new Exception("Invalid token found for mode");
	        }
	        
			op = Op.Multiply;
			precedence = 3;
			assoc = Associativity.Left;
		}
		else if (Token.isOperatorDivide(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            throw new Exception("Invalid token found for mode");
	        }
	        
			op = Op.Divide;
			precedence = 3;
			assoc = Associativity.Left;
		}
		else if (Token.isOperatorPower(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            throw new Exception("Invalid token found for mode");
	        }
	        
			op = Op.Power;
			precedence = 4;
			assoc = Associativity.Right;
		}
		else if (Token.isOperatorMod(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            throw new Exception("Invalid token found for mode");
	        }
	        
			op = Op.Mod;
			precedence = 3;
			assoc = Associativity.Left;
		}
		else if (Token.isOperatorAND(token)) {
			op = Op.And;
			precedence = 4;
			assoc = Associativity.Left;
		}
		else if (Token.isOperatorOR(token)) {
			op = Op.Or;
			precedence = 4;
			assoc = Associativity.Left;
		}
		else if (Token.isOperatorXOR(token)) {
			op = Op.Xor;
			precedence = 4;
			assoc = Associativity.Left;
		}
	}
		
	public Operator(String token, String className)
	{
		super(token, className);
	}

	public int getPrecedence()
	{
		return precedence;
	}
		
	public Associativity getAssociativity()
	{
		return assoc;
	}
	
	public Op getOp()
	{
		return op;
	}

	public Operand evaluate(Operand o1, Operand o2) throws Exception
	{
		Operand result = null;;

		switch (getOp()) {

			case Plus:
				result = o1.add(o2);
				break;

			case Minus:
				result = o1.subtract(o2);
				break;

			case Multiply:
				result = o1.multiply(o2);
				break;

			case Divide:
				result = o1.divide(o2);
				break;

			case Power:
				result = o1.pow(o2);
				break;

			case Mod:
				result = o1.mod(o2);
				break;

			case And:
				result = o1.and(o2);
				break;

			case Or:
				result = o1.or(o2);
				break;

			case Xor:
				result = o1.xor(o2);
				break;
		}

		return result;
	}

	public Boolean isOperator() {
		return true;
	}
}
