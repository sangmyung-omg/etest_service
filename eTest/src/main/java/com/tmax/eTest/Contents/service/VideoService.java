package com.tmax.eTest.Contents.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.model.video.VideoCurriculum;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.RecommendInfo;
import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.dto.VideoCurriculumDTO;
import com.tmax.eTest.Contents.dto.VideoDTO;
import com.tmax.eTest.Contents.dto.VideoJoin;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.DiagnosisReportRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoCurriculumRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoHitRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSUtils;
import com.tmax.eTest.LRS.util.LRSAPIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoService {

  private enum VideoType {
    YOUTUBE, SELF
  }

  private final String YOUTUBE_TYPE = "youtu.be";

  @Autowired
  private VideoCurriculumRepositorySupport videoCurriculumRepositorySupport;

  @Autowired
  private VideoHitRepositorySupport videoHitRepositorySupport;

  @Autowired
  private VideoRepositorySupport videoRePositorySupport;

  @Autowired
  private VideoBookmarkRepository videoBookmarkRepository;

  @Autowired
  private LRSUtils lrsUtils;

  @Autowired
  private LRSAPIManager lrsapiManager;

  @Autowired
  private DiagnosisReportRepositorySupport diagnosisReportRepositorySupport;

  public ListDTO.Curriculum getVideoCurriculumList() {
    List<VideoCurriculum> curriculums = videoCurriculumRepositorySupport.findAll();
    return convertVideoCurriculumToDTO(curriculums);
  }

  public VideoDTO getVideo(String videoId) {
    Video video = videoRePositorySupport.findVideoById(videoId);
    return convertVideoToDTO(video);
  }

  public VideoDTO getVideo(String userId, String videoId) {
    VideoJoin video = videoRePositorySupport.findVideoByUserAndId(userId, videoId);
    return convertVideoJoinToDTO(video);
  }

  public ListDTO.Video getVideoList(Long curriculumId, SortType sort, String keyword) {
    List<Video> videos = videoRePositorySupport.findVideosByCurriculum(curriculumId, sort, keyword);
    return convertVideoToDTO(videos);
  }

  public ListDTO.Video getVideoList(String userId, Long curriculumId, SortType sort, String keyword) {
    List<VideoJoin> videos = videoRePositorySupport.findVideosByUserAndCurriculum(userId, curriculumId, sort, keyword);
    return convertVideoJoinToDTO(videos);
  }

  public ListDTO.Video getRecommendVideoList(String userId, Long curriculumId, String keyword) throws IOException {
    List<String> recommendVideos = getDiagnosisRecommendVideoList(userId);
    Boolean recommended = !CommonUtils.objectNullcheck(recommendVideos);

    List<VideoJoin> videos = videoRePositorySupport.findVideosByUserAndCurriculum(userId, curriculumId,
        SortType.RECOMMEND, keyword);
    return recommended ? convertVideoJoinToDTO(filtering(videos, recommendVideos), recommended)
        : convertVideoJoinToDTO(videos, recommended);
  }

  private List<VideoJoin> filtering(List<VideoJoin> videoJoins, List<String> recommends) {
    Map<Boolean, List<VideoJoin>> videoMap = videoJoins.stream()
        .collect(Collectors.partitioningBy(e -> recommends.contains(e.getVideo().getVideoId())));
    return Stream.concat(videoMap.get(true).stream(), videoMap.get(false).stream()).collect(Collectors.toList());
  }

  private List<String> getDiagnosisRecommendVideoList(String userId) throws IOException {
    DiagnosisReport diagnosisReport = diagnosisReportRepositorySupport.findDiagnosisReportByUser(userId);
    if (CommonUtils.objectNullcheck(diagnosisReport)) {
      log.info("Diagnosis Report doesn't contain " + userId);
      return null;
    }
    String basicStr = diagnosisReport.getRecommendBasicList();
    String typeStr = diagnosisReport.getRecommendTypeList();
    String advancedStr = diagnosisReport.getRecommendAdvancedList();
    ObjectMapper mapper = new ObjectMapper();
    if (CommonUtils.stringNullCheck(basicStr) || CommonUtils.stringNullCheck(typeStr)
        || CommonUtils.stringNullCheck(advancedStr)) {
      log.info("Diagnosis Report By " + userId + " isn't recommended");
      return null;
    }

    RecommendInfo[] basics = mapper.readValue(basicStr, RecommendInfo[].class);
    RecommendInfo[] types = mapper.readValue(typeStr, RecommendInfo[].class);
    RecommendInfo[] advanceds = mapper.readValue(advancedStr, RecommendInfo[].class);
    return Stream.of(basics, types, advanceds).flatMap(Arrays::stream).filter(e -> e.getType().equals("video"))
        .map(e -> e.getId()).collect(Collectors.toList());
  }

  public ListDTO.Video getBookmarkVideoList(String userId, Long curriculumId, SortType sort, String keyword) {
    List<VideoJoin> videos = videoRePositorySupport.findBookmarkVideosByUserAndCurriculum(userId, curriculumId, sort,
        keyword);
    return convertVideoJoinToDTO(videos);
  }

  @Transactional
  public SuccessDTO insertBookmarkVideo(String userId, String videoId) {
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
  public SuccessDTO deleteBookmarkVideo(String userId, String videoId) {
    VideoBookmarkId videoBookmarkId = new VideoBookmarkId(userId, videoId);
    videoBookmarkRepository.delete(videoBookmarkRepository.findById(videoBookmarkId).orElseThrow(
        () -> new ContentsException(ErrorCode.DB_ERROR, "VideoBookmark doesn't exist in VideoBookmark Table")));

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateVideoHit(String videoId) {
    if (videoHitRepositorySupport.notExistsById(videoId))
      throw new ContentsException(ErrorCode.DB_ERROR, "VideoId doesn't exist in VideoHit Table");
    videoHitRepositorySupport.updateVideoHit(videoId);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateVideoHit(String userId, String videoId) throws ParseException {
    if (videoHitRepositorySupport.notExistsById(videoId))
      throw new ContentsException(ErrorCode.DB_ERROR, "VideoId doesn't exist in VideoHit Table");
    videoHitRepositorySupport.updateVideoHit(videoId);

    // lrsService.init("/SaveStatement");
    // lrsService.saveStatement(lrsService.makeStatement(userId,
    // Long.toString(videoId), LRSService.ACTION_TYPE.enter,
    // LRSService.SOURCE_TYPE.video));
    lrsapiManager.saveStatementList(
        Arrays.asList(lrsUtils.makeStatement(userId, videoId, LRSUtils.ACTION_TYPE.enter, LRSUtils.SOURCE_TYPE.video)));
    return new SuccessDTO(true);
  }

  public SuccessDTO quitVideo(String userId, String videoId, Integer duration) throws ParseException {
    lrsapiManager.saveStatementList(Arrays.asList(
        lrsUtils.makeStatement(userId, videoId, LRSUtils.ACTION_TYPE.quit, LRSUtils.SOURCE_TYPE.video, duration)));
    return new SuccessDTO(true);
  }

  public VideoDTO convertVideoToDTO(Video video) {
    return VideoDTO.builder().videoId(video.getVideoId()).videoSrc(video.getVideoSrc()).title(video.getTitle())
        .imgSrc(video.getImgSrc()).subject(video.getVideoCurriculum().getSubject()).totalTime(video.getTotalTime())
        .startTime(video.getStartTime()).endTime(video.getEndTime()).hit(video.getVideoHit().getHit())
        .createDate(video.getCreateDate().toString())
        .videoType(!CommonUtils.stringNullCheck(video.getVideoSrc()) && video.getVideoSrc().contains(YOUTUBE_TYPE)
            ? VideoType.YOUTUBE.toString()
            : VideoType.SELF.toString())
        .uks(video.getVideoUks().stream().map(videoUks -> videoUks.getUkMaster().getUkName())
            .collect(Collectors.toList()))
        .hashtags(video.getVideoHashtags().stream().map(videoHashtags -> videoHashtags.getHashtag().getName())
            .collect(Collectors.toList()))
        .build();
  }

  public ListDTO.Video convertVideoToDTO(List<Video> videos) {
    return new ListDTO.Video(null, videos.size(),
        videos.stream().map(video -> this.convertVideoToDTO(video)).collect(Collectors.toList()));
  }

  public VideoDTO convertVideoJoinToDTO(VideoJoin videoJoin) {
    Video video = videoJoin.getVideo();
    return VideoDTO.builder().videoId(video.getVideoId()).videoSrc(video.getVideoSrc()).title(video.getTitle())
        .imgSrc(video.getImgSrc()).subject(video.getVideoCurriculum().getSubject()).totalTime(video.getTotalTime())
        .startTime(video.getStartTime()).endTime(video.getEndTime()).hit(video.getVideoHit().getHit())
        .createDate(video.getCreateDate().toString()).bookmark(!CommonUtils.stringNullCheck(videoJoin.getUserUuid()))
        .videoType(!CommonUtils.stringNullCheck(video.getVideoSrc()) && video.getVideoSrc().contains(YOUTUBE_TYPE)
            ? VideoType.YOUTUBE.toString()
            : VideoType.SELF.toString())
        .uks(video.getVideoUks().stream().map(videoUks -> videoUks.getUkMaster().getUkName())
            .collect(Collectors.toList()))
        .hashtags(video.getVideoHashtags().stream().map(videoHashtags -> videoHashtags.getHashtag().getName())
            .collect(Collectors.toList()))
        .build();
  }

  public ListDTO.Video convertVideoJoinToDTO(List<VideoJoin> videoJoins) {
    return new ListDTO.Video(null, videoJoins.size(),
        videoJoins.stream().map(videoJoin -> this.convertVideoJoinToDTO(videoJoin)).collect(Collectors.toList()));
  }

  public ListDTO.Video convertVideoJoinToDTO(List<VideoJoin> videoJoins, Boolean recommended) {
    return new ListDTO.Video(recommended, videoJoins.size(),
        videoJoins.stream().map(videoJoin -> this.convertVideoJoinToDTO(videoJoin)).collect(Collectors.toList()));
  }

  public ListDTO.Curriculum convertVideoCurriculumToDTO(List<VideoCurriculum> curriculums) {
    return new ListDTO.Curriculum(curriculums.size(),
        curriculums.stream()
            .map(curriculum -> new VideoCurriculumDTO(curriculum.getCurriculumId(), curriculum.getSubject()))
            .collect(Collectors.toList()));
  }

}
