package com.expense.tracker.service;

import com.expense.tracker.dto.ExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private BudgetService budgetService;
    
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request, User user) {
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(request.getCategory());
        expense.setNotes(request.getNotes());
        expense.setUser(user);
        
        Expense savedExpense = expenseRepository.save(expense);
        
        // Check budget limits
        budgetService.checkBudgetLimit(user, expense.getCategory(), 
                expense.getDate().getMonthValue(), expense.getDate().getYear());
        
        return ExpenseResponse.fromEntity(savedExpense);
    }
    
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to access this expense");
        }
        
        return ExpenseResponse.fromEntity(expense);
    }
    
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses(User user) {
        return expenseRepository.findByUserOrderByDateDesc(user).stream()
                .map(ExpenseResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByCategory(User user, Expense.Category category) {
        return expenseRepository.findByUserAndCategory(user, category).stream()
                .map(ExpenseResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate).stream()
                .map(ExpenseResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this expense");
        }
        
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(request.getCategory());
        expense.setNotes(request.getNotes());
        
        Expense updatedExpense = expenseRepository.save(expense);
        
        // Check budget limits
        budgetService.checkBudgetLimit(user, expense.getCategory(), 
                expense.getDate().getMonthValue(), expense.getDate().getYear());
        
        return ExpenseResponse.fromEntity(updatedExpense);
    }
    
    @Transactional
    public void deleteExpense(Long id, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this expense");
        }
        
        expenseRepository.delete(expense);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalExpenses(User user, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = expenseRepository.getTotalExpensesByUserAndDateRange(user, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }
}
