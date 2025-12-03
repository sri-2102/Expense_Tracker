package com.expense.tracker.repository;

import com.expense.tracker.entity.Budget;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    List<Budget> findByUser(User user);
    
    List<Budget> findByUserAndMonthAndYear(User user, Integer month, Integer year);
    
    Optional<Budget> findByUserAndCategoryAndMonthAndYear(
            User user, Expense.Category category, Integer month, Integer year);
    
    boolean existsByUserAndCategoryAndMonthAndYear(
            User user, Expense.Category category, Integer month, Integer year);
}
