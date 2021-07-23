package com.tmax.eTest.Common.repository.user;

import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.model.user.UserKnowledgeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserKnowledgeRepo extends JpaRepository<UserKnowledge, UserKnowledgeKey>{
    
}
