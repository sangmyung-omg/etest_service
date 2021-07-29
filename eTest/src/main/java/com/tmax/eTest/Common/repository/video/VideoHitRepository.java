package com.tmax.eTest.Common.repository.video;

import com.tmax.eTest.Common.model.video.VideoHit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoHitRepository extends JpaRepository<VideoHit, Long> {

  @Modifying
  @Query("update VideoHit vh set vh.hit = vh.hit + 1 where vh.videoId = :videoId")
  Integer updateHit(@Param("videoId") Long videoId);
}
