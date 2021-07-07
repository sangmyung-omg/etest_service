package com.tmax.eTest.Contents.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Contents.model.ErrorReport;

public interface ErrorReportRepository extends JpaRepository<ErrorReport, Long>{

}
