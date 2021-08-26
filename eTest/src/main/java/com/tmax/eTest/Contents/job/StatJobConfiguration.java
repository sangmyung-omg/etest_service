// package com.tmax.eTest.Contents.job;

// import java.util.List;

// import javax.sql.DataSource;

// import com.tmax.eTest.Common.model.stat.HitStat;
// import com.tmax.eTest.Contents.dto.LRSGetStatementDTO;
// import com.tmax.eTest.Contents.dto.LRSStatementDTO;
// import com.tmax.eTest.Contents.util.LRSService;

// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import
// org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
// import
// org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
// import
// org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
// import
// org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
// import org.springframework.batch.core.configuration.annotation.StepScope;
// import org.springframework.batch.core.repository.JobRepository;
// import
// org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
// import org.springframework.batch.item.ItemProcessor;
// import org.springframework.batch.item.ItemWriter;
// import org.springframework.batch.item.support.ListItemReader;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.PlatformTransactionManager;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Configuration
// @EnableBatchProcessing
// public class StatJobConfiguration extends DefaultBatchConfigurer {

// // For Spring batch job to apply Tibero
// ////////////////////////////////////////////////////////////////
// @Autowired
// private DataSource dataSource;
// @Autowired
// private PlatformTransactionManager transactionManager;

// public StatJobConfiguration() {
// super();
// }

// public StatJobConfiguration(DataSource dataSource) {
// super(dataSource);
// }

// @Override
// protected JobRepository createJobRepository() throws Exception {
// JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
// factory.setDataSource(dataSource);
// factory.setDatabaseType("ORACLE");
// factory.setTransactionManager(transactionManager);
// factory.afterPropertiesSet();
// return factory.getObject();
// }

// ////////////////////////////////////////////////////////////////

// @Autowired
// public JobBuilderFactory jobBuilderFactory;

// @Autowired
// public StepBuilderFactory stepBuilderFactory;

// @Autowired
// private LRSService lrsService;

// @Bean
// public Job statJob() {
// return jobBuilderFactory.get("statJob").start(statStep).build();
// }

// @Bean
// public Step statStep() {
// return stepBuilderFactory.get("statStep").<LRSStatementDTO,
// HitStat>chunk(100).reader(statReader())
// .processor(statProcessor()).writer(statWriter()).build();
// }

// @Bean
// @StepScope
// public ListItemReader<LRSStatementDTO> statReader() {
// lrsService.init("/StatementList");
// LRSGetStatementDTO lrsGetStatementDTO = lrsService.makeGetStatement();
// List<LRSStatementDTO> lrsStatementDTOs =
// lrsService.getStatementList(lrsGetStatementDTO);

// return new ListItemReader<>(lrsStatementDTOs);
// }

// public ItemProcessor<LRSStatementDTO, HitStat> statProcessor() {
// String date = LocalDate.now().toString();

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

// public ItemWriter<HitStat> statWriter() {

// }

// }
