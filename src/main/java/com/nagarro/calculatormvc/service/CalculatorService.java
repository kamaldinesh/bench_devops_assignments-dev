package com.nagarro.calculatormvc.service;

import com.nagarro.calculatormvc.model.ResultModel;

public class CalculatorService {
	
	static ResultModel r1= new ResultModel();
	static double result;
	
	
	public static ResultModel addResult(double num1, double num2) {	
		result=num1+num2;
		r1.setResult(result);
		r1.setOperator("+");
		return r1;
	}
	
	public static ResultModel subtractResult(double num1, double num2) {	
		result=num1-num2;
		r1.setResult(result);
		r1.setOperator("-");
		return r1;
	}
	
	public static ResultModel multiplyResult(double num1, double num2) {	
		result=num1*num2;
		r1.setResult(result);
		r1.setOperator("*");
		return r1;
	}
	
	public static ResultModel divideResult(double num1, double num2) {	
		try {
			result = num1 / num2;
            r1.setResult(result);
            r1.setOperator("/");
		}
		catch(ArithmeticException e) {
			System.out.println("Cannot divide by zero!");
		}
		return r1;
	}
}
