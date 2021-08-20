package com.tmax.eTest.Report.dto;

import java.util.List;

import com.tmax.eTest.Common.model.video.Video;

import lombok.Data;

@Data
public class VideoResultDTO {
	String videoSrcUrl;
	String title;
	String createDate;
	String creatorId;
	String imgSrcUrl;
	
	public void setParamByVideoModel(Video videoModel)
	{
		this.videoSrcUrl = videoModel.getVideoSrc();
		this.title = videoModel.getTitle();
		this.createDate = videoModel.getCreateDate().toString();
		this.creatorId = videoModel.getCreatorId();
		this.imgSrcUrl = videoModel.getImgSrc();
	}
}
