package com.expense.tracker.service;

import com.expense.tracker.dto.BudgetRequest;
import com.expense.tracker.dto.BudgetResponse;
import com.expense.tracker.entity.Budget;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.BudgetRepository;
import com.expense.tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    
    @Autowired
    private BudgetRepository budgetRepository;
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Transactional
    public BudgetResponse createBudget(BudgetRequest request, User user) {
        if (budgetRepository.existsByUserAndCategoryAndMonthAndYear(
                user, request.getCategory(), request.getMonth(), request.getYear())) {
            throw new RuntimeException("Budget already exists for this category and period");
        }
        
        Budget budget = new Budget();
        budget.setCategory(request.getCategory());
        budget.setLimitAmount(request.getLimitAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setAlertThreshold(request.getAlertThreshold() != null ? request.getAlertThreshold() : 80);
        budget.setUser(user);
        
        Budget savedBudget = budgetRepository.save(budget);
        
        // Calculate spent amount
        LocalDate startDate = LocalDate.of(savedBudget.getYear(), savedBudget.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        BigDecimal spentAmount = expenseRepository.getTotalExpensesByCategoryAndDateRange(
                user, savedBudget.getCategory(), startDate, endDate);
        
        return BudgetResponse.fromEntity(savedBudget, spentAmount);
    }
    
    @Transactional(readOnly = true)
    public BudgetResponse getBudgetById(Long id, User user) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
        
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to access this budget");
        }
        
        // Calculate spent amount
        LocalDate startDate = LocalDate.of(budget.getYear(), budget.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        BigDecimal spentAmount = expenseRepository.getTotalExpensesByCategoryAndDateRange(
                user, budget.getCategory(), startDate, endDate);
        
        return BudgetResponse.fromEntity(budget, spentAmount);
    }
    
    @Transactional(readOnly = true)
    public List<BudgetResponse> getAllBudgets(User user) {
        return budgetRepository.findByUser(user).stream()
                .map(budget -> {
                    // Calculate spent amount for each budget
                    LocalDate startDate = LocalDate.of(budget.getYear(), budget.getMonth(), 1);
                    LocalDate endDate = startDate.plusMonths(1).minusDays(1);
                    BigDecimal spentAmount = expenseRepository.getTotalExpensesByCategoryAndDateRange(
                            user, budget.getCategory(), startDate, endDate);
                    return BudgetResponse.fromEntity(budget, spentAmount);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgetsByMonthAndYear(User user, Integer month, Integer year) {
        return budgetRepository.findByUserAndMonthAndYear(user, month, year).stream()
                .map(budget -> {
                    // Calculate spent amount for each budget
                    LocalDate startDate = LocalDate.of(budget.getYear(), budget.getMonth(), 1);
                    LocalDate endDate = startDate.plusMonths(1).minusDays(1);
                    BigDecimal spentAmount = expenseRepository.getTotalExpensesByCategoryAndDateRange(
                            user, budget.getCategory(), startDate, endDate);
                    return BudgetResponse.fromEntity(budget, spentAmount);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional
    public BudgetResponse updateBudget(Long id, BudgetRequest request, User user) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
        
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this budget");
        }
        
        // Check if another budget exists with the new category/period combination
        if (!budget.getCategory().equals(request.getCategory()) || 
            !budget.getMonth().equals(request.getMonth()) || 
            !budget.getYear().equals(request.getYear())) {
            if (budgetRepository.existsByUserAndCategoryAndMonthAndYear(
                    user, request.getCategory(), request.getMonth(), request.getYear())) {
                throw new RuntimeException("Budget already exists for this category and period");
            }
        }
        
        budget.setCategory(request.getCategory());
        budget.setLimitAmount(request.getLimitAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setAlertThreshold(request.getAlertThreshold() != null ? request.getAlertThreshold() : budget.getAlertThreshold());
        
        Budget updatedBudget = budgetRepository.save(budget);
        
        // Calculate spent amount
        LocalDate startDate = LocalDate.of(updatedBudget.getYear(), updatedBudget.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        BigDecimal spentAmount = expenseRepository.getTotalExpensesByCategoryAndDateRange(
                user, updatedBudget.getCategory(), startDate, endDate);
        
        return BudgetResponse.fromEntity(updatedBudget, spentAmount);
    }
    
    @Transactional
    public void deleteBudget(Long id, User user) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
        
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this budget");
        }
        
        budgetRepository.delete(budget);
    }
    
    @Transactional(readOnly = true)
    public void checkBudgetLimit(User user, Expense.Category category, Integer month, Integer year) {
        Budget budget = budgetRepository.findByUserAndCategoryAndMonthAndYear(user, category, month, year)
                .orElse(null);
        
        if (budget == null) {
            return; // No budget set for this category
        }
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByCategoryAndDateRange(
                user, category, startDate, endDate);
        
        if (totalExpenses != null && totalExpenses.compareTo(budget.getLimitAmount()) > 0) {
            System.out.println("WARNING: Budget limit exceeded for category " + category + 
                    " in " + month + "/" + year + 
                    ". Limit: " + budget.getLimitAmount() + 
                    ", Current: " + totalExpenses);
            // Here you can implement notification logic (email, SMS, etc.)
        }
    }
}
