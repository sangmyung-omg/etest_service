package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.repository.video.VideoHitRepository;
import com.tmax.eTest.Common.repository.video.VideoRepository;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.VideoDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoService {

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private VideoHitRepository videoHitRepository;

  public ListDTO.Video getVideoList() {
    List<Video> videos = videoRepository.findAll();
    return new ListDTO.Video(videos.size(), videos.stream().map(o -> o.getVideoId()).collect(Collectors.toList()));
  }

  // public SuccessDTO updateVideoHit(Long videoId) {
  //   videoHitRepository.
  //   return new SuccessDTO(true);
  // }

  public VideoDTO getVideo(Long userId, Long videoId) {
    Video video = videoRepository.findById(videoId).orElseThrow(() -> new ContentsException(ErrorCode.DB_ERROR));
    VideoDTO videoDto = new VideoDTO(video.getVideoSrc(), video.getTitle(), video.getCreateDate().toString(),
        video.getCreatorId(), video.getImgSrc(), video.getSubject(), video.getTotalTime(), video.getHit().getHit(),
        false, null);
    return videoDto;
  }

}
