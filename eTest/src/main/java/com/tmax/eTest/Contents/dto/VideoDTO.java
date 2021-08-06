package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoDTO {

  private Long videoId;
  private String videoSrc;
  private String title;
  private String createDate;
  private String creatorId;
  private String imgSrc;
  private String subject;
  private Float totalTime;
  private Integer hit;
  private boolean bookmark;
  private List<String> uks;
  private List<String> hashtags;

}
