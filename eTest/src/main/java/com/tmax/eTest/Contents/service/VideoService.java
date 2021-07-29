package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoCurriculum;
import com.tmax.eTest.Common.repository.video.VideoCurriculumRepository;
import com.tmax.eTest.Common.repository.video.VideoHitRepository;
import com.tmax.eTest.Common.repository.video.VideoRepository;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.dto.VideoCurriculumDTO;
import com.tmax.eTest.Contents.dto.VideoDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoService {

  @Autowired
  private VideoCurriculumRepository videoCurriculumRepository;

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private VideoHitRepository videoHitRepository;

  public ListDTO.Curriculum getVideoCurriculumList() {
    List<VideoCurriculum> curriculums = videoCurriculumRepository.findAll();
    return new ListDTO.Curriculum(curriculums.size(),
        curriculums.stream()
            .map(curriculum -> new VideoCurriculumDTO(curriculum.getCurriculumId(), curriculum.getSubject()))
            .collect(Collectors.toList()));
  }

  public ListDTO.Video getVideoList(Long userId) {
    List<Video> videos = videoRepository.findAll();
    return new ListDTO.Video(videos.size(),
        videos.stream()
            .map(video -> new VideoDTO(video.getVideoId(), video.getVideoSrc(), video.getTitle(),
                video.getCreateDate().toString(), video.getCreatorId(), video.getImgSrc(),
                video.getVideoCurriculum().getSubject(), video.getTotalTime(), video.getHit().getHit(), false, null))
            .collect(Collectors.toList()));
  }

  public ListDTO.Video getBookmarkVideoList(Long userId) {
    List<Video> videos = videoRepository.findAll();
    return new ListDTO.Video(videos.size(),
        videos.stream()
            .map(video -> new VideoDTO(video.getVideoId(), video.getVideoSrc(), video.getTitle(),
                video.getCreateDate().toString(), video.getCreatorId(), video.getImgSrc(),
                video.getVideoCurriculum().getSubject(), video.getTotalTime(), video.getHit().getHit(), true, null))
            .collect(Collectors.toList()));
  }

  @Transactional
  public SuccessDTO updateVideoHit(Long videoId) {
    log.info("VideoId: " + videoId);
    Integer isSuccess = videoHitRepository.updateHit(videoId);
    if (isSuccess == 1)
      return new SuccessDTO(true);
    else
      throw new ContentsException(ErrorCode.DB_ERROR);
  }

}
