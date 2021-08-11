package com.tmax.eTest.Common.model.article;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ArticleUkRelId.class)
public class ArticleUkRel {
  @Id
  private Long articleId;
  @Id
  private Long ukId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "articleId", insertable = false, updatable = false, nullable = true)
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ukId", insertable = false, updatable = false, nullable = true)
  private UkMaster ukMaster;
}
