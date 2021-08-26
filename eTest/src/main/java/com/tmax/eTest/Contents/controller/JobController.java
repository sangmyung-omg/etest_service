package com.tmax.eTest.Contents.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class JobController {

  @Autowired
  JobLauncher jobLauncher;

  @Autowired
  Job job;

  @RequestMapping("/launchjob")
  public String handle() throws Exception {

    JobExecution jobExecution = null;
    try {
      Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
      confMap.put("nowDate", new JobParameter(LocalDate.now().toString()));
      confMap.put("time", new JobParameter(System.currentTimeMillis()));
      JobParameters jobParameters = new JobParameters(confMap);
      jobExecution = jobLauncher.run(job, jobParameters);
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    return "jobExecution's info: Id = " + jobExecution.getId() + " ,status = " + jobExecution.getExitStatus();
  }
}
