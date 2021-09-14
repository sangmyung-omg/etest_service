package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.article.QArticle.article;
import static com.tmax.eTest.Common.model.article.QArticleBookmark.articleBookmark;
import static com.tmax.eTest.Common.model.article.QArticleUkRel.articleUkRel;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.article.Article;
import com.tmax.eTest.Contents.dto.ArticleJoin;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  public ArticleRepositorySupport(JPAQueryFactory query) {
    super(Article.class);
    this.query = query;
  }

  public ArticleJoin findArticleByUserAndId(String userId, Long articleId) {
    return tupleToJoin(query.select(article, articleBookmark.userUuid).from(article)
        .leftJoin(article.articleBookmarks, articleBookmark).on(userEq(userId)).where(idEq(articleId)).fetchOne());
  }

  public List<ArticleJoin> findArticlesByUser(String userId, String keyword) {
    return tupleToJoin(query.select(article, articleBookmark.userUuid).from(article)
        .leftJoin(article.articleBookmarks, articleBookmark).on(userEq(userId)).where(checkKeyword(keyword)).orderBy()
        .fetch());
  }

  public List<ArticleJoin> findBookmarkArticlesByUser(String userId, String keyword) {
    return tupleToJoin(
        query.select(article, articleBookmark.userUuid).from(article).join(article.articleBookmarks, articleBookmark)
            .where(userEq(userId)).where(checkKeyword(keyword)).orderBy().fetch());
  }

  private BooleanExpression userEq(String userId) {
    return commonUtils.stringNullCheck(userId) ? null : articleBookmark.userUuid.eq(userId);
  }

  private BooleanExpression idEq(Long articleId) {
    return commonUtils.objectNullcheck(articleId) ? null : article.articleId.eq(articleId);
  }

  private BooleanExpression checkKeyword(String keyword) {
    return commonUtils.stringNullCheck(keyword) ? null
        : article.title.contains(keyword).or(article.articleUks.any().in(JPAExpressions.selectFrom(articleUkRel)
            .where(articleUkRel.article.eq(article), articleUkRel.ukMaster.ukName.contains(keyword))));
  }

  private ArticleJoin tupleToJoin(Tuple tuple) {
    return new ArticleJoin(tuple.get(article), tuple.get(articleBookmark.userUuid));
  }

  private List<ArticleJoin> tupleToJoin(List<Tuple> tuples) {
    return tuples.stream().map(tuple -> new ArticleJoin(tuple.get(article), tuple.get(articleBookmark.userUuid)))
        .collect(Collectors.toList());
  }
}
