package com.tmax.eTest.Common.model.article;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.data.domain.Persistable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ArticleBookmarkId.class)
public class ArticleBookmark implements Persistable<ArticleBookmarkId> {
  @Id
  private String userUuid;
  @Id
  private Long articleId;

  public ArticleBookmark(String userUuid, Long articleId) {
    this.userUuid = userUuid;
    this.articleId = articleId;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "articleId", insertable = false, updatable = false, nullable = true)
  private Article article;

  @Transient
  private boolean isNew = true;

  @Transient
  private ArticleBookmarkId articleBookmarkId;

  @Transient
  private CommonUtils commonUtils = new CommonUtils();

  @Override
  public ArticleBookmarkId getId() {
    return commonUtils.objectNullcheck(articleBookmarkId)
        ? articleBookmarkId = new ArticleBookmarkId(this.userUuid, this.articleId)
        : articleBookmarkId;
  }

  @Override
  public boolean isNew() {
    return this.isNew;
  }

  @PrePersist
  @PostLoad
  public void markNotNew() {
    this.isNew = false;
  }
}
