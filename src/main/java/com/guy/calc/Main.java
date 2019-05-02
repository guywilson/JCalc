package com.guy.calc;

import com.guy.calc.token.Operand;
import com.guy.calc.type.Base;
import com.guy.calc.util.DebugHelper;

public class Main
{
	private static void displayHelp()
	{
		System.out.println("\nOperators supported:");
		System.out.println("\t+, -, *, /, % (Modulo)");
		System.out.println("\t& (AND), | (OR), ~ (XOR)");
		System.out.println("\t^ (power, e.g. x to the power of y)\n");
		System.out.println("\tNesting is achieved with braces ()\n");
		System.out.println("Functions supported:");
		System.out.println("\tsin(x)\treturn the sine of the angle x degrees");
		System.out.println("\tcos(x)\treturn the cosine of the angle x degrees");
		System.out.println("\ttan(x)\treturn the tangent of the angle x degrees");
		System.out.println("\tasin(x)\treturn the angle in degrees of arcsine(x)");
		System.out.println("\tacos(x)\treturn the angle in degrees of arccosine(x)");
		System.out.println("\tatan(x)\treturn the angle in degrees of arctangent(x)");
		System.out.println("\tsqrt(x)\treturn the square root of x");
		System.out.println("\tlog(x)\treturn the log of x");
		System.out.println("\tln(x)\treturn the natural log of x");
		System.out.println("\tfact(x)\treturn the factorial of x");
		System.out.println("\tmem(n)\tthe value in memory location n, where n is 0 - 9\n");
		System.out.println("Constants supported:");
		System.out.println("\tpi\tthe ratio pi");
		System.out.println("\tc\tthe speed of light in a vacuum\n");
		System.out.println("Commands supported:");
		System.out.println("\tmemstn\tStore the last result in memory location n (0 - 9)");
		System.out.println("\tmemr\tRecall memory contents of all locations");
	    System.out.println("\tdec\tSwitch to decimal mode");
	    System.out.println("\thex\tSwitch to hexadecimal mode");
	    System.out.println("\tbin\tSwitch to binary mode");
	    System.out.println("\toct\tSwitch to octal mode");
	    System.out.println("\tsetpn\tSet the precision to n");
		System.out.println("\thelp\tThis help text");
		System.out.println("\tdbgon\tTurn on debugging output");
		System.out.println("\tdbgoff\tTurn off debugging output");
		System.out.println("\texit\tExit the calculator\n");
	}
	
	public static void main(String[] args)
	{
		String						calculation = null;
		StringBuffer				inputBuffer = new StringBuffer();
		int							i;
		boolean						loop;
		boolean						hasParams = false;
		DebugHelper					dbg;

		if (args.length > 0) {
			for (i = 0;i < args.length;i++) {
				inputBuffer.append(args[i]);
			}

			calculation = inputBuffer.toString();
			hasParams = true;
		}

		dbg = DebugHelper.getInstance();
		
		CalcSystem sys = CalcSystem.getInstance();

		if (!hasParams) {
			System.out.println("Welcome to Calc. A command line scientific calculator.");
			System.out.println("Type a calculation or command at the prompt, type 'help' for info.\n");
		}
		
		loop = true;

		while (loop) {
			try {
				if (!hasParams) {
				    try {
						calculation = sys.getUserInput();
					}
				    catch (Exception e) {
						e.printStackTrace();
					}

					if (dbg.isDebugOn()) {
						System.out.println("Calculation entered = [" + calculation + "]\n");
					}
				}

				if (calculation.length() == 0) {
					continue;
				}

				if (calculation.startsWith("exit") || calculation.startsWith("quit") || calculation.charAt(0) == 'q') {
					loop = false;
				}
				else {
					String resultString = handleCalculation(calculation);
					
					if (resultString != null) {
						if (resultString.equals("quit")) {
							loop = false;
						}
						else {
							System.out.println(resultString);
						}
					}
				}
			}
			catch (Exception e) {
				System.out.println("Caught exception: " + e.getMessage());
				e.printStackTrace();
			}

			if (hasParams) {
				loop = false;
			}
		}
	}
	
	public static String handleCalculation(String calculation) throws Exception
	{
		String 						strResult = null;
		DebugHelper					dbg;
		Operand						result = new Operand(0.0);
		int							i;
		
		dbg = DebugHelper.getInstance();

		CalcSystem sys = CalcSystem.getInstance();

	    Calculator calc = new Calculator();

	    try {
			if (calculation.startsWith("exit") || calculation.startsWith("quit") || calculation.charAt(0) == 'q') {
				strResult = "quit";
			}
			else if (calculation.startsWith("dbgon")) {
				dbg.setDebugOn();
			}
			else if (calculation.startsWith("dbgoff")) {
				dbg.setDebugOff();
			}
			else if (calculation.startsWith("memst")) {
				int memoryNum = 0;

				if (calculation.length() > 5) {
					memoryNum = Integer.valueOf(calculation.substring(5)).intValue();
				}

				sys.setMemoryValueAt(memoryNum, result);
			}
			else if (calculation.startsWith("memr")) {
				StringBuffer s = new StringBuffer();
				
			    for (i = 0;i < CalcSystem.NUM_MEMORY_SLOTS;i++) {
			        Operand mem = sys.getMemoryValueAt(i);
			        s.append("[" + i + "] = ").append(mem.toString(sys.getBase())).append(";\n");
			    }
			    
			    s.append("\n");
			    
			    strResult = s.toString();
			}
			else if (calculation.startsWith("dec")) {
				sys.setBase(Base.Dec);

			    String s = result.toString(Base.Dec);
			    
			    strResult = "Result = " + s;
			}
			else if (calculation.startsWith("hex")) {
				sys.setBase(Base.Hex);

			    String s = result.toString(Base.Hex);
			    
			    strResult = "Result = " + s;
			}
			else if (calculation.startsWith("bin")) {
				sys.setBase(Base.Bin);

			    String s = result.toString(Base.Bin);
			    
			    strResult = "Result = " + s;
			}
			else if (calculation.startsWith("oct")) {
				sys.setBase(Base.Oct);

			    String s = result.toString(Base.Oct);
			    
			    strResult = "Result = " + s;
			}
			else if (calculation.startsWith("setp")) {
				int precision = 0;

				if (calculation.length() > 4) {
					precision = Integer.valueOf(calculation.substring(4)).intValue();

					if (dbg.isDebugOn()) {
						System.out.println("Precision = [" + precision + "]\n");
					}

			        if (precision < 0 || precision > CalcSystem.MAX_DISPLAY_PRECISION) {
			            strResult = "Precision must be between 0 and " + CalcSystem.MAX_DISPLAY_PRECISION + "\n";
			        }
			        else {
			            sys.setScale(precision);
			        }
				}
			}
			else if (calculation.startsWith("help") || calculation.charAt(0) == '?') {
				displayHelp();
			}
			else {
				result = calc.evaluate(calculation.toString());

				strResult = calculation + " = " + result.toString(sys.getBase());
			}
		}
		catch (Exception e) {
			throw e;
		}
		
		return strResult;
	}
}
