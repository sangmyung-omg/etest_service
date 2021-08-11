package com.tmax.eTest.Common.model.article;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Article {
  @Id
  private Long articleId;
  private String articleUrl;
  private String title;
  private Timestamp createDate;
  private String creatorId;
  private String description;
  private String imgSrc;
  private String source;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ArticleUkRel> articleUks = new LinkedHashSet<ArticleUkRel>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ArticleBookmark> articleBookmarks = new LinkedHashSet<ArticleBookmark>();
}
