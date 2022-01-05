package com.tmax.eTest.Report.dto.comment;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentInfoDTO {
	String name;
	String main;
	String detail;
	Integer score;
	List<List<String>> detailScoreList;
}
