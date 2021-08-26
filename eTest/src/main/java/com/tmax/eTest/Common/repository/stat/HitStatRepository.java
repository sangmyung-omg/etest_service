package com.tmax.eTest.Common.repository.stat;

import java.sql.Date;

import com.tmax.eTest.Common.model.stat.HitStat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HitStatRepository extends JpaRepository<HitStat, Long> {

  @Modifying
  @Query("update HitStat hs set hs.videoHit = :videoHit, hs.bookHit = :bookHit, hs.wikiHit = :wikiHit, hs.articleHit = :articleHit where hs.statDate = :statDate")
  Integer updateHit(@Param("statDate") Date statDate, @Param("videoHit") Long videoHit, @Param("bookHit") Long bookHit,
      @Param("wikiHit") Long wikiHit, @Param("articleHit") Long articleHit);

  Boolean existsByStatDate(Date date);
}
