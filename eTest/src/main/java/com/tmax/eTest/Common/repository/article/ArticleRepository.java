package com.tmax.eTest.Common.repository.article;

import com.tmax.eTest.Common.model.article.Article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{
  
}
