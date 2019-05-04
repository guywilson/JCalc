/******************************************************************************
** SHUNTING YARD ALGORITHM
** -----------------------
** 
** While there are tokens to be read:
** 	Read a token.
** 	If the token is a number, then push it to the output queue.
** 	If the token is a function token, then push it onto the stack.
** 	If the token is a function argument separator (e.g., a comma):
** 		Until the token at the top of the stack is a left parenthesis, pop
** 		operators off the stack onto the output queue. If no left parentheses
** 		are encountered, either the separator was misplaced or parentheses were
** 		mismatched.
** 	If the token is an operator, o1, then:
** 		while there is an operator token o2, at the top of the operator stack
** 		and either
** 		o1 is left-associative and its precedence is less than or equal to that
** 		of o2, or
** 		o1 is right associative, and has precedence less than that of o2,
** 		pop o2 off the operator stack, onto the output queue;
** 		at the end of iteration push o1 onto the operator stack.
** 	If the token is a left parenthesis (i.e. "("), then push it onto the stack.
** 	If the token is a right parenthesis (i.e. ")"):
** 		Until the token at the top of the stack is a left parenthesis, pop
** 		operators off the stack onto the output queue.
** 		Pop the left parenthesis from the stack, but not onto the output queue.
** 		If the token at the top of the stack is a function token, pop it onto
** 		the output queue.
** 		If the stack runs out without finding a left parenthesis, then there
** 		are mismatched parentheses.
** 		When there are no more tokens to read:
** 		While there are still operator tokens in the stack:
** 			If the operator token on the top of the stack is a parenthesis,
** 			then there are mismatched parentheses.
** 			Pop the operator onto the output queue.
** Exit.
** 
******************************************************************************/

package com.guy.calc;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.guy.calc.token.Brace;
import com.guy.calc.token.Function;
import com.guy.calc.token.Operand;
import com.guy.calc.token.Operator;
import com.guy.calc.token.Token;
import com.guy.calc.type.Associativity;
import com.guy.calc.type.BraceType;
import com.guy.calc.util.CalcTokenizer;
import com.guy.log.Logger;

public class Calculator
{
	private CalcSystem sys = CalcSystem.getInstance();
	
	private Logger log = new Logger(Calculator.class);
        
	private void _convertToRPN(String expression, Queue<Token> outputQueue) throws Exception
	{
		log.entry();
		
		try {
			Stack<Token> operatorStack = new Stack<>();

			CalcTokenizer tok = new CalcTokenizer(expression, this.sys.getBase());

			while (tok.hasMoreTokens()) {
				Token t = tok.nextToken();

				/*
				** If the token is a number, then push it to the output queue.
				*/
				if (t.isOperand()) {
					outputQueue.add(t);
				}
				/*
				** If the token is a function token, then push it onto the stack.
				*/
				else if (t.isFunction()) {
					operatorStack.push(t);
				}
				/*
				** If the token is an operator, o1, then:
				**	while there is an operator token o2, at the top of the operator stack
				**	and either
				**	o1 is left-associative and its precedence is less than or equal to that
				**	of o2, or
				**	o1 is right associative, and has precedence less than that of o2,
				**	pop o2 off the operator stack, onto the output queue;
				**	at the end of iteration push o1 onto the operator stack.
				*/
				else if (t.isOperator()) {
					Operator o1 = (Operator)t;

					while (!operatorStack.empty()) {
						Token topToken = operatorStack.peek();

						if (!topToken.isOperator()) {
							break;
						}

						Operator o2 = (Operator)topToken;

						if ((o1.getAssociativity() == Associativity.Left && o1.getPrecedence() <= o2.getPrecedence()) ||
							(o1.getAssociativity() == Associativity.Right && o1.getPrecedence() < o2.getPrecedence()))
						{
							o2 = (Operator)operatorStack.pop();
							outputQueue.add(o2);
						}
						else {
							break;
						}
					}

					operatorStack.push(o1);
				}
				else if (t.isBrace()) {
					Brace br = (Brace)t;

					/*
					** If the token is a left parenthesis (i.e. "("), then push it onto the stack.
					*/
					if (br.getType() == BraceType.Open) {
						operatorStack.push(br);
					}
					else {
						/*
						If the token is a right parenthesis (i.e. ")"):
							Until the token at the top of the stack is a left parenthesis, pop
							operators off the stack onto the output queue.
							Pop the left parenthesis from the stack, but not onto the output queue.
							If the token at the top of the stack is a function token, pop it onto
							the output queue.
							If the stack runs out without finding a left parenthesis, then there
							are mismatched parentheses.
						*/
						boolean foundLeftParenthesis = false;

						while (!operatorStack.empty()) {
							Token stackToken = operatorStack.pop();

							if (stackToken.isBrace()) {
								Brace brace = (Brace)stackToken;

								if (brace.getType() == BraceType.Open) {
									foundLeftParenthesis = true;
									break;
								}
							}
							else {
								outputQueue.add(stackToken);
							}
						}

						if (!foundLeftParenthesis) {
							/*
							** If we've got here, we must have unmatched parenthesis...
							*/
							throw new Exception("Failed to find left parenthesis on operator stack");
						}
					}
				}
			}

			/*
			While there are still operator tokens in the stack:
				If the operator token on the top of the stack is a parenthesis,
				then there are mismatched parentheses.
				Pop the operator onto the output queue.
			*/
			while (!operatorStack.empty()) {
				Token stackToken = operatorStack.pop();

				if (stackToken.isBrace()) {
					/*
					** If we've got here, we must have unmatched parenthesis...
					*/
					throw new Exception("Found too many parenthesis on operator stack");
				}
				else {
					outputQueue.add(stackToken);
				}
			}
		}
		catch (Throwable e) {
			log.error("Error converting to RPN", e);
			throw e;
		}
		finally {
			log.exit();
		}
	}
                      
