package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.video.QVideoHit.videoHit;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.video.VideoHit;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class VideoHitRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  public VideoHitRepositorySupport(JPAQueryFactory query) {
    super(VideoHit.class);
    this.query = query;
  }

  // 애매한뎅
  public Boolean notExistsById(String videoId) {
    return commonUtils
        .objectNullcheck(query.selectOne().from(videoHit).where(videoHit.videoId.eq(videoId)).fetchFirst());
  }

  public Long updateVideoHit(String videoId) {
    return query.update(videoHit).set(videoHit.hit, videoHit.hit.add(1)).where(videoHit.videoId.eq(videoId)).execute();
  }
}
