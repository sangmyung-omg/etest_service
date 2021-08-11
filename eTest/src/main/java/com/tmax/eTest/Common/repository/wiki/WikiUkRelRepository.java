package com.tmax.eTest.Common.repository.wiki;

import com.tmax.eTest.Common.model.wiki.WikiUkRel;
import com.tmax.eTest.Common.model.wiki.WikiUkRelId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiUkRelRepository extends JpaRepository<WikiUkRel, WikiUkRelId> {

}
