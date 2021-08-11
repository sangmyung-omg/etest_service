package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.article.QArticle.article;
import static com.tmax.eTest.Common.model.article.QArticleBookmark.articleBookmark;
import static com.tmax.eTest.Common.model.article.QArticleUkRel.articleUkRel;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.article.Article;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  public ArticleRepositorySupport(JPAQueryFactory query) {
    super(Article.class);
    this.query = query;
  }

  public List<Article> findArticlesByUser(String userId, String keyword) {
    return query.selectFrom(article).leftJoin(article.articleBookmarks, articleBookmark).on(userEq(userId))
        .where(checkKeyword(keyword)).orderBy().fetch();
  }

  public List<Article> findBookmarkArticlesByUser(String userId, String keyword) {
    return query.selectFrom(article).join(article.articleBookmarks, articleBookmark).where(userEq(userId))
        .where(checkKeyword(keyword)).orderBy().fetch();
  }

  private BooleanExpression userEq(String userId) {
    return CommonUtils.stringNullCheck(userId) ? null : articleBookmark.userUuid.eq(userId);
  }

  private BooleanExpression checkKeyword(String keyword) {
    return CommonUtils.stringNullCheck(keyword) ? null
        : article.title.contains(keyword).or(article.articleUks.any().in(JPAExpressions.selectFrom(articleUkRel)
            .where(articleUkRel.article.eq(article), articleUkRel.ukMaster.ukName.contains(keyword))));
  }

}
