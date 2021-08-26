package com.tmax.eTest.Contents.job;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.tmax.eTest.Common.model.stat.HitStat;
import com.tmax.eTest.Common.repository.stat.HitStatRepository;
import com.tmax.eTest.Contents.dto.LRSGetStatementDTO;
import com.tmax.eTest.Contents.dto.LRSStatementDTO;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSService;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
  private LRSService lrsService;

  @Autowired
  private HitStatRepository hitStatRepository;

  @Bean
  public Job statJob() {
    return jobBuilderFactory.get("statJob").start(statStep()).build();
  }

  @Bean
  public Step statStep() {
    return stepBuilderFactory.get("statStep").transactionManager(jpaTransactionManager())
        .tasklet((contribution, chunkContext) -> {

          // Date date = Date.valueOf(LocalDate.now());
          String nowDate = chunkContext.getStepContext().getJobParameters().get("nowDate").toString();
          String tomorrowDate = LocalDate.parse(nowDate, DateTimeFormatter.ISO_DATE).plusDays(1).toString();
          log.info("Job Date: " + nowDate);

          lrsService.init("/StatementList");
          LRSGetStatementDTO lrsGetStatementDTO = lrsService.makeGetStatement(nowDate, tomorrowDate);
          List<LRSStatementDTO> lrsStatementDTOs = lrsService.getStatementList(lrsGetStatementDTO);
          Map<String, Long> counterMap = lrsStatementDTOs.stream()
              .collect(Collectors.groupingBy(e -> e.getSourceType(), Collectors.counting()));
          log.info("StateMent Size : " + counterMap.size());

          Date date = Date.valueOf(nowDate);
          if (!hitStatRepository.existsByStatDate(date))
            hitStatRepository.save(HitStat.builder().statDate(date).build());
          hitStatRepository.updateHit(date, CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.video.name())),
              CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.textbook.name())),
              CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.wiki.name())),
              CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.article.name())));

          log.info("Job Success!");
          return RepeatStatus.FINISHED;
        }).build();
  }

  // @Bean
  // @StepScope
  // public ListItemReader<LRSStatementDTO> statReader() {
  // lrsService.init("/StatementList");
  // LRSGetStatementDTO lrsGetStatementDTO = lrsService.makeGetStatement();
  // List<LRSStatementDTO> lrsStatementDTOs =
  // lrsService.getStatementList(lrsGetStatementDTO);

  // return new ListItemReader<>(lrsStatementDTOs);
  // }

  // public ItemProcessor<LRSStatementDTO, HitStat> statProcessor(Date date) {

  // Map<String, Long> counterMap = userMap.get(userId).stream()
  // .collect(Collectors.groupingBy(e -> e.getSourceType(),
  // Collectors.counting()));
  // Hit hit = new
  // Hit(CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.video.name())),
  // CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.textbook.name())),
  // CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.wiki.name())),
  // CommonUtils.zeroIfNull(counterMap.get(LRSService.SOURCE_TYPE.article.name())));

  // StatDTO stat =
  // StatDTO.builder().userId(userId).hit(hit).bookMark(bookMark).build();
  // stats.add(stat);
  // }

  // }

  // public ItemWriter<HitStat> statWriter(Date date) {
  // return ((List<? extends HitStat> hitStats) -> {
  // if(!hitStatRepository.existsByStatDate(date))
  // hitStatRepository.save(HitStat.builder().statDate(date).build());
  // for(HitStat hitStat:hitStats)
  // hitStatRepository.updateHit(date, hitStat.get, bookHit, wikiHit, articleHit);
  // }
  // }
}
