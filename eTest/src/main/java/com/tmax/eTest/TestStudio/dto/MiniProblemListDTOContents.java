package com.tmax.eTest.TestStudio.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class MiniProblemListDTOContents {
	private Integer id;
	private String part;
	private String difficulty;
	private String subject;
	private String status;
	private String question;
	private ZonedDateTime editDate;
	
	@QueryProjection
	public MiniProblemListDTOContents(Integer id, String part, String difficulty, String subject, String status, String question, Date editDate) {
		this.id = id;
		this.part = part;
		this.difficulty = difficulty;
		this.subject = subject;
		this.status = status;
		this.question = question;
		this.editDate = editDate.toInstant().atZone(ZoneId.of("Asia/Seoul"));
	}

}