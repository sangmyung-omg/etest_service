package com.tmax.eTest.Common.repository.stat;

import com.tmax.eTest.Common.model.stat.HitStat;
import com.tmax.eTest.Common.model.stat.HitStatId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HitStatRepository extends JpaRepository<HitStat, HitStatId> {

}
