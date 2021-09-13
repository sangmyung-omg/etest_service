package com.tmax.eTest.Contents.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.service.VideoService;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class VideoController {

  @Autowired
  private VideoService videoService;

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  private String getUserId(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    String userId = null;
    if (header == null) {
      log.info("header is null : Unregistered User.");
    } else {
      String token = header.replace("Bearer ", "");
      Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
      userId = parseInfo.get("userUuid").toString();
      log.info("User ID : " + userId);
    }
    return userId;
  }

  @GetMapping("/videos/curriculums")
  public ResponseEntity<Object> getVideoCurriculumList() {
    log.info("---getVideoCurriculumList---");
    return new ResponseEntity<>(videoService.getVideoCurriculumList(), HttpStatus.OK);
  }

  @GetMapping("/videos")
  public ResponseEntity<Object> getVideoList(@RequestParam(value = "curriculumId", required = false) Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "DATE") SortType sort,
      @RequestParam(value = "keyword", required = false) String keyword, HttpServletRequest request)
      throws IOException {
    log.info("---getVideoList---");
    String userId = getUserId(request);
    return new ResponseEntity<>(
        CommonUtils.stringNullCheck(userId) ? videoService.getVideoList(curriculumId, sort, keyword)
            : (sort.equals(SortType.RECOMMEND) ? videoService.getRecommendVideoList(userId, curriculumId, keyword)
                : videoService.getVideoList(userId, curriculumId, sort, keyword)),
        HttpStatus.OK);
  }

  @GetMapping("/videos/{video_id}")
  public ResponseEntity<Object> getVideo(@PathVariable("video_id") String videoId, HttpServletRequest request) {
    log.info("---getVideo---");
    String userId = getUserId(request);
    return new ResponseEntity<>(
        CommonUtils.stringNullCheck(userId) ? videoService.getVideo(videoId) : videoService.getVideo(userId, videoId),
        HttpStatus.OK);
  }

  @PostMapping("/videos/{id}/hit")
  public ResponseEntity<Object> updateVideoHit(@PathVariable("id") String videoId, HttpServletRequest request)
      throws ParseException {
    log.info("---updateVideoHit---");
    String userId = getUserId(request);
    return new ResponseEntity<>(CommonUtils.stringNullCheck(userId) ? videoService.updateVideoHit(videoId)
        : videoService.updateVideoHit(userId, videoId), HttpStatus.OK);
  }

  @PostMapping("/videos/{id}/quit")
  public ResponseEntity<Object> quitVideo(@PathVariable("id") String videoId,
      @RequestParam(value = "duration", required = true) Integer duration, HttpServletRequest request)
      throws ParseException {
    log.info("---quitVideo---");
    String userId = getUserId(request);
    return new ResponseEntity<>(videoService.quitVideo(userId, videoId, duration), HttpStatus.OK);
  }

  @GetMapping("/videos/bookmark")
  public ResponseEntity<Object> getBookmarkVideoList(
      @RequestParam(value = "curriculumId", required = false) Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "DATE") SortType sort,
      @RequestParam(value = "keyword", required = false) String keyword, HttpServletRequest request) {
    log.info("---getBookmarkVideoList---");
    String userId = getUserId(request);
    return new ResponseEntity<>(videoService.getBookmarkVideoList(userId, curriculumId, sort, keyword), HttpStatus.OK);
  }

  @PutMapping("/videos/{video_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkVideo(@PathVariable("video_id") String videoId,
      HttpServletRequest request) {
    log.info("---insertBookmarkVideo---");
    String userId = getUserId(request);
    return new ResponseEntity<>(videoService.insertBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

  @DeleteMapping("/videos/{video_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkVideo(@PathVariable("video_id") String videoId,
      HttpServletRequest request) {
    log.info("---deleteBookmarkVideo---");
    String userId = getUserId(request);
    return new ResponseEntity<>(videoService.deleteBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

}
