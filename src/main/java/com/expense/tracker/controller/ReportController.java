package com.expense.tracker.controller;

import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.service.ChartService;
import com.expense.tracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private ChartService chartService;
    
    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<byte[]> generateMonthlyPdfReport(
            @PathVariable Integer year,
            @PathVariable Integer month,
            @AuthenticationPrincipal User user) {
        try {
            byte[] pdfBytes = reportService.generateMonthlyReport(user, month, year);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "monthly-report-" + month + "-" + year + ".pdf");
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/yearly/{year}")
    public ResponseEntity<byte[]> generateYearlyPdfReport(
            @PathVariable Integer year,
            @AuthenticationPrincipal User user) {
        try {
            byte[] pdfBytes = reportService.generateYearlyReport(user, year);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "yearly-report-" + year + ".pdf");
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/chart/pie")
    public ResponseEntity<byte[]> generatePieChart(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal User user) {
        try {
            byte[] chartBytes = chartService.generateExpensePieChart(user, startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(chartBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/chart/category")
    public ResponseEntity<byte[]> generateCategoryChart(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal User user) {
        try {
            byte[] chartBytes = chartService.generateExpensePieChart(user, startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(chartBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/expenses-by-category")
    public ResponseEntity<Map<Expense.Category, BigDecimal>> getExpensesByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal User user) {
        try {
            Map<Expense.Category, BigDecimal> expenses = chartService.getExpensesByCategory(user, startDate, endDate);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
