# Library Management

[Activity-Book-Management](https://github.com/honghyunin/Activity-Book-Management)를 참고해 도서관리 서버를 직접 구현한 학습 프로젝트입니다. 도메인별 책임 분리, Spring Security 기반 JWT 인증, JPA 연관관계와 변경 감지를 연습했습니다.

## 구현 기능

| 영역 | 구현 내용 |
| --- | --- |
| 회원 | 회원가입, 내 정보·회원 번호 조회, 현재 비밀번호 확인 후 비밀번호 변경, 회원탈퇴 |
| 인증 | BCrypt 비밀번호 해시, 로그인, JWT Access Token 발급·검증 |
| 도서 | 등록·전체 조회·수정·실제 삭제, 제목·카테고리 완전 일치 검색, 대여 가능 목록 |
| 대여 | 대여, 내 대여 목록, 본인 대여 반납·반납 예정일 연장 |
| 공통 | 요청 DTO 검증, 응답 DTO 분리, 예외별 HTTP 응답 |

로그아웃·Refresh Token·Redis는 아직 구현하지 않았습니다.

## 기술 스택

Java 21, Spring Boot 4.1.2, Spring Web MVC, Spring Security, Spring Data JPA, H2 파일 DB, JJWT 0.13.0, Gradle, Lombok.

## 실행 방법

JDK 21과 Git이 필요합니다. H2는 애플리케이션에 포함되어 별도 서버를 실행하지 않습니다.

```bash
git clone https://github.com/dy1051/library-management.git
cd library-management
mkdir -p ~/tools/db

# 로컬용 서명키 생성. 같은 키를 사용해야 재시작 전 발급한 토큰도 검증할 수 있습니다.
export JWT_SECRET="$(openssl rand -base64 32)"

./gradlew bootRun
```

IntelliJ로 실행할 때는 애플리케이션 실행 설정의 환경 변수에 `JWT_SECRET`을 등록합니다. 키는 Base64로 인코딩된 32바이트 이상의 무작위 값으로 준비하고 저장소에 커밋하지 않습니다.

| 설정 | 현재 값 |
| --- | --- |
| 애플리케이션 | http://localhost:8080 |
| H2 JDBC URL | `jdbc:h2:~/tools/db/library` |
| H2 사용자 / 비밀번호 | `sa` / 빈 값 |
| H2 콘솔 | http://localhost:8080/h2-console |
| Access Token 유효시간 | 30분 |
| 스키마 관리 | `ddl-auto: update` |
| OSIV | 비활성화 |

현재 설정은 로컬 학습용입니다. 파일 DB의 데이터는 재시작 후에도 유지됩니다.

## 요청 예시

회원가입:

```http
POST /api/members
Content-Type: application/json

{
  "loginId": "test01",
  "password": "test1234!",
  "name": "테스트"
}
```

로그인:

```http
POST /api/auth/login
Content-Type: application/json

{
  "loginId": "test01",
  "password": "test1234!"
}
```

로그인 응답의 `accessToken`을 이후 요청에 전달합니다.

```http
GET /api/members/me
Authorization: Bearer <accessToken>
```

Postman에서는 Authorization → Bearer Token에 토큰 값만 입력합니다.

## API

회원가입·로그인은 인증 없이 호출할 수 있습니다. 아래의 나머지 업무 API는 JWT 인증이 필요합니다.

| Method | 경로 | 기능 |
| --- | --- | --- |
| POST | `/api/members` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| GET | `/api/members/me` | 내 정보 |
| GET | `/api/members/{memberId}` | 회원 번호 조회 |
| PATCH | `/api/members/me/password` | 비밀번호 변경 |
| DELETE | `/api/members/me` | 회원탈퇴 |
| POST | `/api/books` | 도서 등록 |
| GET | `/api/books` | 도서 목록 |
| PUT | `/api/books/{bookId}` | 도서 수정 |
| DELETE | `/api/books/{bookId}` | 도서 실제 삭제 |
| GET | `/api/books/title?title=...` | 제목 완전 일치 검색 |
| GET | `/api/books/category?category=...` | 카테고리 완전 일치 검색 |
| GET | `/api/books/status?status=AVAILABLE` | 대여 가능 도서 |
| POST | `/api/rentals` | 대여. 본문: `{"bookId": 1}` |
| GET | `/api/rentals/me` | 내 대여 목록 |
| DELETE | `/api/rentals/{rentalId}` | 본인 대여 반납 |
| POST | `/api/rentals/{rentalId}/extend` | 본인 대여 반납 예정일 연장 |

비밀번호 변경 본문은 `currentPassword`, `newPassword`입니다. 생성은 201과 생성 ID, 수정·삭제·반납·연장은 204, 조회·로그인은 200을 반환합니다.

## 설계와 업무 규칙

- `member`, `book`, `rental`마다 `domain / application / presentation`으로 책임을 나눴습니다. `auth`는 인증, `global`은 공통 설정·예외 처리를 담당합니다.
- Member는 대여 자격과 현재 대출 권수를, Book은 대여 상태를, Rental은 대여일과 반납 예정일을 관리합니다. 공개 setter 대신 행동 메서드로 상태를 변경합니다.
- Rental에서 Member와 Book을 각각 단방향 `@ManyToOne(fetch = LAZY)`로 참조합니다. FK는 `member_id`, `book_id`이며 양방향 컬렉션과 cascade는 두지 않았습니다. 연관관계 자체가 하나의 애그리거트를 의미하지는 않습니다.
- 대여·반납은 RentalService의 단일 트랜잭션에서 처리합니다. 조회한 회원·도서의 변경은 JPA 변경 감지로 반영합니다.
- 대여 불가 회원과 이미 대여 중인 책은 대여를 거절합니다. 회원의 현재 대출 권수는 음수가 될 수 없습니다.
- 대여일로부터 7일 뒤를 반납 예정일로 설정합니다. 연장은 기존 예정일에 7일을 추가하며 횟수·연체 제한은 적용하지 않았습니다.
- 원본 방식에 맞춰 반납 시 Rental을 삭제합니다. 실제 반납일과 반납 이력은 보존하지 않습니다. 책은 AVAILABLE로 복구하고 회원 대출 권수를 줄입니다.
- 도서·회원도 실제 삭제합니다. 참조 중인 대여 기록이 있으면 DB 외래키 제약으로 삭제가 거절될 수 있습니다. 전용 오류 응답·삭제 정책은 후속 과제입니다.
- 반납·연장은 로그인 회원과 대여자를 비교합니다. 도서 관리의 관리자 제한과 회원 번호 조회의 본인·관리자 제한은 아직 적용하지 않았습니다.
- 학번·연락처는 제외했고, PK와 별도인 책 고유번호는 추후 추가합니다.

## 확인한 수동 테스트

아래는 개발 중 Postman·H2로 확인한 결과이며, 자동화 테스트나 동시 요청 검증 결과는 아닙니다.

- 회원가입 201, 필수값·길이 검증 400, 아이디 중복 409, 비밀번호 해시·기본값 저장.
- 로그인·JWT 보호 API 접근, 토큰 검증, 잘못된 로그인 비밀번호 401.
- 회원 조회·비밀번호 변경·탈퇴, 도서 CRUD·검색·대여 가능 목록.
- 대여 시 기록·책 상태·회원 대출 권수 변경, 내 대여 목록.
- 반납 후 기록 삭제·상태 복구·권수 감소, 같은 책 재대여.
- 연장 시 반납 예정일 +7일, 타인의 반납·연장 403.
- 순차 중복 대여 409 및 추가 기록·권수 증가 없음, 없는 회원·책·대여 404.
- ID 참조를 JPA 연관관계로 변경한 후 대여·목록·연장·반납 재확인.

## 후속 과제

- Refresh Token·Redis·로그아웃 구현 및 토큰 폐기 정책.
- 자동화 테스트, 동시 대여·회원 대출 권수 갱신의 동시성 제어.
- 역할별 권한, 대여 중 도서 삭제·미반납 회원탈퇴 정책과 오류 응답.
- 반납 이력 보존·논리 삭제, 책 고유번호.
- 도서 정보를 함께 반환하는 조회 전략과 N+1 검증, 검색·오류 응답 고도화.

