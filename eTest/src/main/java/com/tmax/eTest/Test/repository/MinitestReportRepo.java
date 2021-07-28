package com.tmax.eTest.Test.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.report.MinitestReport;
import org.springframework.stereotype.Repository;

@Repository("TE-MinitestReportRepo")
public interface MinitestReportRepo extends CrudRepository<MinitestReport, String> {

}
