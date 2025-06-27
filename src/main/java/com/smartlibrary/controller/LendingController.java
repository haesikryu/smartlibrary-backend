package com.smartlibrary.controller;

import com.smartlibrary.dto.LendingDto;
import com.smartlibrary.service.LendingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/lendings")
@CrossOrigin(origins = "http://localhost:3000")
public class LendingController {
    
    @Autowired
    private LendingService lendingService;
    
    @GetMapping
    public ResponseEntity<List<LendingDto>> getAllLendings() {
        return ResponseEntity.ok(lendingService.getAllLendings());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LendingDto> getLendingById(@PathVariable Long id) {
        LendingDto lending = lendingService.getLendingById(id);
        return ResponseEntity.ok(lending);
    }
    
    @PostMapping
    public ResponseEntity<LendingDto> createLending(@RequestBody LendingDto lendingDto) {
        return ResponseEntity.ok(lendingService.createLending(lendingDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LendingDto> updateLending(@PathVariable Long id, @RequestBody LendingDto lendingDto) {
        return lendingService.updateLending(id, lendingDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLending(@PathVariable Long id) {
        lendingService.deleteLending(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/return")
    public ResponseEntity<LendingDto> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(lendingService.returnBook(id));
    }
    
    @PutMapping("/{id}/extend")
    public ResponseEntity<LendingDto> extendLending(@PathVariable Long id) {
        return ResponseEntity.ok(lendingService.extendLending(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LendingDto>> getLendingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(lendingService.getLendingsByUser(userId));
    }
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LendingDto>> getLendingsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(lendingService.getLendingsByBook(bookId));
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<LendingDto>> getOverdueLendings() {
        return ResponseEntity.ok(lendingService.getOverdueLendings());
    }
    
    @GetMapping("/stats/active")
    public ResponseEntity<Long> getActiveLendingsCount() {
        return ResponseEntity.ok(lendingService.getActiveLendingsCount());
    }
    
    @GetMapping("/stats/overdue")
    public ResponseEntity<Long> getOverdueLendingsCount() {
        return ResponseEntity.ok(lendingService.getOverdueLendingsCount());
    }
} 