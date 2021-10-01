package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.uk.QUkMaster.ukMaster;
import static com.tmax.eTest.Common.model.video.QHashtag.hashtag;
import static com.tmax.eTest.Common.model.video.QVideo.video;
import static com.tmax.eTest.Common.model.video.QVideoBookmark.videoBookmark;
import static com.tmax.eTest.Common.model.video.QVideoHashtag.videoHashtag;
import static com.tmax.eTest.Common.model.video.QVideoUkRel.videoUkRel;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.dto.VideoJoin;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.QuerydslUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class VideoRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private QuerydslUtils querydslUtils;

  public VideoRepositorySupport(JPAQueryFactory query) {
    super(Video.class);
    this.query = query;
  }

  public OrderSpecifier<?> getVideoSortedColumn(SortType sort) {
    switch (sort.name()) {
      case "DATE":
        return querydslUtils.getSortedColumn(Order.ASC, video, "createDate");
      case "HIT":
        return querydslUtils.getSortedColumn(Order.DESC, video.videoHit, "hit");
      case "RECOMMEND":
      case "SEQUENCE":
        return querydslUtils.getSortedColumn(Order.ASC, video, "sequence");
      default:
        throw new ContentsException(ErrorCode.TYPE_ERROR, sort.name() + ": type not provided!");
    }
  }

  public JPAQuery<Video> multipleFetchJoin() {
    return query.select(video).from(video).join(video.videoCurriculum).fetchJoin().join(video.videoHit).fetchJoin()
        .leftJoin(video.videoHashtags, videoHashtag).fetchJoin().leftJoin(videoHashtag.hashtag, hashtag).fetchJoin()
        .leftJoin(video.videoUks, videoUkRel).fetchJoin().leftJoin(videoUkRel.ukMaster, ukMaster).fetchJoin();
  }

  public JPAQuery<Tuple> multipleBookmarkFetchJoin() {
    return query.select(video, videoBookmark.userUuid).from(video).join(video.videoCurriculum).fetchJoin()
        .join(video.videoHit).fetchJoin().leftJoin(video.videoHashtags, videoHashtag).fetchJoin()
        .leftJoin(videoHashtag.hashtag, hashtag).fetchJoin().leftJoin(video.videoUks, videoUkRel).fetchJoin()
        .leftJoin(videoUkRel.ukMaster, ukMaster).fetchJoin().leftJoin(video.videoBookmarks, videoBookmark).fetchJoin();
  }

  public Video findVideoById(String videoId) {
    return multipleFetchJoin().where(idEq(videoId)).distinct().fetchOne();
  }

  public List<Video> findVideosByCurriculum(Long curriculumId, SortType sort, String keyword) {
    return multipleFetchJoin().where(curriculumEq(curriculumId)).where(checkKeyword(keyword))
        .orderBy(getVideoSortedColumn(sort)).distinct().fetch();
  }

  public VideoJoin findVideoByUserAndId(String userId, String videoId) {
    return tupleToJoin(multipleBookmarkFetchJoin().leftJoin(video.videoBookmarks, videoBookmark).on(userEq(userId))
        .where(idEq(videoId)).fetch().stream().distinct().collect(Collectors.toList()).get(0));
  }

  public List<VideoJoin> findVideosByUserAndCurriculum(String userId, Long curriculumId, SortType sort,
      String keyword) {
    return tupleToJoin(multipleBookmarkFetchJoin().leftJoin(video.videoBookmarks, videoBookmark).on(userEq(userId))
        .where(curriculumEq(curriculumId)).where(checkKeyword(keyword)).orderBy(getVideoSortedColumn(sort)).fetch()
        .stream().distinct().collect(Collectors.toList()));
  }

  public List<VideoJoin> findBookmarkVideosByUserAndCurriculum(String userId, Long curriculumId, SortType sort,
      String keyword) {
    return tupleToJoin(multipleBookmarkFetchJoin().join(video.videoBookmarks, videoBookmark)
        .where(userEq(userId), curriculumEq(curriculumId)).where(checkKeyword(keyword))
        .orderBy(getVideoSortedColumn(sort)).fetch().stream().distinct().collect(Collectors.toList()));
  }

  private BooleanExpression userEq(String userId) {
    return commonUtils.stringNullCheck(userId) ? null : videoBookmark.userUuid.eq(userId);
  }

  private BooleanExpression idEq(String videoId) {
    return commonUtils.objectNullcheck(videoId) ? null : video.videoId.eq(videoId);
  }

  private BooleanExpression curriculumEq(Long curriculumId) {
    return commonUtils.objectNullcheck(curriculumId) ? null : video.videoCurriculum.curriculumId.eq(curriculumId);
  }

  private BooleanExpression checkKeyword(String keyword) {
    return commonUtils.stringNullCheck(keyword) ? null
        : video.title.containsIgnoreCase(keyword)
            .or(video.videoUks.any()
                .in(JPAExpressions.selectFrom(videoUkRel).where(videoUkRel.video.eq(video),
                    videoUkRel.ukMaster.ukName.contains(keyword))))
            .or(video.videoHashtags.any().in(JPAExpressions.selectFrom(videoHashtag).where(videoHashtag.video.eq(video),
                videoHashtag.hashtag.name.contains(keyword))));
  }

  private VideoJoin tupleToJoin(Tuple tuple) {
    return new VideoJoin(tuple.get(video), tuple.get(videoBookmark.userUuid));
  }

  private List<VideoJoin> tupleToJoin(List<Tuple> tuples) {
    return tuples.stream().map(tuple -> new VideoJoin(tuple.get(video), tuple.get(videoBookmark.userUuid)))
        .collect(Collectors.toList());
  }
}
