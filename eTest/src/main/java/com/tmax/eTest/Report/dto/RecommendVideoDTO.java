package com.tmax.eTest.Report.dto;

import com.tmax.eTest.Common.model.video.Video;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecommendVideoDTO {
	
	long id;
	String videoSrcUrl;
	String title;
	String thumbnailUrl;
	float totalTime;
	int hit;
	boolean isBookmark;
	
	public RecommendVideoDTO(Video videoModel, int hit, boolean isBookmark){
		setParamByVideoModel(videoModel, hit, isBookmark);
	}

	public void setParamByVideoModel(Video videoModel, int hit, boolean isBookmark) {
		this.id = videoModel.getVideoId();
		this.videoSrcUrl = videoModel.getVideoSrc();
		this.title = videoModel.getTitle();
		this.thumbnailUrl = videoModel.getImgSrc();
		this.hit = hit;
		this.isBookmark = isBookmark;
		this.totalTime = videoModel.getTotalTime();
	}
}
