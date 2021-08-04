package com.tmax.eTest.TestStudio.dto;

import java.util.Date;

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
}