package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ListDTO {

  @Data
  @AllArgsConstructor
  public static class Video {
    private int size;
    private List<Integer> videos;
  }

  @Data
  @AllArgsConstructor
  public static class Book {
    private int size;
    private List<Integer> books;
  }

  @Data
  @AllArgsConstructor
  public static class Wiki {
    private int size;
    private List<Integer> wikis;
  }

  @Data
  @AllArgsConstructor
  public static class Article {
    private int size;
    private List<Integer> articles;
  }
}
