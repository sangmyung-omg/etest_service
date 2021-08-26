package com.tmax.eTest.LRS.dto;

import com.tmax.eTest.LRS.model.Statement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatementDTO {
	@ApiModelProperty(
		name = "userId",
		dataType = "String",
		example = "test_user_id",
		value = "유저의 ID(JWT or 일반 String 값)",
		required = true)
    private String userId;
	
	@ApiModelProperty(
			name = "actionType",
			dataType = "String",
			example = "submit",
			value = "해당 활동의 동사.",
			required = true)
    private String actionType;
	
	@ApiModelProperty(
			name = "sourceType",
			dataType = "String",
			example = "question",
			value = "해당 활동의 목적어(Source) 타입.",
			required = true)
    private String sourceType;
	
	@ApiModelProperty(
			name = "sourceId",
			dataType = "String",
			example = "100",
			value = "해당 활동의 Source의 ID. ( ex) 유저가 푼 문제의 ID, 100)")
    private String sourceId;
	
	@ApiModelProperty(
			name = "timestamp",
			dataType = "String",
			example = "2021-05-27T15:35:52+09:00",
			value = "해당 활동이 일어난 시간. ISO 8601 Format.",
			required = true)
    private String timestamp;
	
	@ApiModelProperty(
			name = "platform",
			dataType = "String",
			example = "WAPL Math",
			value = "해당 활동이 일어난 Platform 이름")
    private String platform;
	
	@ApiModelProperty(
			name = "cursorTime",
			dataType = "String",
			example = "10000",
			value = "비디오, 오디오와 같은 활동에서 사용되는 커서 시간. (Ednet 참고. ex) A 비디오 10초(10000ms) 지점을 클릭 했다.)")
    private Integer cursorTime;
	
	@ApiModelProperty(
			name = "userAnswer",
			dataType = "String",
			example = "3,5",
			value = "유저가 해당 활동에서 작성한 답안. ( ex) 문제 A에 3, 5번을 선택하여 제출했다.)")
    private String userAnswer;
	
	@ApiModelProperty(
			name = "duration",
			dataType = "String",
			example = "30000",
			value = "활동의 지속 시간( ex) 비디오 보기를 30초(30000ms) 동안 지속했다.)")
    private String duration;
	
	@ApiModelProperty(
			name = "extension",
			dataType = "String",
			example = "'{tempValue : 1, tempValue2 : 2}'",
			value = "추가 메타 데이터 (Json 형태의 String)")
    private String extension;
	
	@ApiModelProperty(
			name = "isCorrect",
			dataType = "Integer",
			example = "0",
			value = "활동의 정답 여부. (0 : Fail, 1 : Success)")
    private Integer isCorrect;

	
	 public StatementDTO(Statement dao)
    {
        this.userId = dao.getUserId();
        this.actionType = dao.getActionType();
        this.sourceType = dao.getSourceType();
        this.sourceId = dao.getSourceId();
        this.timestamp = dao.getTimestamp();
        this.platform = dao.getPlatform();
        this.cursorTime = dao.getCursorTime();
        this.userAnswer = dao.getUserAnswer();
        this.duration = dao.getDuration();
        this.extension = dao.getExtension();
        this.isCorrect = dao.getIsCorrect();
    }
}
