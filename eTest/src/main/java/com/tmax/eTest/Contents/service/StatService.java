package com.tmax.eTest.Contents.service;

import java.text.ParseException;
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
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.StatDTO;
import com.tmax.eTest.Contents.dto.StatDTO.BookMark;
import com.tmax.eTest.Contents.dto.StatDTO.Hit;
import com.tmax.eTest.Contents.dto.UserListDTO;
import com.tmax.eTest.Contents.repository.support.HitStatRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSUtils;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatService {

  @Autowired
  private HitStatRepositorySupport hitStatRepositorySupport;

  // @Autowired
  // private LRSService lrsService;

  @Autowired
  private LRSUtils lrsUtils;

  @Autowired
  private LRSAPIManager lRSAPIManager;

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

  public ListDTO.Stat getContentsStatsByUsers(UserListDTO users) throws ParseException {
    List<String> userIdList = users.getUserIds();

    // lrsService.init("/StatementList");
    // LRSGetStatementDTO lrsGetStatementDTO =
    // lrsService.makeGetStatement(userIdList);
    // List<LRSStatementDTO> lrsStatementDTOs =
    // lrsService.getStatementList(lrsGetStatementDTO);

    GetStatementInfoDTO lrsGetStatementDTO = lrsUtils.makeGetStatement(userIdList);
    List<StatementDTO> lrsStatementDTOs = lRSAPIManager.getStatementList(lrsGetStatementDTO);

    List<StatDTO> stats = new ArrayList<StatDTO>();
    ListDTO.Stat result = new ListDTO.Stat(userIdList.size(), stats);

    Map<String, List<StatementDTO>> userMap = lrsStatementDTOs.stream()
        .filter(e -> e.getActionType().equals(LRSUtils.ACTION_TYPE.enter.name()))
        .collect(Collectors.groupingBy(e -> e.getUserId(), Collectors.toList()));
    for (String userId : userIdList) {
      BookMark bookMark = new BookMark(videoBookmarkRepository.countByUserUuid(userId),
          bookBookmarkRepository.countByUserUuid(userId), wikiBookmarkRepository.countByUserUuid(userId),
          articleBookmarkRepository.countByUserUuid(userId));
      Hit hit = new Hit();
      if (userMap.containsKey(userId)) {
        Map<String, Long> counterMap = userMap.get(userId).stream()
            .collect(Collectors.groupingBy(e -> e.getSourceType(), Collectors.counting()));
        hit = new Hit(CommonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.video.name())),
            CommonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.textbook.name())),
            CommonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.wiki.name())),
            CommonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.article.name())));
      }

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
}
