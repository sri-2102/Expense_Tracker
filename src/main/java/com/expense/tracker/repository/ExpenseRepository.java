package com.expense.tracker.repository;

import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    List<Expense> findByUser(User user);
    
    List<Expense> findByUserOrderByDateDesc(User user);
    
    List<Expense> findByUserAndCategory(User user, Expense.Category category);
    
    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    
    List<Expense> findByUserAndCategoryAndDateBetween(
            User user, Expense.Category category, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpensesByUserAndDateRange(
            @Param("user") User user, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.date BETWEEN :startDate AND :endDate GROUP BY e.category")
    List<Object[]> getExpensesByCategoryAndDateRange(
            @Param("user") User user, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.category = :category AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpensesByCategoryAndDateRange(
            @Param("user") User user,
            @Param("category") Expense.Category category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
