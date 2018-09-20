package com.guy.calc.token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import com.guy.calc.CalcSystem;
import com.guy.calc.type.Base;
import com.guy.calc.type.Func;

public class Function extends Operator
{
	private Func function;
	private int numArguments;
	
	private MathContext mc = CalcSystem.getInstance().getMathContext();
	
	private BigInteger _factorial(long arg)
	{
		BigInteger result = null;
		
		if (arg > 1) {
			result = BigInteger.valueOf(arg);
			long index = 0;

			for (index = arg - 1;index > 0;index--) {
				result = result.multiply(BigInteger.valueOf(index));
			}
		}

		return result;
	}

	public Function(String token) throws Exception
	{
		super(token);
		
	    boolean error = false;
	    
		if (Token.isFunctionSine(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.Sine;
			numArguments = 1;
		}
		else if (Token.isFunctionCosine(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.Cosine;
			numArguments = 1;
		}
		else if (Token.isFunctionTangent(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.Tangent;
			numArguments = 1;
		}
		else if (Token.isFunctionArcSine(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.ArcSine;
			numArguments = 1;
		}
		else if (Token.isFunctionArcCosine(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.ArcCosine;
			numArguments = 1;
		}
		else if (Token.isFunctionArcTangent(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.ArcTangent;
			numArguments = 1;
		}
		else if (Token.isFunctionSquareRoot(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.SquareRoot;
			numArguments = 1;
		}
		else if (Token.isFunctionLogarithm(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.Logarithm;
			numArguments = 1;
		}
		else if (Token.isFunctionNaturalLog(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.NaturalLog;
			numArguments = 1;
		}
		else if (Token.isFunctionFactorial(token)) {
	        if (CalcSystem.getInstance().getBase() != Base.Dec) {
	            error = true;
	        }
			function = Func.Factorial;
			numArguments = 1;
		}
		else if (Token.isFunctionMemory(token)) {
			function = Func.Memory;
			numArguments = 1;
		}

		setPrecedence(5);
	    
	    if (error) {
	        throw new Exception("Invalid token found for mode");
	    }
	}

	public Operand evaluate(Operand arg1)
	{
		Operand result = null;
		
		BigDecimal degreesToRadians = new BigDecimal(Math.PI, mc).divide(new BigDecimal("180.0", mc), 16, RoundingMode.HALF_UP);
		BigDecimal radiansToDegrees = new BigDecimal("180.0", mc).divide(new BigDecimal(Math.PI, mc), 16, RoundingMode.HALF_UP);

		switch (this.function) {

			case Sine:
				result = new Operand(Math.sin(arg1.getDoubleValue().multiply(degreesToRadians, mc).doubleValue()));
				break;

			case Cosine:
				result = new Operand(Math.cos(arg1.getDoubleValue().multiply(degreesToRadians, mc).doubleValue()));
				break;

			case Tangent:
				result = new Operand(Math.tan(arg1.getDoubleValue().multiply(degreesToRadians, mc).doubleValue()));
				break;

			case ArcSine:
				result = new Operand(Math.asin(arg1.getDoubleValue().doubleValue()) * radiansToDegrees.doubleValue());
				break;

			case ArcCosine:
				result = new Operand(Math.acos(arg1.getDoubleValue().doubleValue()) * radiansToDegrees.doubleValue());
				break;

			case ArcTangent:
				result = new Operand(Math.atan(arg1.getDoubleValue().doubleValue()) * radiansToDegrees.doubleValue());
				break;

			case SquareRoot:
				result = new Operand(arg1.getDoubleValue().sqrt(mc));
				break;

			case Logarithm:
				result = new Operand(Math.log10(arg1.getDoubleValue().doubleValue()));
				break;

			case NaturalLog:
				result = new Operand(Math.log(arg1.getDoubleValue().doubleValue()));
				break;

			case Factorial:
				result = new Operand(_factorial(arg1.getIntValue().longValue()));
				break;

			case Memory:
				result = CalcSystem.getInstance().getMemoryValueAt(arg1.getIntValue().intValue());
				break;

			default:
				break;
		}

		return result;
	}

	public int getNumArguments() {
		return numArguments;
	}

	public Boolean isFunction() {
		return true;
	}
}
