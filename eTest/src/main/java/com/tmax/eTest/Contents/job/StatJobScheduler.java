package com.tmax.eTest.Contents.job;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
public class StatJobScheduler {

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private StatJobConfiguration statJobConfiguration;

  @Scheduled(cron = "0 59 * * * *")
  public void runJob() {

    Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
    confMap.put("now", new JobParameter(LocalDate.now().toString()));
    confMap.put("time", new JobParameter(System.currentTimeMillis()));
    JobParameters jobParameters = new JobParameters(confMap);
    try {
      jobLauncher.run(statJobConfiguration.statJob(), jobParameters);
    } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException
        | org.springframework.batch.core.repository.JobRestartException e) {
      log.error(e.getMessage());
    }
  }
}
