package com.tmax.eTest.Common.repository.book;

import com.tmax.eTest.Common.model.book.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
