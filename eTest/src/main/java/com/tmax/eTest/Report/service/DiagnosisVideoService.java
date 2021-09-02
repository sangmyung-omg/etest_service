package com.tmax.eTest.Report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.repository.user.UserKnowledgeRepo;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Common.repository.video.VideoRepository;
import com.tmax.eTest.Common.repository.video.VideoUkRelRepository;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.RecommendVideoDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisVideoService {

	@Autowired
	VideoBookmarkRepository videoBookmarkRepo;

	@Autowired
	UserKnowledgeRepo userKnowledgeRepo;

	@Autowired
	VideoUkRelRepository videoUkRelRepo;

	@Autowired
	VideoRepository videoRepo;

	@Autowired
	LRSAPIManager lrsManager;
<<<<<<< HEAD:eTest/src/main/java/com/tmax/eTest/Report/service/MiniTestVideoService.java

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
=======
>>>>>>> 69495b2... [feat] Diagnosis Main Record 에 정보 추가.:eTest/src/main/java/com/tmax/eTest/Report/service/DiagnosisVideoService.java

<<<<<<< HEAD
	public boolean setVideoBookmark(String userId, String videoId, boolean isCheckBookmark) throws Exception {
		try {
			if (isCheckBookmark) {
=======
	public boolean setVideoBookmark(String userId, String videoId, boolean isCheckBookmark) throws Exception
	{
		try
		{
			if(isCheckBookmark)
			{
>>>>>>> 90b72dc... [feat] VideoId String 화에 따른 변경.(Master Pull 당긴 후 나오는 에러 처리를 위해 dev에서 작업)
				VideoBookmark bookmarkModel = new VideoBookmark(userId, videoId);
				videoBookmarkRepo.save(bookmarkModel);
			} else {
				VideoBookmarkId bookmarkId = new VideoBookmarkId(userId, videoId);
				videoBookmarkRepo.deleteById(bookmarkId);
			}
		} catch (Exception e) {
			if (isCheckBookmark)
				throw new ReportBadRequestException("Save Video Bookmark error. Check userID & videoID.", e);
			else
				throw new ReportBadRequestException("Delete Video Bookmark error. Check userID & videoID.", e);
		}

		return true;
	}
<<<<<<< HEAD:eTest/src/main/java/com/tmax/eTest/Report/service/MiniTestVideoService.java

	public List<VideoResultDTO> getRecommendVideo(String userId) throws Exception {
		List<VideoResultDTO> result = new ArrayList<>();
		GetStatementInfoDTO statementDto = new GetStatementInfoDTO();

		statementDto.pushUserId(userId);
		statementDto.pushActionType("play_video");
		statementDto.pushSourceType("video");

		Map<Long, Boolean> viewedVideo = new HashMap<>();

		List<StatementDTO> lrsRes = lrsManager.getStatementList(statementDto);
		List<UserKnowledge> ukRes = userKnowledgeRepo.findUKListByUserUuid(userId);

		Long[] ukIdList = { 0L, 0L, 0L };

		for (StatementDTO state : lrsRes) {
			try {
				long videoId = Long.getLong(state.getSourceId());
				viewedVideo.put(videoId, true);
			} catch (Exception e) {
				logger.info("in getRecommend " + e.toString());
			}
		}

		int idx = 0;
		for (UserKnowledge uk : ukRes) {
			ukIdList[idx] = (long) uk.getUkId();

			logger.info(uk.getUkId() + " " + uk.getUkMastery() + " " + ukIdList[idx] + "");
			idx++;
			if (idx >= 3)
				break;
		}

		List<Long> videoIds = videoUkRelRepo.findVideoIdFrom3UkId(ukIdList[0], ukIdList[1], ukIdList[2]);

		while (result.size() <= 5 && videoIds.size() != 0) {
			Random rand = new Random();
			int randomIdx = rand.nextInt(videoIds.size());

			if (viewedVideo.get(videoIds.get(randomIdx)) == null) {
				Optional<Video> opt = videoRepo.findById(videoIds.get(randomIdx));

				if (opt.isPresent()) {
					Video recVideo = opt.get();
					VideoResultDTO output = new VideoResultDTO();
					output.setParamByVideoModel(recVideo);
					result.add(output);
				}
			}

			videoIds.remove(randomIdx);
		}

		return result;

	}
=======
>>>>>>> 69495b2... [feat] Diagnosis Main Record 에 정보 추가.:eTest/src/main/java/com/tmax/eTest/Report/service/DiagnosisVideoService.java
}
