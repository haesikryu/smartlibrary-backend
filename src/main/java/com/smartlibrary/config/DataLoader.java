package com.smartlibrary.config;

import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.User;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 제목을 기반으로 카테고리를 자동 분류하는 메서드
    private String determineCategory(String title) {
        String lowerTitle = title.toLowerCase();
        
        if (lowerTitle.contains("spring") || lowerTitle.contains("java") || lowerTitle.contains("코드") || 
            lowerTitle.contains("프로그래밍") || lowerTitle.contains("개발") || lowerTitle.contains("알고리즘")) {
            return "프로그래밍";
        } else if (lowerTitle.contains("데이터베이스") || lowerTitle.contains("db") || lowerTitle.contains("sql") || 
                   lowerTitle.contains("nosql") || lowerTitle.contains("mongo") || lowerTitle.contains("redis")) {
            return "데이터베이스";
        } else if (lowerTitle.contains("네트워크") || lowerTitle.contains("http") || lowerTitle.contains("tcp") || 
                   lowerTitle.contains("보안") || lowerTitle.contains("암호화")) {
            return "네트워크";
        } else if (lowerTitle.contains("운영체제") || lowerTitle.contains("os") || lowerTitle.contains("리눅스") || 
                   lowerTitle.contains("윈도우") || lowerTitle.contains("unix")) {
            return "운영체제";
        } else if (lowerTitle.contains("웹") || lowerTitle.contains("html") || lowerTitle.contains("css") || 
                   lowerTitle.contains("javascript") || lowerTitle.contains("react") || lowerTitle.contains("vue")) {
            return "웹개발";
        } else if (lowerTitle.contains("모바일") || lowerTitle.contains("안드로이드") || lowerTitle.contains("ios") || 
                   lowerTitle.contains("flutter") || lowerTitle.contains("react native")) {
            return "모바일개발";
        } else if (lowerTitle.contains("인공지능") || lowerTitle.contains("ai") || lowerTitle.contains("머신러닝") || 
                   lowerTitle.contains("딥러닝") || lowerTitle.contains("tensorflow") || lowerTitle.contains("pytorch")) {
            return "인공지능";
        } else {
            return "기타";
        }
    }
    
    @Override
    public void run(String... args) throws Exception {
        // 샘플 사용자 데이터 추가
        if (userRepository.count() == 0) {
            createUser("admin", "admin123", "관리자", "admin@library.com", "010-0000-0000", User.UserRole.ADMIN);
            createUser("kim_dev", "dev123", "김개발", "kim.dev@company.com", "010-1111-1111", User.UserRole.USER);
            createUser("lee_design", "design123", "이디자인", "lee.design@company.com", "010-2222-2222", User.UserRole.USER);
            createUser("park_qa", "qa123", "박테스트", "park.qa@company.com", "010-3333-3333", User.UserRole.USER);
            createUser("choi_ops", "ops123", "최운영", "choi.ops@company.com", "010-4444-4444", User.UserRole.USER);
            createUser("jung_data", "data123", "정데이터", "jung.data@company.com", "010-5555-5555", User.UserRole.USER);
            createUser("yoon_ai", "ai123", "윤인공지능", "yoon.ai@company.com", "010-6666-6666", User.UserRole.USER);
            createUser("han_mobile", "mobile123", "한모바일", "han.mobile@company.com", "010-7777-7777", User.UserRole.USER);
            createUser("shin_web", "web123", "신웹개발", "shin.web@company.com", "010-8888-8888", User.UserRole.USER);
            createUser("baek_student", "student123", "백학생", "baek.student@university.edu", "010-9999-9999", User.UserRole.USER);
        }
        
        // 샘플 도서 데이터 추가 (위키북 도서 목록 기반)
        if (bookRepository.count() == 0) {
            // 프로그래밍 관련 도서들
            createBook("클린 코드", "로버트 C. 마틴", "9788966262472", 
                      "깨끗하고 유지보수하기 쉬운 코드를 작성하는 방법을 다룹니다.", 5);
            
            createBook("리팩터링", "마틴 파울러", "9788966262427", 
                      "기존 코드의 구조를 개선하는 방법을 설명합니다.", 3);
            
            createBook("Spring Boot 실전 활용", "김영한", "9788966262434", 
                      "Spring Boot를 활용한 웹 애플리케이션 개발 가이드", 4);
            
            createBook("JPA 프로그래밍", "김영한", "9788966262441", 
                      "JPA와 Hibernate를 활용한 데이터 접근 기술", 2);
            
            createBook("이펙티브 자바", "조슈아 블로크", "9788966262281", 
                      "Java 언어의 핵심 기능과 모범 사례를 다룹니다.", 3);
            
            createBook("코드 컴플리트", "스티브 맥코넬", "9788966262250", 
                      "소프트웨어 구축에 관한 실용적인 가이드", 4);
            
            createBook("객체지향의 사실과 오해", "조영호", "9788998139766", 
                      "객체지향 프로그래밍의 본질을 이해하는 책", 3);
            
            // 데이터베이스 관련 도서들
            createBook("SQL 첫걸음", "아사이 아츠시", "9788968482311", 
                      "SQL 기초부터 실무까지 체계적으로 학습", 3);
            
            createBook("Redis 운영과 관리", "김종민", "9788968482328", 
                      "Redis 인메모리 데이터베이스 운영 가이드", 2);
            
            createBook("MongoDB 완벽 가이드", "크리스티나 초도로우", "9788968482335", 
                      "NoSQL 데이터베이스 MongoDB 완벽 가이드", 3);
            
            // 네트워크 관련 도서들
            createBook("HTTP 완벽 가이드", "데이빗 고울리", "9788968482342", 
                      "HTTP 프로토콜의 모든 것을 다룹니다", 3);
            
            createBook("네트워크 보안", "윌리엄 스탤링", "9788968482359", 
                      "네트워크 보안의 기본 개념과 실무", 2);
            
            // 웹개발 관련 도서들
            createBook("모던 자바스크립트", "이선 브라운", "9788968482366", 
                      "ES6+ 문법과 모던 JavaScript 개발", 4);
            
            createBook("React 실전 활용", "알렉스 뱅크스", "9788968482373", 
                      "React를 활용한 현대적인 웹 개발", 3);
            
            createBook("Vue.js 완벽 가이드", "에반 유", "9788968482380", 
                      "Vue.js 프레임워크 완벽 가이드", 3);
            
            // 모바일개발 관련 도서들
            createBook("안드로이드 프로그래밍", "빌 필립스", "9788968482397", 
                      "안드로이드 앱 개발 완벽 가이드", 3);
            
            createBook("iOS 프로그래밍", "매트 뉴버그", "9788968482403", 
                      "iOS 앱 개발을 위한 Swift 가이드", 3);
            
            // 인공지능 관련 도서들
            createBook("머신러닝 입문", "세바스찬 라시카", "9788968482410", 
                      "머신러닝의 기본 개념과 알고리즘", 3);
            
            createBook("딥러닝 첫걸음", "요시다 유타", "9788968482427", 
                      "딥러닝의 기초부터 실전까지", 3);
            
            createBook("파이썬으로 배우는 인공지능", "김성훈", "9788968482434", 
                      "파이썬을 활용한 AI/ML 프로젝트", 4);
            
            // 운영체제 관련 도서들
            createBook("운영체제", "에이브러햄 실버샤츠", "9788968482441", 
                      "운영체제의 기본 개념과 구현", 3);
            
            createBook("리눅스 커널", "로버트 러브", "9788968482458", 
                      "리눅스 커널의 구조와 동작 원리", 2);
            
            // 알고리즘 관련 도서들
            createBook("알고리즘 문제 해결 전략", "구종만", "9788968482465", 
                      "알고리즘 문제 해결을 위한 전략", 3);
            
            createBook("프로그래밍 대회에서 배우는 알고리즘", "스티븐 할림", "9788968482472", 
                      "경쟁 프로그래밍을 통한 알고리즘 학습", 3);
        }
    }
    
    private void createUser(String username, String password, String name, String email, String phone, User.UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    private void createBook(String title, String author, String isbn, String description, int copies) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setDescription(description);
        book.setCategory(determineCategory(title));
        book.setTotalCopies(copies);
        book.setAvailableCopies(copies); // 초기에는 모든 권수가 대출 가능
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }
} 