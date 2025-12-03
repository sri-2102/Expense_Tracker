package com.expense.tracker.service;

import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.ExpenseRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    public byte[] generateExpensePieChart(User user, LocalDate startDate, LocalDate endDate) throws IOException {
        List<Object[]> categoryExpenses = expenseRepository.getExpensesByCategoryAndDateRange(user, startDate, endDate);
        
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Object[] row : categoryExpenses) {
            Expense.Category category = (Expense.Category) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            dataset.setValue(category.name(), amount.doubleValue());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Expenses by Category",
                dataset,
                true,  // legend
                true,  // tooltips
                false  // urls
        );
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(outputStream, chart, 800, 600);
        return outputStream.toByteArray();
    }
    
    public Map<Expense.Category, BigDecimal> getExpensesByCategory(User user, LocalDate startDate, LocalDate endDate) {
        List<Object[]> categoryExpenses = expenseRepository.getExpensesByCategoryAndDateRange(user, startDate, endDate);
        
        return categoryExpenses.stream()
                .collect(Collectors.toMap(
                        row -> (Expense.Category) row[0],
                        row -> (BigDecimal) row[1]
                ));
    }
}
