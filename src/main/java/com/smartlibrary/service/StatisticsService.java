package com.smartlibrary.service;

import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.Lending;
import com.smartlibrary.entity.User;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.LendingRepository;
import com.smartlibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    
    @Autowired
    private LendingRepository lendingRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 이번 달 대출 수
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long thisMonthBorrowed = lendingRepository.countByBorrowedAtAfter(startOfMonth);
        
        // 활성 사용자 수 (최근 30일 내 대출한 사용자)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long activeUsers = lendingRepository.findDistinctUserIdByBorrowedAtAfter(thirtyDaysAgo).size();
        
        // 평균 대출 기간 (반납된 도서 기준)
        List<Lending> returnedLendings = lendingRepository.findByStatus(Lending.LendingStatus.RETURNED);
        double avgBorrowDays = returnedLendings.stream()
                .filter(l -> l.getBorrowedAt() != null && l.getReturnedAt() != null)
                .mapToLong(l -> java.time.Duration.between(l.getBorrowedAt(), l.getReturnedAt()).toDays())
                .average()
                .orElse(0.0);
        
        // 연체율
        long totalActiveLendings = lendingRepository.countByStatus(Lending.LendingStatus.BORROWED);
        long overdueLendings = lendingRepository.countOverdueLendings(LocalDateTime.now());
        double overdueRate = totalActiveLendings > 0 ? (double) overdueLendings / totalActiveLendings * 100 : 0.0;
        
        stats.put("thisMonthBorrowed", thisMonthBorrowed);
        stats.put("activeUsers", activeUsers);
        stats.put("avgBorrowDays", Math.round(avgBorrowDays));
        stats.put("overdueRate", Math.round(overdueRate * 10.0) / 10.0);
        
        return stats;
    }
    
    public Map<String, Object> getMonthlyStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Map<String, Object>> monthlyData = new ArrayList<>();
        
        // 최근 6개월 데이터
        for (int i = 5; i >= 0; i--) {
            LocalDateTime monthStart = LocalDateTime.now().minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime monthEnd = monthStart.plusMonths(1);
            
            long borrowed = lendingRepository.countByBorrowedAtBetween(monthStart, monthEnd);
            long returned = lendingRepository.countByReturnedAtBetween(monthStart, monthEnd);
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthStart.format(DateTimeFormatter.ofPattern("M월")));
            monthData.put("borrowed", borrowed);
            monthData.put("returned", returned);
            
            monthlyData.add(monthData);
        }
        
        stats.put("monthlyStats", monthlyData);
        return stats;
    }
    
    public Map<String, Object> getPopularBooks() {
        Map<String, Object> stats = new HashMap<>();
        
        // 도서별 대출 횟수 계산
        Map<Long, Long> bookBorrowCounts = lendingRepository.findAll().stream()
                .collect(Collectors.groupingBy(l -> l.getBook().getId(), Collectors.counting()));
        
        // 상위 5개 도서
        List<Map<String, Object>> popularBooks = bookBorrowCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Book book = bookRepository.findById(entry.getKey()).orElse(null);
                    if (book != null) {
                        Map<String, Object> bookData = new HashMap<>();
                        bookData.put("title", book.getTitle());
                        bookData.put("author", book.getAuthor());
                        bookData.put("borrowCount", entry.getValue());
                        return bookData;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        stats.put("popularBooks", popularBooks);
        return stats;
    }
    
    public Map<String, Object> getCategoryStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 카테고리별 통계
        Map<String, List<Book>> booksByCategory = bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(Book::getCategory));
        
        List<Map<String, Object>> categoryStats = booksByCategory.entrySet().stream()
                .map(entry -> {
                    String category = entry.getKey();
                    List<Book> books = entry.getValue();
                    long total = books.size();
                    
                    // 해당 카테고리 도서의 대출 횟수
                    long borrowed = books.stream()
                            .mapToLong(book -> lendingRepository.countByBookId(book.getId()))
                            .sum();
                    
                    double percentage = total > 0 ? (double) borrowed / total * 100 : 0.0;
                    
                    Map<String, Object> categoryData = new HashMap<>();
                    categoryData.put("category", category);
                    categoryData.put("total", total);
                    categoryData.put("borrowed", borrowed);
                    categoryData.put("percentage", Math.round(percentage * 10.0) / 10.0);
                    
                    return categoryData;
                })
                .sorted((a, b) -> Double.compare((Double) b.get("percentage"), (Double) a.get("percentage")))
                .collect(Collectors.toList());
        
        stats.put("categoryStats", categoryStats);
        return stats;
    }
    
    public Map<String, Object> getDepartmentStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 사용자별 대출 횟수 계산
        Map<Long, Long> userBorrowCounts = lendingRepository.findAll().stream()
                .collect(Collectors.groupingBy(l -> l.getUser().getId(), Collectors.counting()));
        
        // 부서별 통계 (사용자의 role을 부서로 간주)
        Map<String, List<User>> usersByRole = userRepository.findAll().stream()
                .collect(Collectors.groupingBy(user -> user.getRole().name()));
        
        List<Map<String, Object>> departmentStats = usersByRole.entrySet().stream()
                .map(entry -> {
                    String department = entry.getKey();
                    List<User> users = entry.getValue();
                    long userCount = users.size();
                    
                    // 해당 부서 사용자들의 총 대출 횟수
                    long totalBorrowed = users.stream()
                            .mapToLong(user -> userBorrowCounts.getOrDefault(user.getId(), 0L))
                            .sum();
                    
                    Map<String, Object> deptData = new HashMap<>();
                    deptData.put("department", department);
                    deptData.put("users", userCount);
                    deptData.put("borrowed", totalBorrowed);
                    
                    return deptData;
                })
                .sorted((a, b) -> Long.compare((Long) b.get("borrowed"), (Long) a.get("borrowed")))
                .collect(Collectors.toList());
        
        stats.put("departmentStats", departmentStats);
        return stats;
    }
    
    public Map<String, Object> getGrowthStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 현재 월과 이전 월의 시작/끝 시간
        LocalDateTime currentMonthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime currentMonthEnd = currentMonthStart.plusMonths(1);
        LocalDateTime previousMonthStart = currentMonthStart.minusMonths(1);
        LocalDateTime previousMonthEnd = currentMonthStart;
        
        // 도서 현황 통계
        long totalCopies = bookRepository.findAll().stream()
                .mapToLong(Book::getTotalCopies)
                .sum();
        long availableCopies = bookRepository.findAll().stream()
                .mapToLong(Book::getAvailableCopies)
                .sum();
        
        // 이번 달 새로 추가된 도서 수 (created_at 기준)
        long newBooksThisMonth = bookRepository.findAll().stream()
                .filter(book -> book.getCreatedAt() != null && 
                        book.getCreatedAt().isAfter(currentMonthStart) && 
                        book.getCreatedAt().isBefore(currentMonthEnd))
                .count();
        
        // 지난 달 새로 추가된 도서 수
        long newBooksLastMonth = bookRepository.findAll().stream()
                .filter(book -> book.getCreatedAt() != null && 
                        book.getCreatedAt().isAfter(previousMonthStart) && 
                        book.getCreatedAt().isBefore(previousMonthEnd))
                .count();
        
        // 도서 증가율 계산
        double bookGrowthRate = newBooksLastMonth > 0 ? 
                ((double) (newBooksThisMonth - newBooksLastMonth) / newBooksLastMonth) * 100 : 0.0;
        
        // 사용자 현황 통계
        long currentTotalUsers = userRepository.count();
        long activeUsers = userRepository.findAll().stream()
                .filter(user -> user.getActive())
                .count();
        
        // 이번 달 새로 등록된 사용자 수
        long newUsersThisMonth = userRepository.findAll().stream()
                .filter(user -> user.getCreatedAt() != null && 
                        user.getCreatedAt().isAfter(currentMonthStart) && 
                        user.getCreatedAt().isBefore(currentMonthEnd))
                .count();
        
        // 지난 달 새로 등록된 사용자 수
        long newUsersLastMonth = userRepository.findAll().stream()
                .filter(user -> user.getCreatedAt() != null && 
                        user.getCreatedAt().isAfter(previousMonthStart) && 
                        user.getCreatedAt().isBefore(previousMonthEnd))
                .count();
        
        // 사용자 증가율 계산
        double userGrowthRate = newUsersLastMonth > 0 ? 
                ((double) (newUsersThisMonth - newUsersLastMonth) / newUsersLastMonth) * 100 : 0.0;
        
        // 대출 현황 통계
        long currentMonthBorrowed = lendingRepository.countByBorrowedAtBetween(currentMonthStart, currentMonthEnd);
        long previousMonthBorrowed = lendingRepository.countByBorrowedAtBetween(previousMonthStart, previousMonthEnd);
        
        // 대출 증가율 계산
        double borrowGrowthRate = previousMonthBorrowed > 0 ? 
                ((double) (currentMonthBorrowed - previousMonthBorrowed) / previousMonthBorrowed) * 100 : 0.0;
        
        // 반납 현황 통계
        long currentMonthReturned = lendingRepository.countByReturnedAtBetween(currentMonthStart, currentMonthEnd);
        long previousMonthReturned = lendingRepository.countByReturnedAtBetween(previousMonthStart, previousMonthEnd);
        
        // 반납 증가율 계산
        double returnGrowthRate = previousMonthReturned > 0 ? 
                ((double) (currentMonthReturned - previousMonthReturned) / previousMonthReturned) * 100 : 0.0;
        
        // 도서 현황
        Map<String, Object> bookStats = new HashMap<>();
        bookStats.put("totalBooks", totalCopies);
        bookStats.put("availableBooks", availableCopies);
        bookStats.put("newBooksThisMonth", newBooksThisMonth);
        bookStats.put("bookGrowthRate", Math.round(bookGrowthRate * 10.0) / 10.0);
        bookStats.put("utilizationRate", totalCopies > 0 ? 
                Math.round(((double) (totalCopies - availableCopies) / totalCopies) * 1000) / 10.0 : 0.0);
        
        // 사용자 현황
        Map<String, Object> userStats = new HashMap<>();
        userStats.put("totalUsers", currentTotalUsers);
        userStats.put("activeUsers", activeUsers);
        userStats.put("newUsersThisMonth", newUsersThisMonth);
        userStats.put("userGrowthRate", Math.round(userGrowthRate * 10.0) / 10.0);
        userStats.put("activationRate", currentTotalUsers > 0 ? 
                Math.round(((double) activeUsers / currentTotalUsers) * 100 * 10.0) / 10.0 : 0.0);
        
        // 대출 현황
        Map<String, Object> lendingStats = new HashMap<>();
        lendingStats.put("currentMonthBorrowed", currentMonthBorrowed);
        lendingStats.put("previousMonthBorrowed", previousMonthBorrowed);
        lendingStats.put("borrowGrowthRate", Math.round(borrowGrowthRate * 10.0) / 10.0);
        lendingStats.put("currentMonthReturned", currentMonthReturned);
        lendingStats.put("previousMonthReturned", previousMonthReturned);
        lendingStats.put("returnGrowthRate", Math.round(returnGrowthRate * 10.0) / 10.0);
        
        stats.put("bookStats", bookStats);
        stats.put("userStats", userStats);
        stats.put("lendingStats", lendingStats);
        
        return stats;
    }
} 