package com.tmax.eTest.Common.repository.article;

import com.tmax.eTest.Common.model.article.ArticleUkRel;
import com.tmax.eTest.Common.model.article.ArticleUkRelId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleUkRelRepository extends JpaRepository<ArticleUkRel, ArticleUkRelId> {

}
