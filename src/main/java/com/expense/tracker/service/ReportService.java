package com.expense.tracker.service;

import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.ExpenseRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private ChartService chartService;
    
    public byte[] generateMonthlyReport(User user, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        return generateReport(user, startDate, endDate, "Monthly Report - " + month + "/" + year);
    }
    
    public byte[] generateYearlyReport(User user, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        return generateReport(user, startDate, endDate, "Yearly Report - " + year);
    }
    
    private byte[] generateReport(User user, LocalDate startDate, LocalDate endDate, String title) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Title
            Paragraph titlePara = new Paragraph(title)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titlePara);
            
            // User info
            document.add(new Paragraph("User: " + user.getUsername()));
            document.add(new Paragraph("Period: " + startDate.format(DateTimeFormatter.ISO_DATE) + 
                    " to " + endDate.format(DateTimeFormatter.ISO_DATE)));
            document.add(new Paragraph("\n"));
            
            // Total expenses
            BigDecimal totalExpenses = expenseRepository.getTotalExpensesByUserAndDateRange(user, startDate, endDate);
            document.add(new Paragraph("Total Expenses: $" + (totalExpenses != null ? totalExpenses : BigDecimal.ZERO))
                    .setFontSize(14)
                    .setBold());
            document.add(new Paragraph("\n"));
            
            // Expenses by category
            document.add(new Paragraph("Expenses by Category:").setFontSize(14).setBold());
            List<Object[]> categoryExpenses = expenseRepository.getExpensesByCategoryAndDateRange(user, startDate, endDate);
            
            if (!categoryExpenses.isEmpty()) {
                Table categoryTable = new Table(2);
                categoryTable.addHeaderCell("Category");
                categoryTable.addHeaderCell("Amount");
                
                for (Object[] row : categoryExpenses) {
                    Expense.Category category = (Expense.Category) row[0];
                    BigDecimal amount = (BigDecimal) row[1];
                    categoryTable.addCell(category.name());
                    categoryTable.addCell("$" + amount.toString());
                }
                
                document.add(categoryTable);
            } else {
                document.add(new Paragraph("No expenses found for this period."));
            }
            
            document.add(new Paragraph("\n"));
            
            // Detailed expenses list
            document.add(new Paragraph("Detailed Expenses:").setFontSize(14).setBold());
            List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate);
            
            if (!expenses.isEmpty()) {
                Table expenseTable = new Table(4);
                expenseTable.addHeaderCell("Date");
                expenseTable.addHeaderCell("Description");
                expenseTable.addHeaderCell("Category");
                expenseTable.addHeaderCell("Amount");
                
                for (Expense expense : expenses) {
                    expenseTable.addCell(expense.getDate().format(DateTimeFormatter.ISO_DATE));
                    expenseTable.addCell(expense.getDescription());
                    expenseTable.addCell(expense.getCategory().name());
                    expenseTable.addCell("$" + expense.getAmount().toString());
                }
                
                document.add(expenseTable);
            }
            
            document.close();
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
