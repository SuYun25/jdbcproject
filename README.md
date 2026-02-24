# 📈 Stock Trading Console Project

## 📝 프로젝트 개요
Java와 Oracle DB를 활용하여 구현한 콘솔 기반 주식 매매 시스템입니다.  
주식 거래 흐름과 데이터베이스 연동 구조를 이해하고,  
계층형 아키텍처(Controller-Service-DAO)를 직접 설계해보기 위해 제작한 학습용 프로젝트입니다.

실제 주식 거래 시스템의 기본 구조를 단순화하여 구현하였으며,  
회원 관리와 매수/매도 로직을 중심으로 데이터 흐름을 학습하는 데 목적이 있습니다.

---

## ⏳ 개발 기간
**2025.11.20 ~ 2025.11.23**

---

## 🛠 기술 스택

- Java
- JDBC
- Oracle Database (XE)
- Eclipse IDE
- Lombok
- Git / GitHub

---

## 📂 프로젝트 구조
```
src
├── stock.controller → 사용자 입력 및 메뉴 제어
├── stock.service    → 비즈니스 로직 처리
├── stock.dao        → DB 접근 로직
├── stock.dto        → 데이터 전달 객체
└── stock.util       → DB 연결 유틸 (DBUtil)
```

---

## 💻 코딩 구성

### 🔹 Controller
- 콘솔 메뉴 출력
- 회원가입 / 로그인 처리
- 사용자 입력 제어

### 🔹 Service
- 비즈니스 로직 분리
- DAO 호출 관리

### 🔹 DAO
- JDBC를 이용한 SQL 실행
- CRUD 처리

### 🔹 DTO
- 데이터 전달 객체
- 계층 간 데이터 이동 역할

### 🔹 DBUtil
- Oracle DB 연결 관리
- JDBC Driver 로딩 및 Connection 반환

---

## 🎯 주요 기능

- 회원가입
- 로그인
- 주식 조회
- 주식 매수/매도
- 데이터베이스 연동

---

## 📚 학습 내용

### 1️⃣ 계층형 아키텍처 설계
- Controller / Service / DAO 계층 분리
- 역할 분리의 중요성 이해

### 2️⃣ JDBC 직접 구현
- Driver 로딩
- Connection 관리
- PreparedStatement 사용
- ResultSet 처리

### 3️⃣ DB 연동 구조 이해
- Oracle DB 연결
- SQL 작성 및 실행
- 트랜잭션 흐름 이해

### 4️⃣ Git 관리 경험
- IDE 설정 파일 분리 (.gitignore)
- 민감 정보 제거
- 브랜치 전략 이해

---

## 🔐 보안 관련

DB 연결 정보는 보안을 위해 레포지토리에 포함하지 않습니다.  
`db.properties` 파일을 로컬에서 직접 생성해야 합니다.

예시:

```
db.url=jdbc:oracle:thin:@localhost:1521:XE
db.user=사용자명
db.pass=비밀번호
```

## 🚀 실행 방법

1. Oracle DB 실행
2. 필요한 테이블 생성
3. `db.properties` 생성
4. Eclipse에서 프로젝트 실행

---

## 🧠 향후 개선 계획

- 예외 처리 고도화
- 트랜잭션 관리 개선
- MVC 패턴 리팩토링
- 웹 기반 프로젝트로 확장 예정 (Spring 기반)
---

## 📌 프로젝트 성격

본 프로젝트는 백엔드 로직 및 DB 흐름 이해를 위한 학습용 프로젝트입니다.  
이후 동일한 로직을 기반으로 Web 환경으로 확장하여 별도 프로젝트로 개발하였습니다.
