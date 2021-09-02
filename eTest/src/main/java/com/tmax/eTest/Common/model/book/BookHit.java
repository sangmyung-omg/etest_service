package com.tmax.eTest.Common.model.book;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BookHit {
  @Id
  private Long bookId;
  private Integer hit;
}
