package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.video.QVideo.video;
import static com.tmax.eTest.Common.model.video.QVideoBookmark.videoBookmark;

import java.util.List;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.util.QuerydslUtils;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class VideoRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  public VideoRepositorySupport(JPAQueryFactory query) {
    super(Video.class);
    this.query = query;
  }

  public OrderSpecifier<?> getVideoSortedColumn(SortType sort) {
    switch (sort.toString()) {
      case "date":
        return QuerydslUtils.getSortedColumn(Order.ASC, video, "createDate");
      case "hit":
        return QuerydslUtils.getSortedColumn(Order.ASC, video.videoHit, "hit");
      default:
        throw new ContentsException(ErrorCode.TYPE_ERROR, "Sort should be 'date' or 'hit' !!!");
    }
  }

  public List<Video> findVideosByUser(String userId, SortType sort) {
    return query.selectFrom(video).leftJoin(video.videoBookmarks, videoBookmark).on(videoBookmark.userUuid.eq(userId))
        .orderBy(getVideoSortedColumn(sort)).fetch();
  }

  public List<Video> findVideosByUserAndCurriculum(String userId, Long curriculumId, SortType sort) {
    return query.selectFrom(video).leftJoin(video.videoBookmarks, videoBookmark).on(videoBookmark.userUuid.eq(userId))
        .where(video.curriculumId.eq(curriculumId)).orderBy(getVideoSortedColumn(sort)).fetch();
  }

  public List<Video> findBookmarkVideosByUser(String userId, SortType sort) {
    return query.selectFrom(video).join(video.videoBookmarks, videoBookmark).where(videoBookmark.userUuid.eq(userId))
        .orderBy(getVideoSortedColumn(sort)).fetch();
  }

  public List<Video> findBookmarkVideosByUserAndCurriculum(String userId, Long curriculumId, SortType sort) {
    return query.selectFrom(video).join(video.videoBookmarks, videoBookmark)
        .where(videoBookmark.userUuid.eq(userId), video.curriculumId.eq(curriculumId))
        .orderBy(getVideoSortedColumn(sort)).fetch();
  }
}
