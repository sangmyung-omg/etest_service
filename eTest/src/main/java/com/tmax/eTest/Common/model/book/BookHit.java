package com.tmax.eTest.Common.model.book;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BookHit {
  @Id
  private String bookId;
  private Integer hit;

  @OneToOne(mappedBy = "bookHit")
  private Book book;
}
