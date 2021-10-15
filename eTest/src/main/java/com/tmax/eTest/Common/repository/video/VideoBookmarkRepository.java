package com.tmax.eTest.Common.repository.video;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoBookmarkRepository extends JpaRepository<VideoBookmark, VideoBookmarkId> {
  long countByUserUuid(String userUuid);
  @Query(value = "SELECT * FROM Video_Bookmark WHERE USER_UUID = :USER_UUID", nativeQuery = true)
  List<VideoBookmark> findAllByUserUuid(@Param("USER_UUID") String UserUuid);
}
