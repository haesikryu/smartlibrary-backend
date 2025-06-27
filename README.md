# SmartLibrary Backend

Spring Boot + JPA 기반의 도서 관리 시스템 백엔드 API

## 기술 스택

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (개발용)
- **Gradle**
- **Java 17**
- **Lombok**

## 프로젝트 구조

```
src/main/java/com/smartlibrary/
├── SmartLibraryApplication.java
├── config/
│   ├── SecurityConfig.java
│   └── DataLoader.java
├── controller/
│   └── BookController.java
├── dto/
│   ├── BookDto.java
│   ├── UserDto.java
│   └── LendingDto.java
├── entity/
│   ├── Book.java
│   ├── User.java
│   └── Lending.java
├── repository/
│   ├── BookRepository.java
│   ├── UserRepository.java
│   └── LendingRepository.java
└── service/
    └── BookService.java
```

## 실행 방법

### 1. Java 17 설치 확인
```bash
java -version
```

### 2. 프로젝트 빌드
```bash
./gradlew build
```

### 3. 애플리케이션 실행
```bash
./gradlew bootRun
```

또는

```bash
java -jar build/libs/smart-library-backend-0.0.1-SNAPSHOT.jar
```

## API 엔드포인트

### 도서 관리 API

- `GET /api/books` - 모든 도서 조회
- `GET /api/books/{id}` - 특정 도서 조회
- `GET /api/books/search/title?title={title}` - 제목으로 도서 검색
- `GET /api/books/search/author?author={author}` - 저자로 도서 검색
- `GET /api/books/category/{category}` - 카테고리별 도서 조회
- `GET /api/books/available` - 대출 가능한 도서 조회
- `POST /api/books` - 새 도서 등록
- `PUT /api/books/{id}` - 도서 정보 수정
- `DELETE /api/books/{id}` - 도서 삭제
- `GET /api/books/stats/total` - 전체 도서 수
- `GET /api/books/stats/available` - 대출 가능한 도서 수

## 데이터베이스

- **H2 Database** (인메모리)
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비어있음)

## CORS 설정

프론트엔드 (http://localhost:3000)에서의 요청을 허용하도록 CORS가 설정되어 있습니다.

## 샘플 데이터

애플리케이션 시작 시 다음 도서들이 자동으로 추가됩니다:

1. 클린 코드 - 로버트 C. 마틴
2. 리팩터링 - 마틴 파울러
3. Spring Boot 실전 활용 - 김영한
4. JPA 프로그래밍 - 김영한 