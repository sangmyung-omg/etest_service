package com.tmax.eTest.Common.model.book;

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
public class Book {
  @Id
  private Long bookId;
  private String bookSrc;
  private String title;
  private Timestamp createDate;
  private String creatorId;
  private String imgSrc;
  private String description;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<BookBookmark> bookBookmarks = new LinkedHashSet<BookBookmark>();

}
