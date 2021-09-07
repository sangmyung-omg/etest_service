package com.tmax.eTest.MyPage.repository;


import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.MyPage.dto.MiniTestReportHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MY-MinitestReportRepo")
public interface MinitestReportRepo extends JpaRepository<MinitestReport, Long> {
    @Query(value = "SELECT * FROM Minitest_Report WHERE USER_UUID = :USER_UUID order by minitest_date asc", nativeQuery = true)
    List<MinitestReport> findAllByUserUuid(@Param("USER_UUID") String userUuid);

}


