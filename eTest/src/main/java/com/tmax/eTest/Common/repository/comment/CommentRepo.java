package com.tmax.eTest.Common.repository.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.comment.CommentInfo;
import com.tmax.eTest.Common.model.comment.CommentKey;

@Repository
public interface CommentRepo extends JpaRepository<CommentInfo, CommentKey>  {
	
	boolean existsByVersionName(String versionName);
	
	@Transactional
	Long deleteByVersionName(String versionName);
	
	List<CommentInfo> findAllByVersionName(String versionName);
}
