package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.wiki.Wiki;
import com.tmax.eTest.Common.model.wiki.WikiBookmark;
import com.tmax.eTest.Common.model.wiki.WikiBookmarkId;
import com.tmax.eTest.Common.repository.wiki.WikiBookmarkRepository;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.dto.WikiDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.WikiRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WikiService {
  @Autowired
  private WikiBookmarkRepository wikiBookmarkRepository;

  @Autowired
  private WikiRepositorySupport wikiRepositorySupport;

  public ListDTO.Wiki getWikiList(String userId, String keyword) {
    List<Wiki> wikis = wikiRepositorySupport.findWikisByUser(userId, keyword);
    return convertWikiToDTO(wikis);
  }

  public ListDTO.Wiki getBookmarkWikiList(String userId, String keyword) {
    List<Wiki> wikis = wikiRepositorySupport.findBookmarkWikisByUser(userId, keyword);
    return convertWikiToDTO(wikis);
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

  public ListDTO.Wiki convertWikiToDTO(List<Wiki> wikis) {
    return new ListDTO.Wiki(wikis.size(),
        wikis.stream()
            .map(wiki -> new WikiDTO(wiki.getWikiId(), wiki.getTitle(), wiki.getCreateDate().toString(),
                wiki.getCreatorId(), wiki.getDescription(), wiki.getSummary(), wiki.getSource(),
                !CommonUtils.objectNullcheck(wiki.getWikiBookmarks()), wiki.getWikiUks().stream()
                    .map(wikiUks -> wikiUks.getUkMaster().getUkName()).collect(Collectors.toList())))
            .collect(Collectors.toList()));
  }
}
