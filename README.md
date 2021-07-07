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

# config 파일 관련 주의사항

- spring project에서는 먼저 config 폴더 내의 application.properties를 읽게 됩니다.
- dev 환경에서 사용할 목적으로 config 폴더 내에 application.properties를 로컬 서버 환경에 맞게 작성하였습니다.
- deploy시에는 application.properties.deploy 파일을 활용해주시면 됩니다.