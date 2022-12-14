package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VideoDTO {

  private String videoId;
  private String videoSrc;
  private String title;
  private String imgSrc;
  private String subject;
  private String createDate;
  private String registerDate;
  private Float totalTime;
  private Float startTime;
  private Float endTime;
  private Integer hit;
  private Boolean bookmark;
  private String videoType;
  private String source;
  private String description;
  private List<String> uks;
  private List<String> hashtags;

}
