package com.tmax.eTest.Contents.service;

import java.util.List;

import com.tmax.eTest.Contents.dto.ArticleJoin;
import com.tmax.eTest.Contents.dto.BookJoin;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SortType;
import com.tmax.eTest.Contents.dto.VideoJoin;
import com.tmax.eTest.Contents.dto.WikiJoin;
import com.tmax.eTest.Contents.repository.support.ArticleRepositorySupport;
import com.tmax.eTest.Contents.repository.support.BookRepositorySupport;
import com.tmax.eTest.Contents.repository.support.VideoRepositorySupport;
import com.tmax.eTest.Contents.repository.support.WikiRepositorySupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentsService {

  @Autowired
  private VideoRepositorySupport videoRepositorySupport;

  @Autowired
  private BookRepositorySupport bookRepositorySupport;

  @Autowired
  private WikiRepositorySupport wikiRepositorySupport;

  @Autowired
  private ArticleRepositorySupport articleRepositorySupport;

  @Autowired
  private VideoService videoService;

  @Autowired
  private BookService bookService;

  @Autowired
  private WikiService wikiService;

  @Autowired
  private ArticleService articleService;

  public ListDTO getBookmarkList(String userId) {
    List<VideoJoin> videos = videoRepositorySupport.findBookmarkVideosByUserAndCurriculum(userId, null, SortType.DATE,
        null);
    List<BookJoin> books = bookRepositorySupport.findBookmarkBooksByUser(userId, null);
    List<WikiJoin> wikis = wikiRepositorySupport.findBookmarkWikisByUser(userId, null);
    List<ArticleJoin> articles = articleRepositorySupport.findBookmarkArticlesByUser(userId, null);
    return convertJoinToDTO(videos, books, wikis, articles);
  }

  public ListDTO convertJoinToDTO(List<VideoJoin> videoJoins, List<BookJoin> bookJoins, List<WikiJoin> wikiJoins,
      List<ArticleJoin> articleJoins) {
    ListDTO.Video video = videoService.convertVideoJoinToDTO(videoJoins);
    ListDTO.Book book = bookService.convertBookJoinToDTO(bookJoins);
    ListDTO.Wiki wiki = wikiService.convertWikiJoinToDTO(wikiJoins);
    ListDTO.Article article = articleService.convertArticleJoinToDTO(articleJoins);

    return ListDTO.builder().video(video).book(book).wiki(wiki).article(article).build();
  }
}
