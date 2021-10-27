package com.tmax.eTest.Report.repository.diagnosis;

import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Report.model.diagnosis.VideoMappingForDiag;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepoForDiag extends JpaRepository<Video, String>{

	  List<VideoMappingForDiag> findAllByRelatedIn(Iterable<String> relatedList);
	
}