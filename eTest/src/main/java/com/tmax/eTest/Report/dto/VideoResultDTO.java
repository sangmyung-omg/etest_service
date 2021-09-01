package com.tmax.eTest.Report.dto;

import com.tmax.eTest.Common.model.video.Video;

import lombok.Data;

@Data
public class VideoResultDTO {
	String videoSrcUrl;
	String title;
	String createDate;
	String imgSrcUrl;

	public void setParamByVideoModel(Video videoModel) {
		this.videoSrcUrl = videoModel.getVideoSrc();
		this.title = videoModel.getTitle();
		this.createDate = videoModel.getCreateDate().toString();
		this.imgSrcUrl = videoModel.getImgSrc();
	}
}
