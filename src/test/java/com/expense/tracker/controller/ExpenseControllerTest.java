package com.expense.tracker.controller;

import com.expense.tracker.dto.ExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExpenseControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private ExpenseService expenseService;
    
    private ExpenseRequest testRequest;
    private ExpenseResponse testResponse;
    
    @BeforeEach
    void setUp() {
        testRequest = new ExpenseRequest();
        testRequest.setDescription("Test Expense");
        testRequest.setAmount(new BigDecimal("100.00"));
        testRequest.setDate(LocalDate.now());
        testRequest.setCategory(Expense.Category.FOOD_AND_DINING);
        
        testResponse = new ExpenseResponse();
        testResponse.setId(1L);
        testResponse.setDescription("Test Expense");
        testResponse.setAmount(new BigDecimal("100.00"));
        testResponse.setDate(LocalDate.now());
        testResponse.setCategory(Expense.Category.FOOD_AND_DINING);
    }
    
    @Test
    @WithMockUser(username = "testuser")
    void testCreateExpense() throws Exception {
        when(expenseService.createExpense(any(ExpenseRequest.class), any(User.class)))
                .thenReturn(testResponse);
        
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Expense"))
                .andExpect(jsonPath("$.amount").value(100.00));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    void testGetAllExpenses() throws Exception {
        when(expenseService.getAllExpenses(any(User.class)))
                .thenReturn(Arrays.asList(testResponse));
        
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test Expense"));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    void testGetExpenseById() throws Exception {
        when(expenseService.getExpenseById(eq(1L), any(User.class)))
                .thenReturn(testResponse);
        
        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Expense"));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    void testUpdateExpense() throws Exception {
        when(expenseService.updateExpense(eq(1L), any(ExpenseRequest.class), any(User.class)))
                .thenReturn(testResponse);
        
        mockMvc.perform(put("/api/expenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Expense"));
    }
    
    @Test
    void testCreateExpenseUnauthorized() throws Exception {
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isUnauthorized());
    }
}
