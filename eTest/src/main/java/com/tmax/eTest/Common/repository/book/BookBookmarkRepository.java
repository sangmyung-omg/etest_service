package com.tmax.eTest.Common.repository.book;

import com.tmax.eTest.Common.model.book.BookBookmark;
import com.tmax.eTest.Common.model.book.BookBookmarkId;

import com.tmax.eTest.Common.model.video.VideoBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBookmarkRepository extends JpaRepository<BookBookmark, BookBookmarkId> {
  long countByUserUuid(String userUuid);
  @Query(value = "SELECT * FROM BOOK_BOOKMARK WHERE USER_UUID = :USER_UUID", nativeQuery = true)
  List<BookBookmark> findAllByUserUuid(@Param("USER_UUID") String UserUuid);
}
