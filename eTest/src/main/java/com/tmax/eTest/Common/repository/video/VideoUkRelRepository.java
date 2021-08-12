package com.tmax.eTest.Common.repository.video;

import com.tmax.eTest.Common.model.video.VideoUkRel;
import com.tmax.eTest.Common.model.video.VideoUkRelId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoUkRelRepository extends JpaRepository<VideoUkRel, VideoUkRelId> {
	 
	@Query("select distinct vur.videoId from com.tmax.eTest.Common.model.video.VideoUkRel vur where vur.ukId = :ukId1 or vur.ukId = :ukId2 or vur.ukId = :ukId3")
	List<Long> findVideoIdFrom3UkId(
			@Param("ukId1") Long ukId1,
			@Param("ukId2") Long ukId2,
			@Param("ukId3") Long ukId3);
}
