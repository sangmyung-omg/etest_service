package com.tmax.eTest.TestStudio.dto.problems.base;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProblemDTO {
	//Long
	private String probID;
	//
	private String answerType;
	private String question;
	private String solution;
	private String difficulty;
	private String category;
	private String imgSrc;
	private Long timeRecommendation;
	private String creatorID;
	private Timestamp createDate;
	private String validatorID;
	private Timestamp validateDate;
	private String editorID;
	private Timestamp editDate;
	private String source;
	private String intention;
	private String questionInitial;
	private String solutionInitial;
	
	// temp
	private String imgsOut;
	private String darkImgsOut;
	//
	private List<String> imgListIn;
	private List<String> darkImgListIn;
}
