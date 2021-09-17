package com.tmax.eTest.Contents.job;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.tmax.eTest.Common.model.stat.HitStat;
import com.tmax.eTest.Common.repository.stat.HitStatRepository;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSUtils;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
public class StatJobConfiguration extends DefaultBatchConfigurer {

  // For Spring batch job to apply Tibero
  ////////////////////////////////////////////////////////////////
  @Autowired
  private DataSource dataSource;

  @Override
  protected JobRepository createJobRepository() throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setDatabaseType("ORACLE");
    factory.setTransactionManager(jpaTransactionManager());
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  @Primary
  public JpaTransactionManager jpaTransactionManager() {
    final JpaTransactionManager tm = new JpaTransactionManager();
    tm.setDataSource(dataSource);
    return tm;
  }
  ////////////////////////////////////////////////////////////////

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Autowired
  private LRSUtils lrsUtils;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private LRSAPIManager lrsapiManager;

  @Autowired
  private HitStatRepository hitStatRepository;

  @Bean
  public Job statJob() {
    return jobBuilderFactory.get("statJob").incrementer(new RunIdIncrementer()).start(statStep()).build();
  }

  @Bean
  public Step statStep() {
    return stepBuilderFactory.get("statStep").transactionManager(jpaTransactionManager())
        .tasklet((contribution, chunkContext) -> {

          LocalDate now = LocalDate.parse(chunkContext.getStepContext().getJobParameters().get("now").toString());
          Timestamp nowDate = Timestamp.valueOf(now.atStartOfDay());
          Timestamp tomorrowDate = Timestamp.valueOf(now.plusDays(1).atStartOfDay());
          log.info("Job Date: " + nowDate);

          // lrsService.init("/StatementList");
          // LRSGetStatementDTO lrsGetStatementDTO = lrsService.makeGetStatement(nowDate,
          // tomorrowDate);
          // List<LRSStatementDTO> lrsStatementDTOs =
          // lrsService.getStatementList(lrsGetStatementDTO);

          GetStatementInfoDTO getStatementInfoDTO = lrsUtils.makeGetStatement(nowDate, tomorrowDate);
          List<StatementDTO> statementDTOs = lrsapiManager.getStatementList(getStatementInfoDTO);
          Map<String, Long> counterMap = statementDTOs.stream()
              .filter(e -> e.getActionType().equals(LRSUtils.ACTION_TYPE.enter.name()))
              .collect(Collectors.groupingBy(e -> e.getSourceType(), Collectors.counting()));
          log.info("StateMent Size : " + counterMap.size());

          Date date = new Date(nowDate.getTime());
          if (!hitStatRepository.existsByStatDate(date))
            hitStatRepository.save(HitStat.builder().statDate(date).build());

          hitStatRepository.updateHit(date, commonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.video.name())),
              commonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.textbook.name())),
              commonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.wiki.name())),
              commonUtils.zeroIfNull(counterMap.get(LRSUtils.SOURCE_TYPE.article.name())));

          log.info("Job Success!");
          return RepeatStatus.FINISHED;
        }).build();
  }
}
