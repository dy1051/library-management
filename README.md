# Library Management

## 프로젝트 소개

[Activity-Book-Management](https://github.com/honghyunin/Activity-Book-Management)를 참고해 구현한 도서관리 시스템입니다.
Spring Security, JPA, DDD 설계를 학습하고 적용하는 것을 목표로 진행했습니다.

## 사용 기술

- Java 21, Spring Boot 4.1.2, Gradle
- Spring Data JPA, H2 Database
- Spring Security, JWT(JJWT 0.13.0)
- Validation, Lombok

## 구현 기능

### 회원

- 회원가입
- JWT 로그인
- 내 정보 및 회원 번호로 조회
- 비밀번호 변경
- 회원탈퇴
- 내 대여 목록 조회

### 도서

- 도서 등록·수정·삭제
- 전체 도서 목록 조회
- 제목·카테고리 검색
- 대여 가능한 도서 조회

### 대여·반납

- 도서 대여
- 도서 반납
- 반납 예정일 연장
- 대여·반납 시 도서 상태 및 회원 대출 권수 변경
- 본인의 대여만 반납·연장 가능

## 추후 구현

- 로그아웃, Refresh Token, Redis
- 역할별 권한 제어
- 자동화 테스트 및 동시성 제어
- 반납 이력 보존 및 논리 삭제
