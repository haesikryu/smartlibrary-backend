package com.smartlibrary.service;

import com.smartlibrary.dto.LendingDto;
import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.Lending;
import com.smartlibrary.entity.User;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.LendingRepository;
import com.smartlibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LendingService {
    
    @Autowired
    private LendingRepository lendingRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<LendingDto> getAllLendings() {
        return lendingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public LendingDto getLendingById(Long id) {
        Lending lending = lendingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lending not found"));
        return convertToDto(lending);
    }
    
    @Transactional
    public LendingDto createLending(LendingDto lendingDto) {
        Book book = bookRepository.findById(lendingDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        
        User user = userRepository.findById(lendingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies");
        }
        
        Lending lending = new Lending();
        lending.setBook(book);
        lending.setUser(user);
        lending.setBorrowedAt(LocalDateTime.now());
        lending.setDueDate(lendingDto.getDueDate());
        lending.setStatus(Lending.LendingStatus.BORROWED);
        
        // Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        
        Lending savedLending = lendingRepository.save(lending);
        return convertToDto(savedLending);
    }
    
    @Transactional
    public Optional<LendingDto> updateLending(Long id, LendingDto lendingDto) {
        return lendingRepository.findById(id)
                .map(lending -> {
                    lending.setDueDate(lendingDto.getDueDate());
                    lending.setStatus(Lending.LendingStatus.valueOf(lendingDto.getStatus()));
                    return convertToDto(lendingRepository.save(lending));
                });
    }
    
    @Transactional
    public void deleteLending(Long id) {
        Lending lending = lendingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lending not found"));
        
        // Increase available copies if book was borrowed
        if (lending.getStatus() == Lending.LendingStatus.BORROWED) {
            Book book = lending.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }
        
        lendingRepository.deleteById(id);
    }
    
    @Transactional
    public LendingDto returnBook(Long id) {
        Lending lending = lendingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lending not found"));
        
        if (lending.getStatus() != Lending.LendingStatus.BORROWED) {
            throw new RuntimeException("Book is not currently borrowed");
        }
        
        lending.setStatus(Lending.LendingStatus.RETURNED);
        lending.setReturnedAt(LocalDateTime.now());
        
        // Increase available copies
        Book book = lending.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        
        Lending savedLending = lendingRepository.save(lending);
        return convertToDto(savedLending);
    }
    
    @Transactional
    public LendingDto extendLending(Long id) {
        Lending lending = lendingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lending not found"));
        
        if (lending.getStatus() != Lending.LendingStatus.BORROWED) {
            throw new RuntimeException("Book is not currently borrowed");
        }
        
        // Extend due date by 7 days
        LocalDateTime newDueDate = lending.getDueDate().plusDays(7);
        lending.setDueDate(newDueDate);
        
        Lending savedLending = lendingRepository.save(lending);
        return convertToDto(savedLending);
    }
    
    public List<LendingDto> getLendingsByUser(Long userId) {
        return lendingRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<LendingDto> getLendingsByBook(Long bookId) {
        return lendingRepository.findByBookId(bookId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<LendingDto> getOverdueLendings() {
        return lendingRepository.findOverdueLendings(LocalDateTime.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Long getActiveLendingsCount() {
        return lendingRepository.countActiveLendings();
    }
    
    public Long getOverdueLendingsCount() {
        return lendingRepository.countOverdueLendings(LocalDateTime.now());
    }
    
    private LendingDto convertToDto(Lending lending) {
        return new LendingDto(
                lending.getId(),
                lending.getUser().getId(),
                lending.getUser().getName(),
                lending.getBook().getId(),
                lending.getBook().getTitle(),
                lending.getBorrowedAt(),
                lending.getDueDate(),
                lending.getReturnedAt(),
                lending.getStatus().name(),
                lending.getCreatedAt(),
                lending.getUpdatedAt()
        );
    }
} 