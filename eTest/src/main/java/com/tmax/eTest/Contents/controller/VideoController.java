package com.tmax.eTest.Contents.controller;

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

  @GetMapping("/users/{user_id}/videos")
  public ResponseEntity<Object> getVideoList(@PathVariable("user_id") String userId,
      @RequestParam(value = "curriculumId", required = false, defaultValue = "0") Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "date") SortType sort) {
    log.info("---getVideoList---");
    return new ResponseEntity<>(videoService.getVideoList(userId, curriculumId, sort), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/videos/bookmark")
  public ResponseEntity<Object> getBookmarkVideoList(@PathVariable("user_id") String userId,
      @RequestParam(value = "curriculumId", required = false, defaultValue = "0") Long curriculumId,
      @RequestParam(value = "sort", required = false, defaultValue = "date") SortType sort) {
    log.info("---getBookmarkVideoList---");
    return new ResponseEntity<>(videoService.getBookmarkVideoList(userId, curriculumId, sort), HttpStatus.OK);
  }

  @PutMapping("/users/{user_id}/videos/{video_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkVideo(@PathVariable("user_id") String userId,
      @PathVariable("video_id") Long videoId) {
    log.info("---insertBookmarkVideo---");
    return new ResponseEntity<>(videoService.insertBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

  @DeleteMapping("/users/{user_id}/videos/{video_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkVideo(@PathVariable("user_id") String userId,
      @PathVariable("video_id") Long videoId) {
    log.info("---deleteBookmarkVideo---");
    return new ResponseEntity<>(videoService.deleteBookmarkVideo(userId, videoId), HttpStatus.OK);
  }

  @PostMapping("/videos/{id}/hit")
  public ResponseEntity<Object> updateVideoHit(@PathVariable("id") Long videoId) {
    log.info("---updateVideoHit---");
    return new ResponseEntity<>(videoService.updateVideoHit(videoId), HttpStatus.OK);
  }

}
