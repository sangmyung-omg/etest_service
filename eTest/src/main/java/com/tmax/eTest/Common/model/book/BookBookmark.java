package com.tmax.eTest.Common.model.book;

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
@IdClass(BookBookmarkId.class)
public class BookBookmark implements Persistable<BookBookmarkId> {
  @Id
  private String userUuid;
  @Id
  private String bookId;

  public BookBookmark(String userUuid, String bookId) {
    this.userUuid = userUuid;
    this.bookId = bookId;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bookId", insertable = false, updatable = false, nullable = true)
  private Book book;

  @Transient
  private boolean isNew = true;

  @Transient
  private BookBookmarkId bookBookmarkId;

  @Transient
  private CommonUtils commonUtils = new CommonUtils();

  @Override
  public BookBookmarkId getId() {
    return commonUtils.objectNullcheck(bookBookmarkId) ? bookBookmarkId = new BookBookmarkId(this.userUuid, this.bookId)
        : bookBookmarkId;
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
