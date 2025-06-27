package com.smartlibrary.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lendings")
public class Lending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Column(nullable = false)
    private LocalDateTime borrowedAt;
    @Column(nullable = false)
    private LocalDateTime dueDate;
    @Column
    private LocalDateTime returnedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LendingStatus status = LendingStatus.BORROWED;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Lending() {}

    public Lending(Long id, User user, Book book, LocalDateTime borrowedAt, LocalDateTime dueDate, LocalDateTime returnedAt, LendingStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public LocalDateTime getBorrowedAt() { return borrowedAt; }
    public void setBorrowedAt(LocalDateTime borrowedAt) { this.borrowedAt = borrowedAt; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
    public void setReturnedAt(LocalDateTime returnedAt) { this.returnedAt = returnedAt; }
    public LendingStatus getStatus() { return status; }
    public void setStatus(LendingStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum LendingStatus {
        BORROWED, RETURNED, OVERDUE
    }
} 