package com.tmax.eTest.Contents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoDTO {

  private String videoSrc;
  private String title;
  private String createDate;
  private String creatorId;
  private String imgSrc;
  private String subject;
  private Float totalTime;
  private Integer hit;
  private boolean bookmark;

}
