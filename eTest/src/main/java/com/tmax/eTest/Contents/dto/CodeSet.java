package com.tmax.eTest.Contents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeSet {
  private Integer views;
  private Integer likes;
  private Integer disLikes;
  private String viewDate;
  private Long serialNum;
  private String[] codes;
}
