package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ListDTO {

  @Data
  @AllArgsConstructor
  public static class Video {
    private int size;
    private List<VideoDTO> videos;
  }

  @Data
  @AllArgsConstructor
  public static class Curriculum {
    private int size;
    private List<VideoCurriculumDTO> curriculums;
  }
}
