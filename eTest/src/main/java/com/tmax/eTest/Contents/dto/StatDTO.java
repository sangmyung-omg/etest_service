package com.tmax.eTest.Contents.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatDTO {

  private Date date;
  private String userId;
  private Hit hit;
  private BookMark bookMark;

  public StatDTO(Date date, Hit hit) {
    this.date = date;
    this.hit = hit;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Hit {
    private long total;
    private long video;
    private long book;
    private long wiki;
    private long article;

    public Hit(Long video, Long book, Long wiki, Long article) {
      this.video = video;
      this.book = book;
      this.wiki = wiki;
      this.article = article;
      this.sumTotal();
    }

    public void incrementVideo() {
      this.video++;
    }

    public void incrementBook() {
      this.book++;
    }

    public void incrementWiki() {
      this.wiki++;
    }

    public void incrementArticle() {
      this.article++;
    }

    public void sumTotal() {
      this.total = this.video + this.book + this.wiki + this.article;
    }
  }

  @Data
  @AllArgsConstructor
  @Builder
  public static class BookMark {
    private long total;
    private long video;
    private long book;
    private long wiki;
    private long article;

    public BookMark(Long video, Long book, Long wiki, Long article) {
      this.video = video;
      this.book = book;
      this.wiki = wiki;
      this.article = article;
      this.sumTotal();
    }

    public void incrementVideo() {
      this.video++;
    }

    public void incrementBook() {
      this.book++;
    }

    public void incrementWiki() {
      this.wiki++;
    }

    public void incrementArticle() {
      this.article++;
    }

    public void sumTotal() {
      this.total = this.video + this.book + this.wiki + this.article;
    }
  }
}
