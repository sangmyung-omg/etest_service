package com.tmax.eTest.Common.repository.video;

import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoBookmarkRepository extends JpaRepository<VideoBookmark, VideoBookmarkId> {
  long countByUserUuid(String userUuid);
}
