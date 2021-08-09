package com.tmax.eTest.Common.repository.video;

import com.tmax.eTest.Common.model.video.VideoUkRel;
import com.tmax.eTest.Common.model.video.VideoUkRelId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoUkRelRepository extends JpaRepository<VideoUkRel, VideoUkRelId> {

}
