package com.tmax.eTest.Common.repository.video;

import com.tmax.eTest.Common.model.video.VideoCurriculum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCurriculumRepository extends JpaRepository<VideoCurriculum, Long> {

}
