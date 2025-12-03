package com.expense.tracker.dto;

import com.expense.tracker.entity.Budget;
import com.expense.tracker.entity.Expense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {
    
    private Long id;
    private Expense.Category category;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private Integer month;
    private Integer year;
    private Integer alertThreshold;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static BudgetResponse fromEntity(Budget budget, BigDecimal spentAmount) {
        return new BudgetResponse(
                budget.getId(),
                budget.getCategory(),
                budget.getLimitAmount(),
                spentAmount != null ? spentAmount : BigDecimal.ZERO,
                budget.getMonth(),
                budget.getYear(),
                budget.getAlertThreshold(),
                budget.getCreatedAt(),
                budget.getUpdatedAt()
        );
    }
}
