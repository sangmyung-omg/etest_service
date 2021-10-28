package com.tmax.eTest.Contents.repository;

import com.tmax.eTest.Common.repository.video.VideoRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

  @Autowired
  private VideoRepository videoRepository;

  @Test
  public void test() {

    // List<String> ids = new ArrayList<>(Arrays.asList("질문1-2", "보카7", "질문5-2",
    // "단계2-4", "보카0", "질문1-1"));
    // log.debug(ids.size() + "");
    // Long st = System.currentTimeMillis();
    // log.debug("" + st);
    // // List<Video> videos = videoRepository.findAllByRelatedIn(ids);
    // log.debug("Time: " + (System.currentTimeMillis() - st));

  }
}
