package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.video.QVideoCurriculum.videoCurriculum;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.video.VideoCurriculum;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;;

@Repository
public class VideoCurriculumRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  public VideoCurriculumRepositorySupport(JPAQueryFactory query) {
    super(VideoCurriculum.class);
    this.query = query;
  }

  public List<VideoCurriculum> findAll() {
    return query.selectFrom(videoCurriculum).fetch();
  }
}
