package com.nagarro.calculatormvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.nagarro.calculatormvc.service.CalculatorService;

class ResultModelTest {

	@Test
	void test() {
		System.out.println("This test is running");
	}

	//addition test
	@Test
    public void testAddResult() {
        double num1 = 5;
        double num2 = 3;
        ResultModel resultModel = CalculatorService.addResult(num1, num2);
        
        // Check if the result is calculated correctly
        assertEquals(8, resultModel.getResult(), 0.01);
        
        // Check if the operator is set to "+"
        assertEquals("+", resultModel.getOperator());
    }
	
	//subtraction test
	@Test
    public void testSubtractResult() {
        double num1 = 10;
        double num2 = 3;
        ResultModel resultModel = CalculatorService.subtractResult(num1, num2);
        
        // Check if the result is calculated correctly
        assertEquals(7, resultModel.getResult(), 0.01);
        
        // Check if the operator is set to "-"
        assertEquals("-", resultModel.getOperator());
    }
	
	//multiplication test
	@Test
    public void testMultiplyResult() {
        double num1 = 5;
        double num2 = 3;
        ResultModel resultModel = CalculatorService.multiplyResult(num1, num2);
        
        // Check if the result is calculated correctly
        assertEquals(15, resultModel.getResult(), 0.01);
        
        // Check if the operator is set to "*"
        assertEquals("*", resultModel.getOperator());
    }
	
	//division test
	@Test
    public void testDivideResult() {
        // Test case 1: Division by a non-zero number
        double num1 = 10;
        double num2 = 2;
        ResultModel resultModel = CalculatorService.divideResult(num1, num2);
        
        // Check if the result is calculated correctly
        assertEquals(5, resultModel.getResult(), 0.01);
        
        // Check if the operator is set to "/"
        assertEquals("/", resultModel.getOperator());
        
        // Test case 2: Division by zero
        num2 = 0;
        try {
            CalculatorService.divideResult(num1, num2);
            // If divideResult method doesn't throw an ArithmeticException, fail the test
            System.out.println("Expected ArithmeticException was not thrown");
        } catch (ArithmeticException e) {
            // If an ArithmeticException is thrown, test passes
            assertEquals("Cannot divide by zero!", e.getMessage());
        }
    }
	
}
