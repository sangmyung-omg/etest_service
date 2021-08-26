package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.stat.HitStat;
import com.tmax.eTest.Common.repository.article.ArticleBookmarkRepository;
import com.tmax.eTest.Common.repository.book.BookBookmarkRepository;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Common.repository.wiki.WikiBookmarkRepository;
import com.tmax.eTest.Contents.dto.DateDTO;
import com.tmax.eTest.Contents.dto.LRSGetStatementDTO;
import com.tmax.eTest.Contents.dto.LRSStatementDTO;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.StatDTO;
import com.tmax.eTest.Contents.dto.StatDTO.BookMark;
import com.tmax.eTest.Contents.dto.StatDTO.Hit;
import com.tmax.eTest.Contents.dto.UserListDTO;
import com.tmax.eTest.Contents.repository.support.HitStatRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatService {

  @Autowired
  private HitStatRepositorySupport hitStatRepositorySupport;

  @Autowired
  private LRSService lrsService;

  @Autowired
  private VideoBookmarkRepository videoBookmarkRepository;

  @Autowired
  private WikiBookmarkRepository wikiBookmarkRepository;

  @Autowired
  private BookBookmarkRepository bookBookmarkRepository;

  @Autowired
  private ArticleBookmarkRepository articleBookmarkRepository;

  public ListDTO.Stat getContentsStatsByDate(DateDTO date) {
    List<HitStat> hitStats = hitStatRepositorySupport.findAllByDate(date);
    return convertStatToDTO(hitStats);
  }

  public ListDTO.Stat getContentsStatsByUsers(UserListDTO users) {
    lrsService.init("/StatementList");
    List<String> userIdList = users.getUserIds();
    LRSGetStatementDTO lrsGetStatementDTO = lrsService.makeGetStatement(userIdList);

    List<LRSStatementDTO> lrsStatementDTOs = lrsService.getStatementList(lrsGetStatementDTO);
    List<StatDTO> stats = new ArrayList<StatDTO>();
    ListDTO.Stat result = new ListDTO.Stat(userIdList.size(), stats);

    Map<String, List<LRSStatementDTO>> userMap = lrsStatementDTOs.stream()
        .collect(Collectors.groupingBy(e -> e.getUserId(), Collectors.toList()));
    for (String userId : userIdList) {
      BookMark bookMark = new BookMark(videoBookmarkRepository.countByUserUuid(userId),
          bookBookmarkRepository.countByUserUuid(userId), wikiBookmarkRepository.countByUserUuid(userId),
          articleBookmarkRepository.countByUserUuid(userId));
      Map<String, Long> counterMap = userMap.get(userId).stream()
          .collect(Collectors.groupingBy(e -> e.getSourceType(), Collectors.counting()));
      Hit hit = new Hit(CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.video.name())),
          CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.textbook.name())),
          CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.wiki.name())),
          CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.article.name())));

      StatDTO stat = StatDTO.builder().userId(userId).hit(hit).bookMark(bookMark).build();
      stats.add(stat);
    }

    return result;
  }

  public ListDTO.Stat convertStatToDTO(List<HitStat> hitStats) {
    return new ListDTO.Stat(hitStats.size(),
        hitStats.stream()
            .map(hitStat -> new StatDTO(hitStat.getStatDate(),
                new Hit(hitStat.getVideoHit(), hitStat.getBookHit(), hitStat.getWikiHit(), hitStat.getArticleHit())))
            .collect(Collectors.toList()));
  }

  // public ListDTO.Stat convertStatToDTO(List<HitStat> hitStats, List<Integer>
  // videoHits, ) {
  // return new ListDTO.Stat(hitStats.size(),
  // hitStats.stream()
  // .map(hitStat -> new StatDTO(hitStat.getStatDate(),
  // new Hit(sum(hitStat.getVideoHit(), hitStat.getBookHit(),
  // hitStat.getWikiHit(), hitStat.getArticleHit()),
  // hitStat.getVideoHit(), hitStat.getBookHit(), hitStat.getWikiHit(),
  // hitStat.getArticleHit())))
  // .collect(Collectors.toList()));
  // }

}