	public Operand evaluate(String expression) throws Exception
	{
		Operand result;
		
		log.entry();
		
		try {
			Queue<Token> outputQueue = new LinkedList<Token>();

			/*
			** Convert the calculation in infix notation to the postfix notation
			** (Reverse Polish Notation) using the 'shunting yard algorithm'...
			*/
			_convertToRPN(expression, outputQueue);

			Stack<Token> stack = new Stack<Token>();

			while (!outputQueue.isEmpty()) {
				Token t = outputQueue.poll();

				log.debug("Got Token '" + t.getToken() + "'");

				if (t.isOperand()) {
					stack.push(t);
				}
				/*
				** Must be Operator or Function...
				*/
				else {
					if (t.isOperator()) {
						if (t.isFunction()) {
							Function f = (Function)t;

							if (stack.size() < f.getNumArguments()) {
								throw new Exception("Too few arguments for function");
							}

							Operand o1 = (Operand)stack.pop();

							Operand r = f.evaluate(o1);

							stack.push(r);
						}
						else {
							if (stack.size() < 2) {
								throw new Exception("Too few arguments for operator");
							}

							Operand o2 = (Operand)stack.pop();
							Operand o1 = (Operand)stack.pop();

							Operator op = (Operator)t;

							Operand r = op.evaluate(o1, o2);

							stack.push(r);
						}
					}
				}
			}

			/*
			** If there is one and only one item left on the stack,
			** it is the result of the calculation. Otherwise, we
			** have too many tokens and therefore an error...
			*/
			if (stack.size() == 1) {
				result = (Operand)stack.pop();

				log.debug("Got Result - DoubleValue [" + result.getDoubleValue() + "], IntValue [" + result.getIntValue() + "]");
			}
			else {
				throw new Exception("Too many arguments left on stack");
			}
		}
		catch (Throwable e) {
			log.error("Error evaluating expression [" + expression + "]", e);
			throw e;
		}
		finally {
			log.exit();
		}

		return result;
	}
	
	public String evaluateToString(String expression) throws Exception
	{
		String s = null;
		
		log.entry();
		
		try {
			Operand result = evaluate(expression);

			s = result.toString();
		}
		catch (Throwable e) {
			log.error("Error evaluating expression to string", e);
			throw e;
		}
		finally {
			log.exit();
		}
		
		return s;
	}
}
