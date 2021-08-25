package com.tmax.eTest.LRS.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.JWTUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name="STATEMENT")
@NoArgsConstructor
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

    public Statement(String userId, String actionType, String sourceType, String timestamp)
    {
        super();
        this.statementId = UUID.randomUUID().toString();
        this.userId = userId;
        this.actionType = actionType;
        this.sourceType = sourceType;
        this.timestamp = timestamp;
    }

    public Statement(StatementDTO dto)
    {
        super();
        this.statementId = UUID.randomUUID().toString();
        this.actionType = dto.getActionType();
        this.sourceType = dto.getSourceType();
        this.timestamp = dto.getTimestamp();
        
        String jwtRes = null;
        try {
        	jwtRes = JWTUtil.getJWTPayloadField(dto.getUserId(), "userID");
        }
        catch(Exception e)
        {
        	System.out.println("StatementDAO - userID is not jwt ID");
        }
        
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
