package com.tmax.eTest.Common.repository.report;

import com.tmax.eTest.Common.model.report.ReportCache;
import com.tmax.eTest.Common.model.report.ReportCacheKey;

import org.springframework.data.repository.CrudRepository;

public interface ReportCacheRepo extends CrudRepository<ReportCache, ReportCacheKey> {
    
}