package com.expense.tracker.service;

import com.expense.tracker.dto.ExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    
    @Mock
    private ExpenseRepository expenseRepository;
    
    @Mock
    private BudgetService budgetService;
    
    @InjectMocks
    private ExpenseService expenseService;
    
    private User testUser;
    private Expense testExpense;
    private ExpenseRequest testRequest;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        
        testExpense = new Expense();
        testExpense.setId(1L);
        testExpense.setDescription("Test Expense");
        testExpense.setAmount(new BigDecimal("100.00"));
        testExpense.setDate(LocalDate.now());
        testExpense.setCategory(Expense.Category.FOOD_AND_DINING);
        testExpense.setUser(testUser);
        
        testRequest = new ExpenseRequest();
        testRequest.setDescription("Test Expense");
        testRequest.setAmount(new BigDecimal("100.00"));
        testRequest.setDate(LocalDate.now());
        testRequest.setCategory(Expense.Category.FOOD_AND_DINING);
    }
    
    @Test
    void testCreateExpense() {
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);
        
        ExpenseResponse response = expenseService.createExpense(testRequest, testUser);
        
        assertNotNull(response);
        assertEquals("Test Expense", response.getDescription());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        verify(expenseRepository, times(1)).save(any(Expense.class));
        verify(budgetService, times(1)).checkBudgetLimit(any(), any(), anyInt(), anyInt());
    }
    
    @Test
    void testGetExpenseById() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        
        ExpenseResponse response = expenseService.getExpenseById(1L, testUser);
        
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Expense", response.getDescription());
    }
    
    @Test
    void testGetExpenseByIdNotFound() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            expenseService.getExpenseById(1L, testUser);
        });
    }
    
    @Test
    void testGetAllExpenses() {
        when(expenseRepository.findByUserOrderByDateDesc(testUser)).thenReturn(Arrays.asList(testExpense));
        
        List<ExpenseResponse> responses = expenseService.getAllExpenses(testUser);
        
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test Expense", responses.get(0).getDescription());
    }
    
    @Test
    void testUpdateExpense() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);
        
        testRequest.setDescription("Updated Expense");
        ExpenseResponse response = expenseService.updateExpense(1L, testRequest, testUser);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void testDeleteExpense() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        doNothing().when(expenseRepository).delete(any(Expense.class));
        
        expenseService.deleteExpense(1L, testUser);
        
        verify(expenseRepository, times(1)).delete(any(Expense.class));
    }
    
    @Test
    void testGetTotalExpenses() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        BigDecimal expectedTotal = new BigDecimal("500.00");
        
        when(expenseRepository.getTotalExpensesByUserAndDateRange(testUser, startDate, endDate))
                .thenReturn(expectedTotal);
        
        BigDecimal total = expenseService.getTotalExpenses(testUser, startDate, endDate);
        
        assertEquals(expectedTotal, total);
    }
}
