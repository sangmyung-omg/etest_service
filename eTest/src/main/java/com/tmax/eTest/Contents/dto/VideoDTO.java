package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VideoDTO {

  private Long videoId;
  private String videoSrc;
  private String title;
  private String imgSrc;
  private String subject;
  private String createDate;
  private Float totalTime;
  private Float startTime;
  private Float endTime;
  private Integer hit;
  private Boolean bookmark;
  private String videoType;
  private List<String> uks;
  private List<String> hashtags;

}
