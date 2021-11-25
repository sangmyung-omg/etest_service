package com.tmax.eTest.Common.repository.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tmax.eTest.Common.model.comment.CommentInfo;
import com.tmax.eTest.Common.model.comment.CommentVersionInfo;

@Repository
public interface CommentVersionRepo extends JpaRepository<CommentVersionInfo, String> {
	
	Optional<CommentVersionInfo> findByIsSelected(int isSelected);
}
