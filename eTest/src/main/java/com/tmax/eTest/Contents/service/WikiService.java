package com.tmax.eTest.Contents.service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.wiki.Wiki;
import com.tmax.eTest.Common.model.wiki.WikiBookmark;
import com.tmax.eTest.Common.model.wiki.WikiBookmarkId;
import com.tmax.eTest.Common.repository.wiki.WikiBookmarkRepository;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.dto.WikiDTO;
import com.tmax.eTest.Contents.dto.WikiJoin;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.WikiRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSUtils;
import com.tmax.eTest.LRS.util.LRSAPIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WikiService {
  @Autowired
  private WikiBookmarkRepository wikiBookmarkRepository;

  @Autowired
  private WikiRepositorySupport wikiRepositorySupport;

  @Autowired
  private LRSUtils lrsUtils;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private LRSAPIManager lrsapiManager;

  public WikiDTO getWiki(String userId, Long wikiId) {
    WikiJoin wiki = wikiRepositorySupport.findWikiByUserAndId(userId, wikiId);
    return convertWikiJoinToDTO(wiki);
  }

  public ListDTO.Wiki getWikiList(String userId, String keyword) {
    List<WikiJoin> wikis = wikiRepositorySupport.findWikisByUser(userId, keyword);
    return convertWikiJoinToDTO(wikis);
  }

  public ListDTO.Wiki getBookmarkWikiList(String userId, String keyword) {
    List<WikiJoin> wikis = wikiRepositorySupport.findBookmarkWikisByUser(userId, keyword);
    return convertWikiJoinToDTO(wikis);
  }

  @Transactional
  public SuccessDTO insertBookmarkWiki(String userId, Long wikiId) {
    WikiBookmarkId wikiBookmarkId = new WikiBookmarkId(userId, wikiId);
    WikiBookmark wikiBookmark = new WikiBookmark(userId, wikiId);
    if (wikiBookmarkRepository.existsById(wikiBookmarkId))
      throw new ContentsException(ErrorCode.DB_ERROR, "WikiBookmark already exists in WikiBookmark Table");
    else
      wikiBookmarkRepository.save(wikiBookmark);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO deleteBookmarkWiki(String userId, Long wikiId) {
    WikiBookmarkId wikiBookmarkId = new WikiBookmarkId(userId, wikiId);
    wikiBookmarkRepository.delete(wikiBookmarkRepository.findById(wikiBookmarkId).orElseThrow(
        () -> new ContentsException(ErrorCode.DB_ERROR, "WikiBookmark already exists in WikiBookmark Table")));

    return new SuccessDTO(true);
  }

  public SuccessDTO updateWikiHit(String userId, Long wikiId) throws ParseException {
    // lrsService.init("/SaveStatement");
    // lrsService.saveStatement(lrsService.makeStatement(userId,
    // Long.toString(wikiId), LRSService.ACTION_TYPE.enter,
    // LRSService.SOURCE_TYPE.wiki));
    lrsapiManager.saveStatementList(Arrays.asList(
        lrsUtils.makeStatement(userId, Long.toString(wikiId), LRSUtils.ACTION_TYPE.enter, LRSUtils.SOURCE_TYPE.wiki)));
    return new SuccessDTO(true);
  }

  public SuccessDTO quitWiki(String userId, Long wikiId, Integer duration) throws ParseException {
    lrsapiManager.saveStatementList(Arrays.asList(lrsUtils.makeStatement(userId, Long.toString(wikiId),
        LRSUtils.ACTION_TYPE.quit, LRSUtils.SOURCE_TYPE.wiki, duration)));
    return new SuccessDTO(true);
  }

  // public ListDTO.Wiki convertWikiToDTO(List<Wiki> wikis) {
  // return new ListDTO.Wiki(wikis.size(),
  // wikis.stream()
  // .map(wiki -> new WikiDTO(wiki.getWikiId(), wiki.getTitle(),
  // wiki.getCreateDate().toString(),
  // wiki.getCreatorId(), wiki.getDescription(), wiki.getSummary(),
  // wiki.getSource(),
  // !commonUtils.objectNullcheck(wiki.getWikiBookmarks()),
  // wiki.getWikiUks().stream()
  // .map(wikiUks ->
  // wikiUks.getUkMaster().getUkName()).collect(Collectors.toList())))
  // .collect(Collectors.toList()));
  // }

  public WikiDTO convertWikiJoinToDTO(WikiJoin wikiJoin) {
    Wiki wiki = wikiJoin.getWiki();
    return new WikiDTO(wiki.getWikiId(), wiki.getTitle(), wiki.getCreateDate().toString(), wiki.getCreatorId(),
        wiki.getDescription(), wiki.getSummary(), wiki.getSource(),
        !commonUtils.stringNullCheck(wikiJoin.getUserUuid()));
    // wiki.getWikiUks().stream().map(wikiUks ->
    // wikiUks.getUkMaster().getUkName()).collect(Collectors.toList()));
  }

  public ListDTO.Wiki convertWikiJoinToDTO(List<WikiJoin> wikiJoins) {
    return new ListDTO.Wiki(wikiJoins.size(), wikiJoins.stream().map(wikiJoin -> {
      Wiki wiki = wikiJoin.getWiki();
      return new WikiDTO(wiki.getWikiId(), wiki.getTitle(), wiki.getCreateDate().toString(), wiki.getCreatorId(),
          wiki.getDescription(), wiki.getSummary(), wiki.getSource(),
          !commonUtils.stringNullCheck(wikiJoin.getUserUuid()));
      // wiki.getWikiUks().stream().map(wikiUks ->
      // wikiUks.getUkMaster().getUkName()).collect(Collectors.toList()));
    }).collect(Collectors.toList()));
  }
}
