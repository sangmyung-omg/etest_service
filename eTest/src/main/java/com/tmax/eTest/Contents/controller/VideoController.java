package com.tmax.eTest.Contents.controller;

import java.io.IOException;
import java.text.ParseException;

import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.service.VideoService;

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

  @GetMapping("/videos/curriculums")
  public ResponseEntity<Object> getVideoCurriculumList() {
    log.info("---getVideoCurriculumList---");
    return new ResponseEntity<>(videoService.getVideoCurriculumList(), HttpStatus.OK);
  }

  @GetMapping("/videos")
  public ResponseEntity<Object> getVideoList(@RequestParam(value = "curriculumId", required = false) Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "DATE") SortType sort,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getVideoList---");
    return new ResponseEntity<>(videoService.getVideoList(curriculumId, sort, keyword), HttpStatus.OK);
  }

  @PostMapping("/videos/{id}/hit")
  public ResponseEntity<Object> updateVideoHit(@PathVariable("id") String videoId) {
    log.info("---updateVideoHit---");
    return new ResponseEntity<>(videoService.updateVideoHit(videoId), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/videos/{video_id}")
  public ResponseEntity<Object> getVideo(@PathVariable("user_id") String userId,
      @PathVariable("video_id") String videoId) {
    log.info("---getVideo---");
    return new ResponseEntity<>(videoService.getVideo(userId, videoId), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/videos")
  public ResponseEntity<Object> getVideoList(@PathVariable("user_id") String userId,
      @RequestParam(value = "curriculumId", required = false) Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "RECOMMEND") SortType sort,
      @RequestParam(value = "keyword", required = false) String keyword) throws IOException {
    log.info("---getVideoList---");
    log.info("Sort : " + sort.name());
    return new ResponseEntity<>(
        sort.equals(SortType.RECOMMEND) ? videoService.getRecommendVideoList(userId, curriculumId, keyword)
            : videoService.getVideoList(userId, curriculumId, sort, keyword),
        HttpStatus.OK);
  }

  // @GetMapping("/users/{user_id}/videos/recommend")
  // public ResponseEntity<Object> getRecommendVideoList(@PathVariable("user_id")
  // String userId,
  // @RequestParam(value = "curriculumId", required = false) Long curriculumId,
  // @RequestParam(value = "keyword", required = false) String keyword) throws
  // IOException {
  // log.info("---getRecommendVideoList---");
  // return new ResponseEntity<>(videoService.getRecommendVideoList(userId,
  // curriculumId, keyword), HttpStatus.OK);
  // }

  @GetMapping("/users/{user_id}/videos/bookmark")
  public ResponseEntity<Object> getBookmarkVideoList(@PathVariable("user_id") String userId,
      @RequestParam(value = "curriculumId", required = false) Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "DATE") SortType sort,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getBookmarkVideoList---");
    return new ResponseEntity<>(videoService.getBookmarkVideoList(userId, curriculumId, sort, keyword), HttpStatus.OK);
  }

  @PutMapping("/users/{user_id}/videos/{video_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkVideo(@PathVariable("user_id") String userId,
      @PathVariable("video_id") String videoId) {
    log.info("---insertBookmarkVideo---");
    return new ResponseEntity<>(videoService.insertBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

  @DeleteMapping("/users/{user_id}/videos/{video_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkVideo(@PathVariable("user_id") String userId,
      @PathVariable("video_id") String videoId) {
    log.info("---deleteBookmarkVideo---");
    return new ResponseEntity<>(videoService.deleteBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

  @PostMapping("/users/{user_id}/videos/{id}/hit")
  public ResponseEntity<Object> updateVideoHit(@PathVariable("user_id") String userId,
      @PathVariable("id") String videoId) throws ParseException {
    log.info("---updateVideoHit---");
    return new ResponseEntity<>(videoService.updateVideoHit(userId, videoId), HttpStatus.OK);
  }

  @PostMapping("/users/{user_id}/videos/{id}/quit")
  public ResponseEntity<Object> quitVideo(@PathVariable("user_id") String userId, @PathVariable("id") String videoId,
      @RequestParam(value = "duration", required = true) Integer duration) throws ParseException {
    log.info("---quitVideo---");
    return new ResponseEntity<>(videoService.quitVideo(userId, videoId, duration), HttpStatus.OK);
  }
}
