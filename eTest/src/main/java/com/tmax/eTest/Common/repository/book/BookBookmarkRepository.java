package com.tmax.eTest.Common.repository.book;

import com.tmax.eTest.Common.model.book.BookBookmark;
import com.tmax.eTest.Common.model.book.BookBookmarkId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBookmarkRepository extends JpaRepository<BookBookmark, BookBookmarkId> {

}
