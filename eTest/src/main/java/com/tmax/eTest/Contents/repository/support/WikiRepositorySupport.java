package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.wiki.QWiki.wiki;
import static com.tmax.eTest.Common.model.wiki.QWikiBookmark.wikiBookmark;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.wiki.Wiki;
import com.tmax.eTest.Contents.dto.WikiJoin;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class WikiRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  public WikiRepositorySupport(JPAQueryFactory query) {
    super(Wiki.class);
    this.query = query;
  }

  public WikiJoin findWikiByUserAndId(String userId, Long wikiId) {
    return tupleToJoin(query.select(wiki, wikiBookmark.userUuid).from(wiki).leftJoin(wiki.wikiBookmarks, wikiBookmark)
        .on(userEq(userId)).where(idEq(wikiId)).fetchOne());
  }

  public List<WikiJoin> findWikisByUser(String userId, String keyword) {
    return tupleToJoin(query.select(wiki, wikiBookmark.userUuid).from(wiki).leftJoin(wiki.wikiBookmarks, wikiBookmark)
        .on(userEq(userId)).where(checkKeyword(keyword)).orderBy().fetch());
  }

  public List<WikiJoin> findBookmarkWikisByUser(String userId, String keyword) {
    return tupleToJoin(query.select(wiki, wikiBookmark.userUuid).from(wiki).join(wiki.wikiBookmarks, wikiBookmark)
        .where(userEq(userId)).where(checkKeyword(keyword)).orderBy().fetch());
  }

  private BooleanExpression userEq(String userId) {
    return commonUtils.stringNullCheck(userId) ? null : wikiBookmark.userUuid.eq(userId);
  }

  private BooleanExpression idEq(Long wikiId) {
    return commonUtils.objectNullcheck(wikiId) ? null : wiki.wikiId.eq(wikiId);
  }

  private BooleanExpression checkKeyword(String keyword) {
    return commonUtils.stringNullCheck(keyword) ? null : wiki.title.contains(keyword);
    // .or(wiki.wikiUks.any().in(JPAExpressions.selectFrom(wikiUkRel)
    // .where(wikiUkRel.wiki.eq(wiki),
    // wikiUkRel.ukMaster.ukName.contains(keyword))));
  }

  private WikiJoin tupleToJoin(Tuple tuple) {
    return new WikiJoin(tuple.get(wiki), tuple.get(wikiBookmark.userUuid));
  }

  private List<WikiJoin> tupleToJoin(List<Tuple> tuples) {
    return tuples.stream().map(tuple -> new WikiJoin(tuple.get(wiki), tuple.get(wikiBookmark.userUuid)))
        .collect(Collectors.toList());
  }
}
