package com.tmax.eTest.Common.repository.wiki;

import com.tmax.eTest.Common.model.wiki.WikiBookmark;
import com.tmax.eTest.Common.model.wiki.WikiBookmarkId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiBookmarkRepository extends JpaRepository<WikiBookmark, WikiBookmarkId> {

}
