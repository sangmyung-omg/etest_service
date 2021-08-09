package com.tmax.eTest.Common.repository.user;

import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.model.user.UserKnowledgeKey;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Common.model.video.VideoJoin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserKnowledgeRepo extends JpaRepository<UserKnowledge, UserKnowledgeKey>{
	
	@Query("select uk from com.tmax.eTest.Common.model.user.UserKnowledge uk where uk.userUuid = :userUuid and rownum <= 3 order by uk.ukMastery")
	List<UserKnowledge> findLowMasteryListByUserUuid(@Param("userUuid") String userUuid);
}
