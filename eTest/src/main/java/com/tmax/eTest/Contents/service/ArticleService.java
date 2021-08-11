package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.tmax.eTest.Common.model.article.Article;
import com.tmax.eTest.Common.model.article.ArticleBookmark;
import com.tmax.eTest.Common.model.article.ArticleBookmarkId;
import com.tmax.eTest.Common.repository.article.ArticleBookmarkRepository;
import com.tmax.eTest.Contents.dto.ArticleDTO;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.ArticleRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

  @Autowired
  private ArticleBookmarkRepository articleBookmarkRepository;

  @Autowired
  private ArticleRepositorySupport articleRepositorySupport;

  public ListDTO.Article getArticleList(String userId, String keyword) {
    List<Article> articles = articleRepositorySupport.findArticlesByUser(userId, keyword);
    return convertArticleToDTO(articles);
  }

  public ListDTO.Article getBookmarkArticleList(String userId, String keyword) {
    List<Article> articles = articleRepositorySupport.findBookmarkArticlesByUser(userId, keyword);
    return convertArticleToDTO(articles);
  }

  @Transactional
  public SuccessDTO insertBookmarkArticle(String userId, Long articleId) {
    ArticleBookmarkId articleBookmarkId = new ArticleBookmarkId(userId, articleId);
    ArticleBookmark articleBookmark = new ArticleBookmark(userId, articleId);
    if (articleBookmarkRepository.existsById(articleBookmarkId))
      throw new ContentsException(ErrorCode.DB_ERROR, "ArticleBookmark already exists in ArticleBookmark Table");
    else
      articleBookmarkRepository.save(articleBookmark);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO deleteBookmarkArticle(String userId, Long articleId) {
    ArticleBookmarkId articleBookmarkId = new ArticleBookmarkId(userId, articleId);
    articleBookmarkRepository.delete(articleBookmarkRepository.findById(articleBookmarkId).orElseThrow(
        () -> new ContentsException(ErrorCode.DB_ERROR, "ArticleBookmark already exists in ArticleBookmark Table")));

    return new SuccessDTO(true);
  }

  public ListDTO.Article convertArticleToDTO(List<Article> articles) {
    return new ListDTO.Article(articles.size(), articles.stream()
        .map(article -> new ArticleDTO(article.getArticleId(), article.getArticleUrl(), article.getTitle(),
            article.getCreateDate().toString(), article.getCreatorId(), article.getDescription(), article.getImgSrc(),
            article.getSource(), !CommonUtils.objectNullcheck(article.getArticleBookmarks()), article.getArticleUks()
                .stream().map(articleUks -> articleUks.getUkMaster().getUkName()).collect(Collectors.toList())))
        .collect(Collectors.toList()));
  }
}
