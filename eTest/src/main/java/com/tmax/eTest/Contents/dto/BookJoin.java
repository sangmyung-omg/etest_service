package com.tmax.eTest.Contents.dto;

import com.tmax.eTest.Common.model.book.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookJoin {
  private Book book;
  private String userUuid;  
}
