package com.tmax.eTest.Report.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.repository.user.UserKnowledgeRepo;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.LRSAPIManager;

@Service
public class MiniTestVideoService {

	@Autowired
	VideoBookmarkRepository videoBookmarkRepo;
	
	@Autowired
	UserKnowledgeRepo userKnowledgeRepo;
	
	@Autowired
	LRSAPIManager lrsManager;
	

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public boolean setVideoBookmark(String userId, long videoId, boolean isCheckBookmark) throws Exception
	{
		try
		{
			if(isCheckBookmark)
			{
				VideoBookmark bookmarkModel = new VideoBookmark(userId, videoId);
				videoBookmarkRepo.save(bookmarkModel);
			}
			else
			{
				VideoBookmarkId bookmarkId = new VideoBookmarkId(userId, videoId);
				videoBookmarkRepo.deleteById(bookmarkId);
			}
		}
		catch(Exception e)
		{
			if(isCheckBookmark)
				throw new ReportBadRequestException("Save Video Bookmark error. Check userID & videoID.", e);
			else
				throw new ReportBadRequestException("Delete Video Bookmark error. Check userID & videoID.", e);
		}

		return true;
	}
	
	public void getRecommendVideo(String userId) throws Exception
	{
		GetStatementInfoDTO statementDto = new GetStatementInfoDTO();
		
		statementDto.pushUserId(userId);
		statementDto.pushActionType("play_video");
		statementDto.pushSourceType("video");
		
		List<StatementDTO> lrsRes= lrsManager.getStatementList(statementDto);
		List<UserKnowledge> ukRes = userKnowledgeRepo.findListByUserUuid(userId);
		
	}
}
