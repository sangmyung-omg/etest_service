package com.tmax.eTest.Contents.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.service.VideoService;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.JWTUtils;

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

  private VideoService videoService;

  private CommonUtils commonUtils;

  private JWTUtils jwtUtils;

  public VideoController(VideoService videoService, CommonUtils commonUtils, JWTUtils jwtUtils) {
    this.videoService = videoService;
    this.commonUtils = commonUtils;
    this.jwtUtils = jwtUtils;
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
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(
        commonUtils.stringNullCheck(userId) ? videoService.getVideoList(curriculumId, sort, keyword)
            : (sort.equals(SortType.RECOMMEND) ? videoService.getRecommendVideoList(userId, curriculumId, keyword)
                : videoService.getVideoList(userId, curriculumId, sort, keyword)),
        HttpStatus.OK);
  }

  @GetMapping("/videos/{video_id}")
  public ResponseEntity<Object> getVideo(@PathVariable("video_id") String videoId, HttpServletRequest request) {
    log.info("---getVideo---");
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(
        commonUtils.stringNullCheck(userId) ? videoService.getVideo(videoId) : videoService.getVideo(userId, videoId),
        HttpStatus.OK);
  }

  @PostMapping("/videos/{video_id}/hit")
  public ResponseEntity<Object> updateVideoHit(@PathVariable("video_id") String videoId,
      @RequestParam(value = "videoType", required = true) VideoService.VideoType videoType, HttpServletRequest request)
      throws ParseException {
    log.info("---updateVideoHit---");
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(commonUtils.stringNullCheck(userId) ? videoService.updateVideoHit(videoId, videoType)
        : videoService.updateVideoHit(userId, videoId, videoType), HttpStatus.OK);
  }

  @PostMapping("/videos/{video_id}/quit")
  public ResponseEntity<Object> quitVideo(@PathVariable("video_id") String videoId,
      @RequestParam(value = "duration", required = true) Integer duration, HttpServletRequest request)
      throws ParseException {
    log.info("---quitVideo---");
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(commonUtils.stringNullCheck(userId) ? videoService.quitVideo(videoId, duration)
        : videoService.quitVideo(userId, videoId, duration), HttpStatus.OK);
  }

  @GetMapping("/videos/bookmark")
  public ResponseEntity<Object> getBookmarkVideoList(
      @RequestParam(value = "curriculumId", required = false) Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "DATE") SortType sort,
      @RequestParam(value = "keyword", required = false) String keyword, HttpServletRequest request) {
    log.info("---getBookmarkVideoList---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(videoService.getBookmarkVideoList(userId, curriculumId, sort, keyword), HttpStatus.OK);
  }

  @PutMapping("/videos/{video_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkVideo(@PathVariable("video_id") String videoId,
      HttpServletRequest request) {
    log.info("---insertBookmarkVideo---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(videoService.insertBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

  @DeleteMapping("/videos/{video_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkVideo(@PathVariable("video_id") String videoId,
      HttpServletRequest request) {
    log.info("---deleteBookmarkVideo---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(videoService.deleteBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

  @GetMapping("/videos/watch")
  public ResponseEntity<Object> getWatchedVideoList(HttpServletRequest request) {
    log.info("---getWatchedVideoList---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(videoService.getWatchVideoList(userId), HttpStatus.OK);
  }

  @DeleteMapping("/videos/watch")
  public ResponseEntity<Object> deleteWatchVideoList(HttpServletRequest request) {
    log.info("---deleteWatchVideoList---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(videoService.deleteWatchVideoList(userId), HttpStatus.OK);
  }

  @DeleteMapping("/videos/{video_id}/watch")
  public ResponseEntity<Object> deleteWatchVideo(@PathVariable("video_id") String videoId, HttpServletRequest request) {
    log.info("---deleteWatchVideo---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(videoService.deleteWatchVideo(userId, videoId), HttpStatus.OK);
  }
}
