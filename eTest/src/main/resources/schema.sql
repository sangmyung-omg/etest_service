CREATE TABLE ETEST.CURRICULUM_MASTER (
	CURRICULUM_ID VARCHAR(255 CHAR) NOT NULL,
	CHAPTER VARCHAR(255 CHAR),
	CHAPTER_ID VARCHAR(255 CHAR),
	CURRICULUM_SEQUENCE NUMBER(10),
	GRADE VARCHAR(255 CHAR),
	SCHOOL_TYPE VARCHAR(255 CHAR),
	SECTION VARCHAR(255 CHAR),
	SEMESTER VARCHAR(255 CHAR),
	SUB_SECTION VARCHAR(255 CHAR)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.DIAGNOSIS_CURRICULUM (
	CURRICULUM_ID NUMBER NOT NULL,
	CHAPTER VARCHAR(100) NOT NULL,
	SECTION VARCHAR(100),
	SUB_SECTION VARCHAR(100)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.DIAGNOSIS_PROBLEM (
	SUBJECT VARCHAR(100),
	PROB_ID NUMBER NOT NULL,
	CURRICULUM_ID NUMBER NOT NULL,
	SET_TYPE CHAR(1),
	ORDER_NUM NUMBER
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE ETEST.DIAGNOSIS_PROBLEM IS '문제와 이미지 파일 경로의 상관 관계를 저장';
COMMENT ON COLUMN DIAGNOSIS_PROBLEM.SUBJECT IS '진단 지식 문항 주제';
COMMENT ON COLUMN DIAGNOSIS_PROBLEM.PROB_ID IS '문제 ID';
COMMENT ON COLUMN DIAGNOSIS_PROBLEM.CURRICULUM_ID IS '분류 체계 ID (임시로 커리큘럼에)';
COMMENT ON COLUMN DIAGNOSIS_PROBLEM.SET_TYPE IS '타입 (A, B, C)';
COMMENT ON COLUMN DIAGNOSIS_PROBLEM.ORDER_NUM IS '문제 순서';

CREATE TABLE ETEST.DIAGNOSIS_REPORT (
	USER_UUID VARCHAR(36) NOT NULL,
	GI_SCORE NUMBER,
	RISK_SCORE NUMBER,
	INVEST_SCORE NUMBER,
	KNOWLEDGE_SCORE NUMBER,
	AVG_UK_MASTERY NUMBER,
	USER_MBTI VARCHAR(5),
	INVEST_ITEM_NUM NUMBER,
	STOCK_RATIO NUMBER,
	DIAGNOSIS_DATE TIMESTAMP(6) NOT NULL
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.ERROR_REPORT (
	ERROR_ID NUMBER NOT NULL,
	PROB_ID NUMBER NOT NULL,
	USER_UUID VARCHAR(36) NOT NULL,
	REPORT_TYPE VARCHAR(32) NOT NULL,
	REPORT_TEXT VARCHAR(1024)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.MINITEST_REPORT (
	USER_UUID VARCHAR(36) NOT NULL,
	AVG_UK_MASTERY NUMBER NOT NULL,
	SET_NUM NUMBER NOT NULL,
	CORRECT_NUM NUMBER,
	WRONG_NUM NUMBER,
	DUNNO_NUM NUMBER,
	MINITEST_DATE TIMESTAMP(6) NOT NULL
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.PROBLEM (
	PROB_ID NUMBER NOT NULL,
	ANSWER_TYPE VARCHAR(50) NOT NULL,
	QUESTION VARCHAR(40000) NOT NULL,
	SOLUTION VARCHAR(40000) NOT NULL,
	DIFFICULTY VARCHAR(20),
	CATEGORY VARCHAR(20),
	PART VARCHAR(100),
	IMG_SRC VARCHAR(256),
	TIME_RECOMMENDATION NUMBER,
	CREATOR_ID VARCHAR(32),
	CREATE_DATE TIMESTAMP(6),
	VALIDATOR_ID VARCHAR(32),
	VALIDATE_DATE TIMESTAMP(6),
	EDITOR_ID VARCHAR(32),
	EDIT_DATE TIMESTAMP(6),
	SOURCE VARCHAR(512),
	INTENTION VARCHAR(100)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE ETEST.PROBLEM IS '문제 내용을 저장';
COMMENT ON COLUMN PROBLEM.PROB_ID IS '문제 ID';
COMMENT ON COLUMN PROBLEM.ANSWER_TYPE IS '답안 유형';
COMMENT ON COLUMN PROBLEM.QUESTION IS '질문';
COMMENT ON COLUMN PROBLEM.SOLUTION IS '해설';
COMMENT ON COLUMN PROBLEM.DIFFICULTY IS '문제 난이도';
COMMENT ON COLUMN PROBLEM.CATEGORY IS '분류';
COMMENT ON COLUMN PROBLEM.PART IS '파트';
COMMENT ON COLUMN PROBLEM.IMG_SRC IS '이미지 경로';
COMMENT ON COLUMN PROBLEM.TIME_RECOMMENDATION IS '적정 풀이 시간';
COMMENT ON COLUMN PROBLEM.CREATOR_ID IS '생성자';
COMMENT ON COLUMN PROBLEM.CREATE_DATE IS '생성 날짜';
COMMENT ON COLUMN PROBLEM.VALIDATOR_ID IS '검수자';
COMMENT ON COLUMN PROBLEM.VALIDATE_DATE IS '검수 날짜';
COMMENT ON COLUMN PROBLEM.EDITOR_ID IS '수정자';
COMMENT ON COLUMN PROBLEM.EDIT_DATE IS '수정 날짜';
COMMENT ON COLUMN PROBLEM.SOURCE IS '문제 출처';
COMMENT ON COLUMN PROBLEM.INTENTION IS '출제 의도';

CREATE TABLE ETEST.PROBLEM_CHOICE (
	PROB_ID NUMBER NOT NULL,
	CHOICE_NUM NUMBER NOT NULL,
	TEXT VARCHAR(512) NOT NULL,
	UK_ID NUMBER,
	CHOICE_SCORE NUMBER
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE ETEST.PROBLEM_CHOICE IS '문제와 이미지 파일 경로의 상관 관계를 저장';
COMMENT ON COLUMN PROBLEM_CHOICE.PROB_ID IS '문제 ID';
COMMENT ON COLUMN PROBLEM_CHOICE.CHOICE_NUM IS '문제 보기 번호';
COMMENT ON COLUMN PROBLEM_CHOICE.TEXT IS '문제 보기 텍스트';
COMMENT ON COLUMN PROBLEM_CHOICE.UK_ID IS 'UK_ID';

CREATE TABLE ETEST.PROBLEM_UK_REL (
	PROB_ID NUMBER NOT NULL,
	UK_ID NUMBER NOT NULL
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE ETEST.PROBLEM_UK_REL IS '문제와 지식 추적 UK의 상관 관계를 저장';
COMMENT ON COLUMN PROBLEM_UK_REL.PROB_ID IS '문제 ID';
COMMENT ON COLUMN PROBLEM_UK_REL.UK_ID IS 'UK_ID';

CREATE TABLE ETEST.TEST_PROBLEM (
	SET_NUM NUMBER NOT NULL,
	SEQUENCE NUMBER NOT NULL,
	PROB_ID NUMBER NOT NULL,
	SUBJECT NUMBER(10)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE ETEST.TEST_PROBLEM IS '문제와 이미지 파일 경로의 상관 관계를 저장';
COMMENT ON COLUMN TEST_PROBLEM.SET_NUM IS '모의테스트 세트 번호';
COMMENT ON COLUMN TEST_PROBLEM.SEQUENCE IS '모의 테스트 세트 내 순서';
COMMENT ON COLUMN TEST_PROBLEM.PROB_ID IS '문제 ID';

CREATE TABLE ETEST.UK_MASTER (
	UK_ID NUMBER NOT NULL,
	UK_NAME VARCHAR(200) NOT NULL,
	UK_DESCRIPTION VARCHAR(1000),
	TRAIN_UNSEEN CHAR(1) NOT NULL,
	CURRICULUM_ID VARCHAR(255 CHAR),
	PART VARCHAR(50)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.UK_REL (
	BASE_UK_ID NUMBER NOT NULL,
	PRE_UK_ID NUMBER NOT NULL,
	RELATION_REFERENCE VARCHAR(32)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.USER_EMBEDDING (
	USER_UUID VARCHAR(255 CHAR) NOT NULL,
	UPDATE_DATE TIMESTAMP(6),
	USER_EMBEDDING VARCHAR(255 CHAR)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.USER_KNOWLEDGE (
	USER_UUID VARCHAR(36) NOT NULL,
	UK_MASTERY NUMBER,
	UPDATE_DATE TIMESTAMP(6),
	UK_ID VARCHAR(255 CHAR) NOT NULL
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE TABLE ETEST.USER_MASTER (
	USER_UUID VARCHAR(36) NOT NULL,
	NAME VARCHAR(100),
	ROLE VARCHAR(100),
    PASSWORD VARCHAR(1000),
    EMAIL VARCHAR(100) NOT NULL,
    PROVIDERID VARCHAR(100),
    EMAILVERIFIED VARCHAR(100),
    PROVIDER VARCHAR(100),
    USERTYPE VARCHAR(100)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;

CREATE UNIQUE INDEX ETEST.ETEST_CON1282100128 ON ETEST.CURRICULUM_MASTER (
	CURRICULUM_ID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.ETEST_CON1287100143 ON ETEST.DIAGNOSIS_CURRICULUM (
	CURRICULUM_ID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.PK_DIAGNOSIS_PROBLEM ON ETEST.DIAGNOSIS_PROBLEM (
	PROB_ID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.PK_DIAGNOSIS_REPORT ON ETEST.DIAGNOSIS_REPORT (
	USER_UUID ASC,
	DIAGNOSIS_DATE ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.PK_ERROR_REPORT ON ETEST.ERROR_REPORT (
	ERROR_ID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.ETEST_CON1286500084 ON ETEST.MINITEST_REPORT (
	USER_UUID ASC,
	MINITEST_DATE ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.PK_PROBLEM ON ETEST.PROBLEM (
	PROB_ID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.PK_UK_MASTER ON ETEST.UK_MASTER (
	UK_ID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.ETEST_CON1290000468 ON ETEST.USER_EMBEDDING (
	USER_UUID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.PK_USER_KNOWLEDGE ON ETEST.USER_KNOWLEDGE (
	USER_UUID ASC,
	UK_ID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

CREATE UNIQUE INDEX ETEST.PK_USER_MASTER ON ETEST.USER_MASTER (
	USER_UUID ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

ALTER TABLE CURRICULUM_MASTER ADD CONSTRAINT ETEST_CON1282100128
PRIMARY KEY (
	CURRICULUM_ID
);

ALTER TABLE DIAGNOSIS_CURRICULUM ADD CONSTRAINT ETEST_CON1287100143
PRIMARY KEY (
	CURRICULUM_ID
);

ALTER TABLE DIAGNOSIS_PROBLEM ADD CONSTRAINT PK_DIAGNOSIS_PROBLEM
PRIMARY KEY (
	PROB_ID
);

ALTER TABLE DIAGNOSIS_REPORT ADD CONSTRAINT PK_DIAGNOSIS_REPORT
PRIMARY KEY (
	USER_UUID,
	DIAGNOSIS_DATE
);

ALTER TABLE ERROR_REPORT ADD CONSTRAINT PK_ERROR_REPORT
PRIMARY KEY (
	ERROR_ID
);

ALTER TABLE MINITEST_REPORT ADD CONSTRAINT ETEST_CON1286500084
PRIMARY KEY (
	USER_UUID,
	MINITEST_DATE
);

ALTER TABLE PROBLEM ADD CONSTRAINT PK_PROBLEM
PRIMARY KEY (
	PROB_ID
);

ALTER TABLE UK_MASTER ADD CONSTRAINT PK_UK_MASTER
PRIMARY KEY (
	UK_ID
);

ALTER TABLE USER_EMBEDDING ADD CONSTRAINT ETEST_CON1290000468
PRIMARY KEY (
	USER_UUID
);

ALTER TABLE USER_KNOWLEDGE ADD CONSTRAINT PK_USER_KNOWLEDGE
PRIMARY KEY (
	USER_UUID,
	UK_ID
);

ALTER TABLE USER_MASTER ADD CONSTRAINT PK_USER_MASTER
PRIMARY KEY (
	USER_UUID
);

CREATE SEQUENCE ERROR_SEQ INCREMENT BY 1 START WITH 1;