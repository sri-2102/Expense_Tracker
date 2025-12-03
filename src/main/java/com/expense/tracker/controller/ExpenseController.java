package com.expense.tracker.controller;

import com.expense.tracker.dto.ExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    @PostMapping
    public ResponseEntity<?> createExpense(@Valid @RequestBody ExpenseRequest request,
                                          @AuthenticationPrincipal User user) {
        try {
            ExpenseResponse response = expenseService.createExpense(request, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(@AuthenticationPrincipal User user) {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses(user);
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        try {
            ExpenseResponse response = expenseService.getExpenseById(id, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(
            @PathVariable Expense.Category category,
            @AuthenticationPrincipal User user) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByCategory(user, category);
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal User user) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(user, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalExpenses(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal User user) {
        BigDecimal total = expenseService.getTotalExpenses(user, startDate, endDate);
        return ResponseEntity.ok(total);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id,
                                          @Valid @RequestBody ExpenseRequest request,
                                          @AuthenticationPrincipal User user) {
        try {
            ExpenseResponse response = expenseService.updateExpense(id, request, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id,
                                          @AuthenticationPrincipal User user) {
        try {
            expenseService.deleteExpense(id, user);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
