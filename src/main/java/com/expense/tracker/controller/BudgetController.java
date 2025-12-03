package com.expense.tracker.controller;

import com.expense.tracker.dto.BudgetRequest;
import com.expense.tracker.dto.BudgetResponse;
import com.expense.tracker.entity.User;
import com.expense.tracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {
    
    @Autowired
    private BudgetService budgetService;
    
    @PostMapping
    public ResponseEntity<?> createBudget(@Valid @RequestBody BudgetRequest request,
                                         @AuthenticationPrincipal User user) {
        try {
            BudgetResponse response = budgetService.createBudget(request, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(@AuthenticationPrincipal User user) {
        List<BudgetResponse> budgets = budgetService.getAllBudgets(user);
        return ResponseEntity.ok(budgets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetById(@PathVariable Long id,
                                          @AuthenticationPrincipal User user) {
        try {
            BudgetResponse response = budgetService.getBudgetById(id, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/period")
    public ResponseEntity<List<BudgetResponse>> getBudgetsByMonthAndYear(
            @RequestParam Integer month,
            @RequestParam Integer year,
            @AuthenticationPrincipal User user) {
        List<BudgetResponse> budgets = budgetService.getBudgetsByMonthAndYear(user, month, year);
        return ResponseEntity.ok(budgets);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBudget(@PathVariable Long id,
                                         @Valid @RequestBody BudgetRequest request,
                                         @AuthenticationPrincipal User user) {
        try {
            BudgetResponse response = budgetService.updateBudget(id, request, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable Long id,
                                         @AuthenticationPrincipal User user) {
        try {
            budgetService.deleteBudget(id, user);
            return ResponseEntity.ok("Budget deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
