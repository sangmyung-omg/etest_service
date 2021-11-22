package com.tmax.eTest.Common.model.book;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
  private String bookId;
  private String bookSrc;
  private String title;
  private String imgSrc;
  private String description;
  private Date createDate;
  private String creatorId;
  private String pdf;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "bookId")
  private BookHit bookHit;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<BookBookmark> bookBookmarks = new LinkedHashSet<BookBookmark>();

}
