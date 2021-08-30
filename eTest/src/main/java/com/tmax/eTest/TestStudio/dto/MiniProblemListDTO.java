package com.tmax.eTest.TestStudio.dto;

import java.util.Date;

import org.springframework.util.ObjectUtils;

import com.tmax.eTest.Common.model.problem.TestProblem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MiniProblemListDTO {
	private Integer id;
	private String part;
	private String difficulty;
	private String subject;
	private String status;
	private String question;
	private Date editDate;
	
	public MiniProblemListDTO(TestProblem t) {
		this.id = t.getProbID();
		this.part = ObjectUtils.isEmpty(t.getPart()) ? null : t.getPart().getPartName();
		this.difficulty = t.getProblem().getDifficulty();
		this.subject = t.getSubject();
		this.status = t.getStatus();
		this.question = t.getProblem().getQuestion();
		this.editDate = t.getProblem().getEditDate();
	}
}