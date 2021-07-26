package com.tmax.eTest.Common.repository.user;

import com.tmax.eTest.Common.model.user.UserEmbedding;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmbeddingRepo extends JpaRepository<UserEmbedding, String>{
    
}
