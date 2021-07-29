package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ListDTO {

  @Data
  @AllArgsConstructor
  public static class Video {
    private int size;
    private List<Long> videos;
  }

  @Data
  @AllArgsConstructor
  public static class Book {
    private int size;
    private List<Long> books;
  }

  @Data
  @AllArgsConstructor
  public static class Wiki {
    private int size;
    private List<Long> wikis;
  }

  @Data
  @AllArgsConstructor
  public static class Article {
    private int size;
    private List<Long> articles;
  }
}
