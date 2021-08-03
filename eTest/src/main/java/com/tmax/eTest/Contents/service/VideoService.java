package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.model.video.VideoCurriculum;
import com.tmax.eTest.Common.model.video.VideoJoin;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Common.repository.video.VideoCurriculumRepository;
import com.tmax.eTest.Common.repository.video.VideoHitRepository;
import com.tmax.eTest.Common.repository.video.VideoRepository;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.dto.VideoCurriculumDTO;
import com.tmax.eTest.Contents.dto.VideoDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
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

  @Autowired
  private VideoBookmarkRepository videoBookmarkRepository;

  private enum SortType {
    date("createDate"), hit("videoHit.hit");

    @Getter
    private final String type;

    private SortType(String type) {
      this.type = type;
    }
  }

  public ListDTO.Curriculum getVideoCurriculumList() {
    List<VideoCurriculum> curriculums = videoCurriculumRepository.findAll();
    return new ListDTO.Curriculum(curriculums.size(),
        curriculums.stream()
            .map(curriculum -> new VideoCurriculumDTO(curriculum.getCurriculumId(), curriculum.getSubject()))
            .collect(Collectors.toList()));
  }

  public ListDTO.Video getVideoList(String userId, Long curriculumId, String sort) {
    List<VideoJoin> videoJoins = curriculumId == 0
        ? videoRepository.getUserVideoListAndSort(userId, Sort.by(SortType.valueOf(sort).getType()))
        : videoRepository.getUserVideoListByCurriculumIdAndSort(userId, curriculumId,
            Sort.by(SortType.valueOf(sort).getType()));
    return new ListDTO.Video(videoJoins.size(), videoJoins.stream().map(videoJoin -> {
      Video video = videoJoin.getVideo();
      return new VideoDTO(video.getVideoId(), video.getVideoSrc(), video.getTitle(), video.getCreateDate().toString(),
          video.getCreatorId(), video.getImgSrc(), video.getVideoCurriculum().getSubject(), video.getTotalTime(),
          video.getVideoHit().getHit(), !CommonUtils.stringNullCheck(videoJoin.getUserUuid()), video.getVideoUks()
              .stream().map(videoUks -> videoUks.getUkMaster().getUkName()).collect(Collectors.toList()));
    }).collect(Collectors.toList()));
  }

  public ListDTO.Video getBookmarkVideoList(String userId, Long curriculumId, String sort) {
    List<Video> videos = curriculumId == 0
        ? videoRepository.findAllByVideoBookmarksUserUuid(userId, Sort.by(SortType.valueOf(sort).getType()))
        : videoRepository.findAllByVideoBookmarksUserUuidAndCurriculumId(userId, curriculumId,
            Sort.by(SortType.valueOf(sort).getType()));
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
    // videoBookmarkRepository.deleteById(videoBookmarkId);
    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateVideoHit(Long videoId) {
    videoHitRepository.findById(videoId)
        .orElseThrow(() -> new ContentsException(ErrorCode.DB_ERROR, "VideoId doesn't exist in VideoHit Table"));
    videoHitRepository.updateHit(videoId);
    return new SuccessDTO(true);
  }

}
