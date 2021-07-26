package com.tmax.eTest.Test.repository;

import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.report.MinitestReport;

public interface MinitestReportRepo extends CrudRepository<MinitestReport, String> {

}
