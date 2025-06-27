package com.smartlibrary.service;

import com.smartlibrary.dto.BookDto;
import com.smartlibrary.entity.Book;
import com.smartlibrary.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return convertToDto(book);
    }
    
    public List<BookDto> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BookDto> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BookDto> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BookDto> getAvailableBooks() {
        return bookRepository.findAvailableBooks().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BookDto createBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setDescription(bookDto.getDescription());
        book.setCategory(bookDto.getCategory());
        book.setTotalCopies(bookDto.getTotalCopies());
        book.setAvailableCopies(bookDto.getTotalCopies());
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }
    
    public Optional<BookDto> updateBook(Long id, BookDto bookDto) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDto.getTitle());
                    book.setAuthor(bookDto.getAuthor());
                    book.setIsbn(bookDto.getIsbn());
                    book.setDescription(bookDto.getDescription());
                    book.setCategory(bookDto.getCategory());
                    book.setTotalCopies(bookDto.getTotalCopies());
                    book.setAvailableCopies(bookDto.getAvailableCopies());
                    return convertToDto(bookRepository.save(book));
                });
    }
    
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Long getTotalBooksCount() {
        return bookRepository.countTotalBooks();
    }
    
    public Long getAvailableBooksCount() {
        return bookRepository.countAvailableBooks();
    }
    
    private BookDto convertToDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getDescription(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
} 