package com.smartlibrary.controller;

import com.smartlibrary.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:3000")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverviewStats() {
        return ResponseEntity.ok(statisticsService.getOverviewStats());
    }
    
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyStats() {
        return ResponseEntity.ok(statisticsService.getMonthlyStats());
    }
    
    @GetMapping("/popular-books")
    public ResponseEntity<Map<String, Object>> getPopularBooks() {
        return ResponseEntity.ok(statisticsService.getPopularBooks());
    }
    
    @GetMapping("/category-stats")
    public ResponseEntity<Map<String, Object>> getCategoryStats() {
        return ResponseEntity.ok(statisticsService.getCategoryStats());
    }
    
    @GetMapping("/department-stats")
    public ResponseEntity<Map<String, Object>> getDepartmentStats() {
        return ResponseEntity.ok(statisticsService.getDepartmentStats());
    }
    
    @GetMapping("/growth-stats")
    public ResponseEntity<Map<String, Object>> getGrowthStats() {
        return ResponseEntity.ok(statisticsService.getGrowthStats());
    }
} 