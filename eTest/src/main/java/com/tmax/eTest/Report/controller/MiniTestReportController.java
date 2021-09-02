package com.tmax.eTest.Report.controller;

import java.util.List;

import com.tmax.eTest.Report.dto.MiniTestRecordDTO;
import com.tmax.eTest.Report.dto.RecommendVideoDTO;
import com.tmax.eTest.Report.service.MiniTestRecordService;
import com.tmax.eTest.Report.service.MiniTestScoreService;
import com.tmax.eTest.Report.service.DiagnosisVideoService;
import com.tmax.eTest.Test.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiniTestReportController {

	@Autowired
	MiniTestScoreService miniTestScoreService;
	@Autowired
	DiagnosisVideoService miniTestVideoService;
	@Autowired
	MiniTestRecordService miniTestRecordService;

	@Autowired
	UserInfoService userService;

	@CrossOrigin("*")
	@PutMapping(value = "/report/miniTest/saveResult/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public boolean saveMiniTestResult(@PathVariable("id") String id, @PathVariable("probSetId") String probSetId)
			throws Exception {

		// Saving user ID of not-logged-in users - by S.M.
		// String updateResult = userService.updateUserInfo(id);
		// ------------------------------------------------

		boolean output = miniTestScoreService.saveMiniTestResult(id, probSetId);

		return output;
	}

	@CrossOrigin("*")
	@GetMapping(value = "/report/miniTest/record/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public ResponseEntity<?> miniTestRecord(@PathVariable("id") String id, @PathVariable("probSetId") String probSetId)
			throws Exception {

		// Saving user ID of not-logged-in users - by S.M.
		// String updateResult = userService.updateUserInfo(id);
		// ------------------------------------------------

		MiniTestRecordDTO output = miniTestRecordService.getMiniTestRecord(id, probSetId);

		return ResponseEntity.ok().body(output);
	}

	@CrossOrigin("*")
	@PutMapping(value = "/report/miniTestResult/recommendVideoBookmark/{userId}/{videoId}/{isBookmarkOn}", produces = "application/json; charset=utf-8")
	public boolean setVideoBookmark(@PathVariable("userId") String userId, @PathVariable("videoId") String videoId,
			@PathVariable("isBookmarkOn") Boolean isBookmarkOn) throws Exception {

		return miniTestVideoService.setVideoBookmark(userId, videoId, isBookmarkOn);
	}
<<<<<<< HEAD

	@CrossOrigin("*")
	@GetMapping(value = "/report/miniTestResult/recommendVideo/{userId}", produces = "application/json; charset=utf-8")
	public List<VideoResultDTO> recommendVideo(@PathVariable("userId") String userId) throws Exception {

		return miniTestVideoService.getRecommendVideo(userId);

	}

=======
>>>>>>> 69495b2... [feat] Diagnosis Main Record 에 정보 추가.
}
