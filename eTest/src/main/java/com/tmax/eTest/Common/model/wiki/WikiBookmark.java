package com.tmax.eTest.Common.model.wiki;

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
@IdClass(WikiBookmarkId.class)
public class WikiBookmark implements Persistable<WikiBookmarkId> {
  @Id
  private String userUuid;
  @Id
  private Long wikiId;

  public WikiBookmark(String userUuid, Long wikiId) {
    this.userUuid = userUuid;
    this.wikiId = wikiId;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wikiId", insertable = false, updatable = false, nullable = true)
  private Wiki wiki;

  @Transient
  private boolean isNew = true;

  @Transient
  private WikiBookmarkId wikiBookmarkId;

  // @Transient
  // private CommonUtils commonUtils;

  @Override
  public WikiBookmarkId getId() {
    return new CommonUtils().objectNullcheck(wikiBookmarkId)
        ? wikiBookmarkId = new WikiBookmarkId(this.userUuid, this.wikiId)
        : wikiBookmarkId;
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
