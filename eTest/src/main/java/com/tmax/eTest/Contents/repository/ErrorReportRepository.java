package com.tmax.eTest.Contents.repository;

import com.tmax.eTest.Common.model.error_report.ErrorReport;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CO-ErrorReportRepository")

public interface ErrorReportRepository extends JpaRepository<ErrorReport, Long>{

}
