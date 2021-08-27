package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.model.video.VideoCurriculum;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.dto.VideoCurriculumDTO;
import com.tmax.eTest.Contents.dto.VideoDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.VideoCurriculumRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoHitRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoService {

  @Autowired
  private VideoCurriculumRepositorySupport videoCurriculumRepositorySupport;

  @Autowired
  private VideoHitRepositorySupport videoHitRepositorySupport;

  @Autowired
  private VideoRepositorySupport videoRePositorySupport;

  @Autowired
  private VideoBookmarkRepository videoBookmarkRepository;

  public ListDTO.Curriculum getVideoCurriculumList() {
    List<VideoCurriculum> curriculums = videoCurriculumRepositorySupport.findAll();

    return new ListDTO.Curriculum(curriculums.size(),
        curriculums.stream()
            .map(curriculum -> new VideoCurriculumDTO(curriculum.getCurriculumId(), curriculum.getSubject()))
            .collect(Collectors.toList()));
  }

  public ListDTO.Video getVideoList(String userId, Long curriculumId, SortType sort) {
    List<Video> videos = curriculumId == 0 ? videoRePositorySupport.findVideosByUser(userId, sort)
        : videoRePositorySupport.findVideosByUserAndCurriculum(userId, curriculumId, sort);
        
    return new ListDTO.Video(videos.size(),
        videos.stream()
            .map(video -> new VideoDTO(video.getVideoId(), video.getVideoSrc(), video.getTitle(),
                video.getCreateDate().toString(), video.getCreatorId(), video.getImgSrc(),
                video.getVideoCurriculum().getSubject(), video.getTotalTime(), video.getVideoHit().getHit(),
                !CommonUtils.objectNullcheck(video.getVideoBookmarks()), video.getVideoUks().stream()
                    .map(videoUks -> videoUks.getUkMaster().getUkName()).collect(Collectors.toList())))
            .collect(Collectors.toList()));
  }

  public ListDTO.Video getBookmarkVideoList(String userId, Long curriculumId, SortType sort) {
    List<Video> videos = curriculumId == 0 ? videoRePositorySupport.findBookmarkVideosByUser(userId, sort)
        : videoRePositorySupport.findBookmarkVideosByUserAndCurriculum(userId, curriculumId, sort);

    return new ListDTO.Video(videos.size(),
        videos.stream()
            .map(video -> new VideoDTO(
                video.getVideoId(), video.getVideoSrc(), video.getTitle(), video.getCreateDate().toString(),
                video.getCreatorId(), video.getImgSrc(), video.getVideoCurriculum().getSubject(), video.getTotalTime(),
                video.getVideoHit().getHit(), true, video.getVideoUks().stream()
                    .map(videoUks -> videoUks.getUkMaster().getUkName()).collect(Collectors.toList())))
            .collect(Collectors.toList()));
  }

  @Transactional
  public SuccessDTO insertBookmarkVideo(String userId, Long videoId) {
    // VideoBookmarkId videoBookmarkId = new VideoBookmarkId(userId, videoId);
    // videoBookmarkRepository.findById(videoBookmarkId).orElseThrow()
    VideoBookmarkId videoBookmarkId = new VideoBookmarkId(userId, videoId);
    VideoBookmark videoBookmark = new VideoBookmark(userId, videoId);
    // videoBookmarkRepository.findById(videoBookmarkId).ifPresentOrElse(
    // (value) -> new ContentsException(ErrorCode.DB_ERROR, "Video Bookmark doesn't
    // exist!!"),
    // () -> videoBookmarkRepository.saveAndFlush(videoBookmark));
    // videoBookmarkRepository.findById(videoBookmarkId).orElseGet(() ->
    // videoBookmarkRepository.save(videoBookmark));
    if (videoBookmarkRepository.existsById(videoBookmarkId))
      throw new ContentsException(ErrorCode.DB_ERROR, "VideoBookmark already exists in VideoBookmark Table");
    else
      videoBookmarkRepository.save(videoBookmark);

    // videoBookmarkRepository.findById(videoBookmarkId).orElseGet(() ->
    // videoBookmarkRepository.save(videoBookmark)).equals(videoBookmark)

    // videoBookmarkRepository.save(videoBookmark);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO deleteBookmarkVideo(String userId, Long videoId) {
    VideoBookmarkId videoBookmarkId = new VideoBookmarkId(userId, videoId);
    videoBookmarkRepository.delete(videoBookmarkRepository.findById(videoBookmarkId).orElseThrow(
        () -> new ContentsException(ErrorCode.DB_ERROR, "VideoBookmark doesn't exist in VideoBookmark Table")));

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateVideoHit(Long videoId) {
    if (videoHitRepositorySupport.notExistsById(videoId))
      throw new ContentsException(ErrorCode.DB_ERROR, "VideoId doesn't exist in VideoHit Table");
    videoHitRepositorySupport.updateVideoHit(videoId);

    return new SuccessDTO(true);
  }

}
