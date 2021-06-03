# eTest-SAP(Student Analysis Platform)-Backend
- KoFIA Prototype : "eTest"
- Feature Coverage : Diagnosis / Mini-Test / Analysis-Report


## Environments
- language : java
- framework : Spring boot
- DB : Tibero
- data access : jpa


## Versions
- java : 1.8.0_241
- Spring : 2.4.5
- Gradle : 6.8.3
- jdbc : tibero6-jdbc.zip


## Configuration
- [Spring Initilaizer](https://start.spring.io/) 를 이용한 초기 프로젝트 생성 옵션


## Dependencies
- Spring Boot DevTools
- Spring Web
- Lombok
- Spring Data JPA
- Oracle Driver


# DB 생성 및 initialize
- src/main/resources 폴더 내로 sql 파일 위치
  + schema.sql = DB 테이블 생성
  + data.sql = DB 데이터 초기화


- src/main/resources/application.properties 파일의 'spring.datasource.initialization-mode' 를 never -> always 로 변경 및 저장
- Run (or 이미 Run 상태이면 자동으로 리부트 됨)
