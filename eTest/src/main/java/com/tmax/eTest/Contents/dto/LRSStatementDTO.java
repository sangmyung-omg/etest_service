package com.tmax.eTest.Contents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LRSStatementDTO {
  private String userId;
  private String actionType;
  private String sourceType;
  private String sourceId;
  private String timestamp;
  private String platform;
  private Integer cursorTime;
  private String userAnswer;
  private String duration;
  private String extension;
  private Integer isCorrect;
}
