package com.nagarro.calculatormvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nagarro.calculatormvc.model.ResultModel;
import com.nagarro.calculatormvc.service.CalculatorService;

@Controller
public class HomeController {
	
	@RequestMapping(value="/home", method=RequestMethod.POST)
	public String home() {
		return "index";
	}
	
//	@RequestMapping("/")
//	public String home2() {
//		return "index";
//	}
	
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public ModelAndView calculate(@RequestParam("num1") double num1,
                                   @RequestParam("num2") double num2,
                                   @RequestParam("operation") String operation) {
        double result = 0;
        String operator = "";
        ResultModel r1;
      
        switch (operation) {
            case "add":
            	r1= CalculatorService.addResult(num1, num2);
                result = r1.getResult();
                operator = r1.getOperator();
                break;
            case "subtract":
            	r1= CalculatorService.subtractResult(num1, num2);
                result = r1.getResult();
                operator = r1.getOperator();
                break;
            case "multiply":
            	r1= CalculatorService.multiplyResult(num1, num2);
                result = r1.getResult();
                operator = r1.getOperator();
                break;
            case "divide":
                if (num2 != 0) {
                	r1=CalculatorService.divideResult(num1, num2);
                	result = r1.getResult();
                    operator = r1.getOperator();
                } else {
                    return new ModelAndView("error", "message", "Cannot divide by zero!");
                }
                break;
        }

        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.addObject("num1", num1);
        modelAndView.addObject("num2", num2);
        modelAndView.addObject("operator", operator);
        modelAndView.addObject("result", result);

        return modelAndView;
    }
}
