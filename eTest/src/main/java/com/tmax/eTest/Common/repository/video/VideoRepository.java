package com.tmax.eTest.Common.repository.video;

import java.util.List;

import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoJoin;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

  @Query("select new com.tmax.eTest.Common.model.video.VideoJoin(v, vb.userUuid) from Video v left outer join VideoBookmark vb on (v.videoId = vb.videoId and vb.userUuid = :user_id)")
  List<VideoJoin> getUserVideoListAndSort(@Param("user_id") String userId, Sort sort);

  @Query("select new com.tmax.eTest.Common.model.video.VideoJoin(v, vb.userUuid) from Video v left outer join VideoBookmark vb on (v.videoId = vb.videoId and vb.userUuid = :user_id) where v.curriculumId = :curriculum_id")
  List<VideoJoin> getUserVideoListByCurriculumIdAndSort(@Param("user_id") String userId,
      @Param("curriculum_id") Long curriculumId, Sort sort);

  List<Video> findAllByVideoBookmarksUserUuid(String userId, Sort sort);

  List<Video> findAllByVideoBookmarksUserUuidAndCurriculumId(String userId, Long curriculumId, Sort sort);
}
