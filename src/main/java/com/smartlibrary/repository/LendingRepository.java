package com.smartlibrary.repository;

import com.smartlibrary.entity.Lending;
import com.smartlibrary.entity.Lending.LendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.query.Param;

@Repository
public interface LendingRepository extends JpaRepository<Lending, Long> {
    
    List<Lending> findByUserId(Long userId);
    
    List<Lending> findByBookId(Long bookId);
    
    List<Lending> findByStatus(LendingStatus status);
    
    @Query("SELECT l FROM Lending l WHERE l.dueDate < :now AND l.status = 'BORROWED'")
    List<Lending> findOverdueLendings(@Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(l) FROM Lending l WHERE l.status = 'BORROWED'")
    Long countActiveLendings();
    
    @Query("SELECT COUNT(l) FROM Lending l WHERE l.dueDate < :now AND l.status = 'BORROWED'")
    Long countOverdueLendings(@Param("now") LocalDateTime now);
    
    // 통계용 메서드들
    Long countByBorrowedAtAfter(LocalDateTime date);
    
    Long countByBorrowedAtBetween(LocalDateTime start, LocalDateTime end);
    
    Long countByReturnedAtBetween(LocalDateTime start, LocalDateTime end);
    
    Long countByBookId(Long bookId);
    
    Long countByStatus(LendingStatus status);
    
    @Query("SELECT DISTINCT l.user.id FROM Lending l WHERE l.borrowedAt > :date")
    List<Long> findDistinctUserIdByBorrowedAtAfter(@Param("date") LocalDateTime date);
} 