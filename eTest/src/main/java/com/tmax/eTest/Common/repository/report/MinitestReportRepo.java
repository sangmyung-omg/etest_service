package com.tmax.eTest.Common.repository.report;

import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.report.MinitestReportKey;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MinitestReportRepo extends CrudRepository<MinitestReport, MinitestReportKey>{
    @Query("Select m from MinitestReport m where minitestId=:minitestID and userUuid=:userUUID")
    Optional<MinitestReport> getReport(@Param("minitestID") String minitestID, @Param("userUUID") String userUUID);
}
