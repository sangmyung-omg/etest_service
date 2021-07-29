package com.tmax.eTest.Contents.controller;

import com.tmax.eTest.Contents.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class VideoController {

  @Autowired
  private VideoService videoService;

  @GetMapping("/videos")
  public ResponseEntity<Object> getVideoList() {
    return new ResponseEntity<>(videoService.getVideoList(), HttpStatus.OK);
  }

  // @PostMapping("/videos/{id}/hit")
  // public ResponseEntity<Object> updateVideoHit(@PathVariable("id") Long videoId){
  //   return new ResponseEntity<>(videoService.updateVideoHit(videoId), HttpStatus.OK);
  // }
  @GetMapping("/users/{user_id}/videos/{video_id}")
  public ResponseEntity<Object> getVideo(@PathVariable("user_id") Long userId, @PathVariable("video_id") Long videoId) {
    return new ResponseEntity<>(videoService.getVideo(userId, videoId), HttpStatus.OK);
  }

}
