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

  @Data
  @AllArgsConstructor
  public static class Book {
    private int size;
    private List<BookDTO> books;
  }

  @Data
  @AllArgsConstructor
  public static class Wiki {
    private int size;
    private List<WikiDTO> wikis;
  }

  @Data
  @AllArgsConstructor
  public static class Article {
    private int size;
    private List<ArticleDTO> articles;
  }

  @Data
  @AllArgsConstructor
  public static class Stat {
    private int size;
    private List<StatDTO> stats;
  }
}
