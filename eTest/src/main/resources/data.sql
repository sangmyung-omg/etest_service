INSERT INTO USER_MASTER(USER_UUID) VALUES('123');
---------------------UK MASTER------------------------
insert into "ETEST"."UK_MASTER" ("UK_UUID", "TRAIN_UNSEEN", "UK_DESCRIPTION", "UK_NAME" ) values ('1', 0, '1번 uk에 대한 설명', '1번 uk');
insert into "ETEST"."UK_MASTER" ("UK_UUID", "TRAIN_UNSEEN", "UK_DESCRIPTION", "UK_NAME" ) values ('2', 1, '2번 uk에 대한 설명', '2번 uk');
insert into "ETEST"."UK_MASTER" ("UK_UUID", "TRAIN_UNSEEN", "UK_DESCRIPTION", "UK_NAME" ) values ('3', 0, '3번 uk에 대한 설명', '3번 uk');

---------------------PROBLEM------------------------
insert into "ETEST"."PROBLEM" ("PROB_ID", "ANSWER_TYPE", "CATEGORY", "CREATE_DATE", "CREATOR_ID", "DIFFICULTY", "EDIT_DATE", "EDITOR_ID", "IMG_SRC", "INTENTION", "PART", "QUESTION", "SOLUTION", "SOURCE", "TIME_RECOMMENDATION", "VALIDATE_DATE", "VALIDATOR_ID" ) values (1, 'MULTIPLE_CHOICE_WO_PASSAGE', null, TO_DATE('2021/05/28 10:42:43', 'YYYY/MM/DD HH24:MI:SS'), '김두희111', '중', null, null, null, '그냥', '2', '{"question": "생애 처음으로 투자를 시작하는 개인투자자 A씨가 투자를 위해 한 행동 중에 현행 제도와 가장 거리가 먼 것은?", "passage": null, "preface": null}', '{"answer":[4],"solution": "지문1 우체국에서 주식매매를 위한 위탁계좌를 개설할 수 있다.
 지문2 증권회사는 펀드 판매사로서 펀드 매매를 위한 계좌개설과 펀드 매수를 중개할 수 있다.
지문3 증권회사의 비대면 채널을 통한 주식 매매 거래의 전형이다.
 지문4 은행은 증권회사를 대신해서 주식매매 계좌의 개설을 대행해 줄 수 있다. 그러나 주식매수 주문은 은행에서는 할 수 없고, 해당 계좌의 원 소속인 증권사를 통해서 매매 주문을 할 수 있다. " }', '교재 5장 16~17 페이지', null, null, null);
insert into "ETEST"."PROBLEM" ("PROB_ID", "ANSWER_TYPE", "CATEGORY", "CREATE_DATE", "CREATOR_ID", "DIFFICULTY", "EDIT_DATE", "EDITOR_ID", "IMG_SRC", "INTENTION", "PART", "QUESTION", "SOLUTION", "SOURCE", "TIME_RECOMMENDATION", "VALIDATE_DATE", "VALIDATOR_ID" ) values (2, 'MULTIPLE_CHOICE_WO_PASSAGE', null, TO_DATE('2021/05/28 10:44:43', 'YYYY/MM/DD HH24:MI:SS'), '김두희222', '중', null, null, null, '그냥', '1', '{"question": "투자자 A씨가 온라인에서 본 유사투자자문업자의 행동(광고)중에 현행자본시장법에 비추어 가장 올바른 것은?", "passage": null, "preface": null}', '{"answer":[2],"solution": "지문1 유사투자자문업은 자본시장법 101조(유사투자자문업의 신고)에 따라 금융위원회에 신고한 자는 합법적인 유사투자자문업자다. 관련 규정을 어긴 자만 불법이다.
 지문2 자본시장법 101조(유사투자자문업의 신고)의 규정으로 맞는 말이다.
지문3 1대1 투자자문은 불법이며 불특정 다수를 상대로 조언을 해야 합법이다.
 지문4 유사투자자문업자의 일임계약은 불법이고, 수익률 약정 등도 과장광고로 금지대상이다." }', '교재 5장 6~17 페이지', null, null, null);
insert into "ETEST"."PROBLEM" ("PROB_ID", "ANSWER_TYPE", "CATEGORY", "CREATE_DATE", "CREATOR_ID", "DIFFICULTY", "EDIT_DATE", "EDITOR_ID", "IMG_SRC", "INTENTION", "PART", "QUESTION", "SOLUTION", "SOURCE", "TIME_RECOMMENDATION", "VALIDATE_DATE", "VALIDATOR_ID" ) values (3, 'MULTIPLE_CHOICE_WO_PASSAGE', null, TO_DATE('2021/05/28 10:46:43', 'YYYY/MM/DD HH24:MI:SS'), '김두희333', '중', null, null, null, '재밌어서', '3', '{"question": "공모주와 스팩(SPAC)투자에 대한 설명으로 가장 거리가 먼 것은?", "passage": null, "preface": null}', '{"answer":[2],"solution": "지문1 공모주 청약을 위해서는 금융투자회사를 통해서 청약을 하고, 배정받는 주식을 보유하거나 매매를 통해 이익을 얻을 수 있다. 따라서 증권회사의 계좌는 반드시 있어야 청약이 가능하다.
 지문2 주관사나 공동주관사를 통해서만 해당 공모주의 청약을 할 수 있기 때문에 해당 공모주를 주관(또는 공동주관)하는 증권사의 계좌가 필요하다.
지문3 스팩의 큰 특징중의 하나로 합병이 불발될 때는 원금과 이익금을 돌려받는 구조다.
 지문4 스팩은 합병을 목적으로 하는 명목상의 회사다." }', '교재 5장 19~20 페이지', null, null, null);

------------------PROBLEM CHOICE--------------------
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (1, 1, '우체국을 방문해서 주식매매 계좌를 신규개설 하였다.', '1');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (1, 2, '유사투자자문업자는 모두 불법으로 법적 근거가 없다.', '1');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (1, 3, '공모주 청약은 증권회사 계좌없이 은행에서도 할 수 있다.', '2');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (2, 1, '증권회사에 가서 펀드매매 계좌를 개설하고 펀드를 매수하였다.', '1');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (2, 2, '안내문에는 불특정 다수인을 대상으로 발행되는 간행물, 전자우편 등에 의한 조언이 가능하다고 한다.', '2');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (2, 3, '공모주 청약은 IPO(Initial Public Offering)를 주관하는 주관사나 공동주관사를 통해서만 청약이 가능하다. ', '2');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (3, 1, '증권회사 앱을 다운받아 비대면 계좌를 개설하고 주식을 매수하였다.', '2');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (3, 2, '금융감독원에 신고된 유사투자자문업자는 월 30만원 이하로 1대1 투자자문을 하는 것은 합법적이라고 한다.', '2');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (3, 3, '스팩이 만기 시점까지 합병하지 못하는 경우에는 투자자에게 투자금의 원금과 이자를 반환하는 수익구조를 가진다.', '1');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (4, 1, '은행을 방문해서 개설한 주식매매 계좌에 100만원을 입금하고, 은행직원에게 주식 매수 주문을 하였다.', '1');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (4, 2, '자신에게 돈을 맡기면 매월 5%씩 안정적인 수익률을 보장한다는 광고와 함께 자신의 계좌 수익률 사진을 보내왔다. ', '3');
insert into "ETEST"."PROBLEM_CHOICE" ("CHOICE_NUM", "PROB_ID", "TEXT", "UK_UUID" ) values (4, 3, '스팩은 공모를 통해 M&A자금을 먼저 마련하며 상장한 후 다른회사와 합병하는 것을 유일한 사업목적으로 하는 Paper Company다.', '3');

------------------DIAGNOSIS--------------------
insert into "ETEST"."DIAGNOSIS_PROBLEM" ("PROB_ID", "SET_NUM" ) values (2, 1);
------------------TEST--------------------
insert into "ETEST"."TEST_PROBLEM" ("PROB_ID", "SEQUENCE", "SET_NUM" ) values (1, 2, 1);
insert into "ETEST"."TEST_PROBLEM" ("PROB_ID", "SEQUENCE", "SET_NUM" ) values (3, 1, 1);
