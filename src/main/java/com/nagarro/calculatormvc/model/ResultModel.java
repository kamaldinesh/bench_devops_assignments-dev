package com.nagarro.calculatormvc.model;

public class ResultModel {
	double result;
	String operator;
	
	public ResultModel() {
		super();
	}
	public ResultModel(double result, String operator) {
		super();
		this.result = result;
		this.operator = operator;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
}

