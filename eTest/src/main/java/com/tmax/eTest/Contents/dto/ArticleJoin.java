package com.tmax.eTest.Contents.dto;

import com.tmax.eTest.Common.model.article.Article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleJoin {
  private Article article;
  private String userUuid;
}
