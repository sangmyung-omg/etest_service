package com.tmax.eTest.LRS.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.JWTUtil;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

@Data
@Entity
@Table(name="STATEMENT", indexes = @Index(columnList = "userId"))
@NoArgsConstructor
@Slf4j
public class Statement {
	
	@Id
	private String statementId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String actionType;

    @Column(nullable = false)
    private String sourceType;

    private String sourceId;

    @Column(nullable = false)
    private String timestamp;

    private String platform;

    private Integer cursorTime;

    private String userAnswer;

    private String duration;
    
    private String extension;

    private Integer isCorrect;
    
    private Integer isDeleted;
    
	private Timestamp statementDate;

	
	
	private Timestamp timeStringToTimestampObj(String timestampStr)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA);
		Timestamp timestampObj = null;
		
		try {
			timestampObj = new Timestamp(dateFormat.parse(timestampStr).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.info("TimeStamp Convert Fail. In Statement "+ timestampStr);
		}
		
		return timestampObj;
	}
	
    public Statement(String userId, String actionType, String sourceType, String timestamp)
    {
        super();
        this.statementId = UUID.randomUUID().toString();
        this.userId = userId;
        this.actionType = actionType;
        this.sourceType = sourceType;
        this.timestamp = timestamp;
        
        this.statementDate = timeStringToTimestampObj(timestamp);
    }

    public Statement(StatementDTO dto)
    {
        super();
        this.statementId = UUID.randomUUID().toString();
        this.actionType = dto.getActionType();
        this.sourceType = dto.getSourceType();
        this.timestamp = dto.getTimestamp();
        
        this.statementDate = timeStringToTimestampObj(this.timestamp);
        
        String jwtRes = JWTUtil.getJWTPayloadField(dto.getUserId(), "userID");
    
        this.userId = (jwtRes==null)?dto.getUserId():jwtRes;
        
        if(dto.getSourceId() != null)
            this.sourceId = dto.getSourceId();
        
        if(dto.getPlatform() != null)
            this.platform = dto.getPlatform();

        if(dto.getCursorTime() != null)
            this.cursorTime = dto.getCursorTime();
        
        if(dto.getUserAnswer() != null)
            this.userAnswer = dto.getUserAnswer();
        
        if(dto.getDuration() != null)
            this.duration = dto.getDuration();
        
        if(dto.getExtension() != null)
            this.extension = dto.getExtension();

        if(dto.getIsCorrect() != null)
            this.isCorrect = dto.getIsCorrect();
        
        this.isDeleted = 0;
    }
}
