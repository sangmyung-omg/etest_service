package com.tmax.eTest.Contents.controller;

import com.tmax.eTest.Contents.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class VideoController {

  @Autowired
  private VideoService videoService;

  @GetMapping("/videos/curriculums")
  public ResponseEntity<Object> getVideoCurriculumList() {
    return new ResponseEntity<>(videoService.getVideoCurriculumList(), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/videos")
  public ResponseEntity<Object> getVideoList(@PathVariable("user_id") Long userId) {
    return new ResponseEntity<>(videoService.getVideoList(userId), HttpStatus.OK);
  }

  @PostMapping("/videos/{id}/hit")
  public ResponseEntity<Object> updateVideoHit(@PathVariable("id") Long videoId) {
    return new ResponseEntity<>(videoService.updateVideoHit(videoId), HttpStatus.OK);
  }

}
