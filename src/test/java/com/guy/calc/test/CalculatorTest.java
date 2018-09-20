package com.guy.calc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.guy.calc.CalcSystem;
import com.guy.calc.Calculator;
import com.guy.calc.token.Operand;
import com.guy.calc.type.Base;

public class CalculatorTest
{
	private static double acceptableDelta = 0.02;
	
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testEvaluate1()
	{
		String calculation = "2 + (3 * 4) ^ 2 - 13";
		double expectedDbl = 133.0;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
			e.printStackTrace();
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate2()
	{
		String calculation = "12 - ((2 * 3) - (8 / 2) / 0.5) / 12.653";
		double expectedDbl = 12.16;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate3()
	{
		String calculation = "2 ^ 16 - 1";
		double expectedDbl = 65535.0;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate4()
	{
		String calculation = "(((((((1 + 2 * 3)-2)*4)/2)-12)+261)/12) - 5.25";
		double expectedDbl = 16.33;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate5()
	{
		String calculation = "pi + sin(45 + 45)";
		double expectedDbl = 4.14;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate6()
	{
		String calculation = "pi * (2 ^ 2)";
		double expectedDbl = 12.57;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate7()
	{
		// Calculate pi...
		String calculation = "3 + 4/(2 * 3 * 4) - 4/(4 * 5 * 6) + 4/(6 * 7 * 8) - 4/(8 * 9 * 10) + 4/(10 * 11 * 12)";
		double expectedDbl = 3.14;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate8()
	{
		String calculation = "84 * -15 + sin(47)";
		double expectedDbl = -1259.27;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate9()
	{
		String calculation = "16 / (3 - 5 + 8) * (3 + 5 - 4)";
		double expectedDbl = 10.66;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate10()
	{
		String calculation = "sin(90) * cos(45) * tan(180) + asin(1) + acos(0) + atan(25)";
		double expectedDbl = 267.71;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testEvaluate11()
	{
		String calculation = "fact(12) + 13";
		double expectedDbl = 479001613.0;

		Operand result = null;

		Calculator calc = new Calculator();
		
		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		assertEquals(result.getDoubleValue().doubleValue(), expectedDbl, acceptableDelta);
	}

	@Test
	public void testValidate1()
	{
		boolean validationFail = false;
		
		String calculation = "((((4(((1 + 2 * 3)-2)*4)/2)-12)+261)/12) - 5.25";

		Calculator calc = new Calculator();
		
		try {
			calc.evaluate(calculation);
		}
		catch (Exception e) {
			validationFail = true;
			e.printStackTrace();
		}
		
		assertTrue(validationFail);
	}

	@Test
	public void testValidate2()
	{
		boolean validationFail = false;
		
		String calculation = "(((((((1 + 2 * 3)-2)*4)/2)-12)+261)/12 - 5.25";

		Calculator calc = new Calculator();
		
		try {
			calc.evaluate(calculation);
		}
		catch (Exception e) {
			validationFail = true;
			e.printStackTrace();
		}
		
		assertTrue(validationFail);
	}

	@Test
	public void testValidate3()
	{
		boolean validationFail = false;
		
		String calculation = "((((((1 + 2 * 3)-2)*4)/2)-12)+261)/12) - 5.25";

		Calculator calc = new Calculator();
		
		try {
			calc.evaluate(calculation);
		}
		catch (Exception e) {
			validationFail = true;
			e.printStackTrace();
		}
		
		assertTrue(validationFail);
	}

	@Test
	public void testValidate4()
	{
		boolean validationFail = false;
		
		String calculation = "84 * -15 + sin47";

		Calculator calc = new Calculator();
		
		try {
			calc.evaluate(calculation);
		}
		catch (Exception e) {
			validationFail = true;
			e.printStackTrace();
		}
		
		assertTrue(validationFail);
	}

	@Test
	public void testBase1()
	{
		Calculator calc = new Calculator();
		CalcSystem.getInstance().setBase(Base.Hex);
		
		String calculation = "2F & 20";

		Operand result = null;

		try {
			result = calc.evaluate(calculation);
		}
		catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
		
		CalcSystem.getInstance().setBase(Base.Dec);
		
		assertEquals(result.getIntValue().intValue(), 0x20);
	}
}
