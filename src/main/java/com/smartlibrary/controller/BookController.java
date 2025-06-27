package com.smartlibrary.controller;

import com.smartlibrary.dto.BookDto;
import com.smartlibrary.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }
    
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDto>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }
    
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDto>> searchBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<BookDto>> getBooksByCategory(@PathVariable String category) {
        return ResponseEntity.ok(bookService.getBooksByCategory(category));
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<BookDto>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }
    
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.createBook(bookDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        return bookService.updateBook(id, bookDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalBooksCount() {
        return ResponseEntity.ok(bookService.getTotalBooksCount());
    }
    
    @GetMapping("/stats/available")
    public ResponseEntity<Long> getAvailableBooksCount() {
        return ResponseEntity.ok(bookService.getAvailableBooksCount());
    }
} 