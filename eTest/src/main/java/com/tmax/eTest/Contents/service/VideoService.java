package com.tmax.eTest.Contents.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.eTest.Common.model.meta.MetaCodeMaster;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.model.video.VideoCurriculum;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Contents.dto.CodeSet;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.RecommendInfo;
import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.dto.VideoCurriculumDTO;
import com.tmax.eTest.Contents.dto.VideoDTO;
import com.tmax.eTest.Contents.dto.VideoJoin;
import com.tmax.eTest.Contents.dto.WatchVideo;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.DiagnosisReportRepositorySupport;
import com.tmax.eTest.Contents.repository.support.MetaCodeMasterRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoCurriculumRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoHitRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSUtils;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Test.util.UkVersionManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoService {

  public enum VideoType {
    YOUTUBE, SELF, VIDEO, ARTICLE
  }

  private final String YOUTUBE_TYPE = "youtu";

  private VideoCurriculumRepositorySupport videoCurriculumRepositorySupport;

  private VideoHitRepositorySupport videoHitRepositorySupport;

  private VideoRepositorySupport videoRePositorySupport;

  private VideoBookmarkRepository videoBookmarkRepository;

  private MetaCodeMasterRepositorySupport metaCodeMasterRepositorySupport;

  private DiagnosisReportRepositorySupport diagnosisReportRepositorySupport;

  private LRSUtils lrsUtils;

  private LRSAPIManager lrsapiManager;

  private CommonUtils commonUtils;

  private UkVersionManager ukVersionManager;

  public VideoService(VideoCurriculumRepositorySupport videoCurriculumRepositorySupport,
      VideoHitRepositorySupport videoHitRepositorySupport, VideoRepositorySupport videoRePositorySupport,
      VideoBookmarkRepository videoBookmarkRepository, MetaCodeMasterRepositorySupport metaCodeMasterRepositorySupport,
      DiagnosisReportRepositorySupport diagnosisReportRepositorySupport, LRSUtils lrsUtils, LRSAPIManager lrsapiManager,
      CommonUtils commonUtils, UkVersionManager ukVersionManager) {
    this.videoCurriculumRepositorySupport = videoCurriculumRepositorySupport;
    this.videoHitRepositorySupport = videoHitRepositorySupport;
    this.videoRePositorySupport = videoRePositorySupport;
    this.videoBookmarkRepository = videoBookmarkRepository;
    this.metaCodeMasterRepositorySupport = metaCodeMasterRepositorySupport;
    this.diagnosisReportRepositorySupport = diagnosisReportRepositorySupport;
    this.lrsUtils = lrsUtils;
    this.lrsapiManager = lrsapiManager;
    this.commonUtils = commonUtils;
    this.ukVersionManager = ukVersionManager;
  }

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

  public List<ListDTO.Video> getWatchVideoList(String userId) {
    GetStatementInfoDTO lrsGetStatementDTO = lrsUtils.makeGetStatement(userId);
    List<WatchVideo> watchVideos = getLrsVideos(userId, lrsGetStatementDTO);
    if (commonUtils.objectNullcheck(watchVideos))
      return new ArrayList<>();
    return convertWatchVideoToDTO(watchVideos);
  }

  private List<WatchVideo> getLrsVideos(String userId, GetStatementInfoDTO lrsGetStatementDTO) {
    List<String> videoIds;
    List<StatementDTO> lrsStatementDTOs = null;
    try {
      lrsStatementDTOs = lrsapiManager.getStatementList(lrsGetStatementDTO);
      videoIds = lrsStatementDTOs.stream().map(lrsStatementDTO -> lrsStatementDTO.getSourceId())
          .collect(Collectors.toMap(key -> key, value -> value, (oldValue, newValue) -> newValue,
              () -> new LinkedHashMap<>(16, 0.75f, true)))
          .values().stream().distinct().sorted((a, b) -> -1).collect(Collectors.toList());
      if (videoIds.size() == 0)
        return null;
    } catch (ParseException e) {
      throw new ContentsException(ErrorCode.LRS_ERROR);
    }
    List<VideoJoin> videos = videoRePositorySupport.findVideosByUserAndIds(userId, videoIds);
    Map<String, List<VideoJoin>> videoMap = new LinkedHashMap<String, List<VideoJoin>>();
    Collections.reverse(lrsStatementDTOs);
    for (VideoJoin video : videos) {
      String videoId = video.getVideo().getVideoId();
      for (StatementDTO lrsStatement : lrsStatementDTOs) {
        if (lrsStatement.getSourceId().equals(videoId)) {
          String watchDate = lrsStatement.getStatementDate().toLocalDateTime().toLocalDate()
              .format(DateTimeFormatter.ISO_LOCAL_DATE);
          if (videoMap.containsKey(watchDate))
            videoMap.get(watchDate).add(video);
          else
            videoMap.put(watchDate, new ArrayList<>(Arrays.asList(video)));
          break;
        }
      }
    }

    List<WatchVideo> watchVideos = videoMap.entrySet().stream().map(e -> new WatchVideo(e.getKey(), e.getValue()))
        .collect(Collectors.toList());

    return watchVideos;
  }

  @Transactional
  public SuccessDTO deleteWatchVideo(String userId, String videoId) {
    Video video = videoRePositorySupport.findVideoById(videoId);
    if (video.getType().equals(VideoType.VIDEO.name()))
      lrsapiManager.disableVideoStatement(userId, videoId);
    else if (video.getType().equals(VideoType.ARTICLE.name()))
      lrsapiManager.disableArticleStatement(userId, videoId);
    else
      throw new ContentsException(ErrorCode.TYPE_ERROR);
    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO deleteWatchVideoList(String userId) {
    lrsapiManager.disableVideoStatement(userId, null);
    lrsapiManager.disableArticleStatement(userId, null);
    return new SuccessDTO(true);
  }

  public ListDTO.Video getRecommendVideoList(String userId, Long curriculumId, String keyword) throws IOException {
    DiagnosisReport diagnosisReport = diagnosisReportRepositorySupport.findDiagnosisReportByUser(userId);
    List<String> recommendVideos = getDiagnosisRecommendVideoList(diagnosisReport);
    Boolean recommended = !commonUtils.objectNullcheck(recommendVideos);
    if (!recommended)
      return new ListDTO.Video(recommended);

    List<VideoJoin> videos = videoRePositorySupport.findVideosByUserAndCurriculum(userId, curriculumId,
        SortType.RECOMMEND, keyword);
    return convertVideoJoinToDTO(filtering(videos, recommendVideos), recommended, diagnosisReport.getDiagnosisDate(),
        diagnosisReport.getRiskScore(), diagnosisReport.getInvestScore(), diagnosisReport.getKnowledgeScore());
  }

  private List<VideoJoin> filtering(List<VideoJoin> videoJoins, List<String> recommends) {
    Map<Boolean, List<VideoJoin>> videoMap = videoJoins.stream()
        .collect(Collectors.partitioningBy(e -> recommends.contains(e.getVideo().getVideoId())));
    return Stream.concat(videoMap.get(true).stream(), videoMap.get(false).stream()).collect(Collectors.toList());
  }

  private List<String> getDiagnosisRecommendVideoList(DiagnosisReport diagnosisReport) throws IOException {
    if (commonUtils.objectNullcheck(diagnosisReport)) {
      log.info("Diagnosis Report doesn't contain user");
      return null;
    }
    String basicStr = diagnosisReport.getRecommendBasicList();
    String typeStr = diagnosisReport.getRecommendTypeList();
    String advancedStr = diagnosisReport.getRecommendAdvancedList();
    ObjectMapper mapper = new ObjectMapper();
    if (commonUtils.stringNullCheck(basicStr) || commonUtils.stringNullCheck(typeStr)
        || commonUtils.stringNullCheck(advancedStr)) {
      log.info("Diagnosis Report not recommended");
      return null;
    }

    RecommendInfo[] basics = mapper.readValue(basicStr, RecommendInfo[].class);
    RecommendInfo[] types = mapper.readValue(typeStr, RecommendInfo[].class);
    RecommendInfo[] advanceds = mapper.readValue(advancedStr, RecommendInfo[].class);
    return Stream.of(basics, types, advanceds).flatMap(Arrays::stream)
        .filter(e -> e.getType().equals(VideoType.VIDEO.name())).map(e -> e.getId()).collect(Collectors.toList());
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
  public SuccessDTO updateVideoHit(String videoId, VideoType videoType) {
    if (videoHitRepositorySupport.notExistsById(videoId))
      throw new ContentsException(ErrorCode.DB_ERROR, "VideoId doesn't exist in VideoHit Table");
    videoHitRepositorySupport.updateVideoHit(videoId);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateVideoHit(String userId, String videoId, VideoType videoType) throws ParseException {
    // if (videoHitRepositorySupport.notExistsById(videoId))
    // throw new ContentsException(ErrorCode.DB_ERROR, "VideoId doesn't exist in
    // VideoHit Table");
    // videoHitRepositorySupport.updateVideoHit(videoId);

    // lrsService.init("/SaveStatement");
    // lrsService.saveStatement(lrsService.makeStatement(userId,
    // Long.toString(videoId), LRSService.ACTION_TYPE.enter,
    // LRSService.SOURCE_TYPE.video));
    lrsapiManager.saveStatementList(Arrays.asList(lrsUtils.makeStatement(userId, videoId, LRSUtils.ACTION_TYPE.enter,
        videoType.equals(VideoType.ARTICLE) ? LRSUtils.SOURCE_TYPE.article : LRSUtils.SOURCE_TYPE.video)));
    return updateVideoHit(videoId, videoType);
  }

  public SuccessDTO quitVideo(String videoId, Integer duration) {
    // lrsapiManager.saveStatementList(Arrays.asList(
    // lrsUtils.makeStatement(userId, videoId, LRSUtils.ACTION_TYPE.quit,
    // LRSUtils.SOURCE_TYPE.video, duration)));
    return new SuccessDTO(true);
  }

  public SuccessDTO quitVideo(String userId, String videoId, Integer duration) throws ParseException {
    lrsapiManager.saveStatementList(Arrays.asList(
        lrsUtils.makeStatement(userId, videoId, LRSUtils.ACTION_TYPE.quit, LRSUtils.SOURCE_TYPE.video, duration)));
    return quitVideo(videoId, duration);
  }

  public VideoDTO convertVideoToDTO(Video video) {
    String source = getSource(video.getCodeSet());
    return convertVideoToDTO(video, source);
  }

  public VideoDTO convertVideoToDTO(Video video, String source) {
    Long ukVersionId = ukVersionManager.getCurrentUkVersionId();
    return VideoDTO.builder().videoId(video.getVideoId()).videoSrc(video.getVideoSrc()).title(video.getTitle())
        .imgSrc(video.getImgSrc()).subject(video.getVideoCurriculum().getSubject()).totalTime(video.getTotalTime())
        .startTime(video.getStartTime()).endTime(video.getEndTime()).hit(video.getVideoHit().getHit())
        .createDate(video.getCreateDate().toString()).registerDate(video.getRegisterDate().toString())
        .videoType(video.getType().equals(VideoType.ARTICLE.name()) ? video.getType()
            : !commonUtils.stringNullCheck(video.getVideoSrc()) && video.getVideoSrc().contains(YOUTUBE_TYPE)
                ? VideoType.YOUTUBE.toString()
                : VideoType.SELF.toString())
        .source(source).description(video.getDescription())
        .uks(video.getVideoUks().stream()
            .map(videoUks -> videoUks.getUkMaster().getUkVersion().stream()
                .filter(ukVersion -> ukVersion.getVersionId().equals(ukVersionId)).findAny()
                .orElseThrow(() -> new ContentsException(ErrorCode.DB_ERROR, ukVersionId + ": UK Version not exist!"))
                .getUkName())
            .collect(Collectors.toList()))
        .hashtags(video.getVideoHashtags().stream().map(videoHashtags -> videoHashtags.getHashtag().getName())
            .collect(Collectors.toList()))
        .build();
  }

  public ListDTO.Video convertVideoToDTO(List<Video> videos) {
    Map<String, String> sourceMap = getSources(
        videos.stream().map(video -> video.getCodeSet()).collect(Collectors.toList()));
    return new ListDTO.Video(videos.size(), videos.stream()
        .map(video -> this.convertVideoToDTO(video, sourceMap.get(video.getCodeSet()))).collect(Collectors.toList()));
  }

  public VideoDTO convertVideoJoinToDTO(VideoJoin videoJoin) {
    Video video = videoJoin.getVideo();
    String source = getSource(video.getCodeSet());
    VideoDTO videoDTO = convertVideoToDTO(video, source);
    videoDTO.setBookmark(!commonUtils.stringNullCheck(videoJoin.getUserUuid()));
    return videoDTO;
  }

  public VideoDTO convertVideoJoinToDTO(VideoJoin videoJoin, String source) {
    Video video = videoJoin.getVideo();
    VideoDTO videoDTO = convertVideoToDTO(video, source);
    videoDTO.setBookmark(!commonUtils.stringNullCheck(videoJoin.getUserUuid()));
    return videoDTO;
  }

  public ListDTO.Video convertVideoJoinToDTO(List<VideoJoin> videoJoins) {
    Map<String, String> sourceMap = getSources(
        videoJoins.stream().map(videoJoin -> videoJoin.getVideo().getCodeSet()).collect(Collectors.toList()));
    return new ListDTO.Video(videoJoins.size(),
        videoJoins.stream()
            .map(videoJoin -> this.convertVideoJoinToDTO(videoJoin, sourceMap.get(videoJoin.getVideo().getCodeSet())))
            .collect(Collectors.toList()));
  }

  public ListDTO.Video convertVideoJoinToDTO(List<VideoJoin> videoJoins, Boolean recommended, Timestamp diagnosisDate,
      Integer riskScore, Integer investScore, Integer knowledgeScore) {
    ListDTO.Video videoDTO = convertVideoJoinToDTO(videoJoins);
    videoDTO.setRecommended(recommended);
    videoDTO.setRecommendDate(diagnosisDate.toLocalDateTime().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    videoDTO.setRiskScore(riskScore);
    videoDTO.setInvestScore(investScore);
    videoDTO.setKnowledgeScore(knowledgeScore);
    return videoDTO;
  }

  public List<ListDTO.Video> convertWatchVideoToDTO(List<WatchVideo> watchVideos) {
    List<ListDTO.Video> watchVideoDTOs = new ArrayList<>();
    for (WatchVideo watchVideo : watchVideos) {
      ListDTO.Video watchVideoDTO = convertVideoJoinToDTO(watchVideo.getVideoJoins());
      watchVideoDTO.setWatchDate(watchVideo.getWatchDate());
      watchVideoDTOs.add(watchVideoDTO);
    }
    return watchVideoDTOs;
  }

  public ListDTO.Curriculum convertVideoCurriculumToDTO(List<VideoCurriculum> curriculums) {
    return new ListDTO.Curriculum(curriculums.size(),
        curriculums.stream()
            .map(curriculum -> new VideoCurriculumDTO(curriculum.getCurriculumId(), curriculum.getSubject()))
            .collect(Collectors.toList()));
  }

  private String getSourceId(String codeSetStr) {
    final String PREFIX_SOURCE_NAME = "????????? ????????????";

    if (commonUtils.stringNullCheck(codeSetStr)) {
      log.info("Meta Code Null Error");
      return null;
    }

    ObjectMapper mapper = new ObjectMapper();
    CodeSet codeSet = null;
    try {
      codeSet = mapper.readValue(codeSetStr, CodeSet.class);
    } catch (IOException e) {
      log.info(codeSetStr + ": Meta Code Json Error ");
      return null;
    }
    String sourceId = null;
    for (String codeId : codeSet.getCodes()) {
      if (codeId.contains(PREFIX_SOURCE_NAME)) {
        sourceId = codeId;
        break;
      }
    }
    return sourceId;
  }

  private String getSource(String codeSetStr) {
    String sourceId = getSourceId(codeSetStr);
    MetaCodeMaster metaCodeMaster = metaCodeMasterRepositorySupport.findMetaCodeById(sourceId);
    String sourceName = metaCodeMaster.getCodeName();

    return sourceName;
  }

  private Map<String, String> getSources(List<String> codeSetStrs) {
    HashMap<String, String> sourceMap = new HashMap<String, String>();
    ArrayList<String> sourceIds = new ArrayList<String>();
    for (String codeSetStr : codeSetStrs) {
      String sourceId = getSourceId(codeSetStr);
      sourceIds.add(sourceId);
    }

    Map<String, String> metaCodeMap = metaCodeMasterRepositorySupport.findMetaCodeMapByIds(sourceIds);
    for (int i = 0; i < codeSetStrs.size(); i++)
      sourceMap.put(codeSetStrs.get(i), metaCodeMap.get(sourceIds.get(i)));
    return sourceMap;
  }
}
