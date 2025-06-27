package com.smartlibrary.repository;

import com.smartlibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    List<Book> findByCategory(String category);
    
    Optional<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    List<Book> findAvailableBooks();
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.availableCopies > 0")
    Long countAvailableBooks();
    
    @Query("SELECT COUNT(b) FROM Book b")
    Long countTotalBooks();
} 