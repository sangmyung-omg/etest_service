package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ListDTO {

  private Video video;
  private Book book;
  private Wiki wiki;
  private Article article;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Video {
    private Boolean recommended;
    private String recommendDate;
    private Integer riskScore;
    private Integer investScore;
    private Integer knowledgeScore;
    private String watchDate;
    private int size;
    private List<VideoDTO> videos;

    public Video(int size, List<VideoDTO> videos) {
      this.size = size;
      this.videos = videos;
    }

    public Video(int size, List<VideoDTO> videos, Boolean recommended) {
      this.size = size;
      this.videos = videos;
      this.recommended = recommended;
    }

    public Video(Boolean recommended) {
      this.recommended = recommended;
    }
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
