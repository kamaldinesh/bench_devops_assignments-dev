package com.nagarro.calculatormvc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test for home() method
    @Test
    void testHome() throws Exception {
        mockMvc.perform(post("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    // Test for calculate() method with addition
    @Test
    void testCalculateAdd() throws Exception {
        mockMvc.perform(post("/calculate")
                .param("num1", "5.0")
                .param("num2", "3.0")
                .param("operation", "add"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("result", 8.0))
                .andExpect(model().attribute("operator", "+"));
    }

    // Test for calculate() method with subtraction
    @Test
    void testCalculateSubtract() throws Exception {
        mockMvc.perform(post("/calculate")
                .param("num1", "10.0")
                .param("num2", "3.0")
                .param("operation", "subtract"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("result", 7.0))
                .andExpect(model().attribute("operator", "-"));
    }

    // Test for calculate() method with multiplication
    @Test
    void testCalculateMultiply() throws Exception {
        mockMvc.perform(post("/calculate")
                .param("num1", "5.0")
                .param("num2", "3.0")
                .param("operation", "multiply"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("result", 15.0))
                .andExpect(model().attribute("operator", "*"));
    }

    // Test for calculate() method with division by non-zero number
    @Test
    void testCalculateDivide() throws Exception {
        mockMvc.perform(post("/calculate")
                .param("num1", "10.0")
                .param("num2", "2.0")
                .param("operation", "divide"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("result", 5.0))
                .andExpect(model().attribute("operator", "/"));
    }

    // Test for calculate() method with division by zero
    @Test
    void testCalculateDivideByZero() throws Exception {
        mockMvc.perform(post("/calculate")
                .param("num1", "10.0")
                .param("num2", "0.0")
                .param("operation", "divide"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("message", "Cannot divide by zero!"));
    }
}