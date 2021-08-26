package com.tmax.eTest.Common.repository.article;

import com.tmax.eTest.Common.model.article.ArticleBookmark;
import com.tmax.eTest.Common.model.article.ArticleBookmarkId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark, ArticleBookmarkId> {
  long countByUserUuid(String userUuid);
}
