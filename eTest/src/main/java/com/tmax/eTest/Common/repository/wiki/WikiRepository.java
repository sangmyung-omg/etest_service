package com.tmax.eTest.Common.repository.wiki;

import com.tmax.eTest.Common.model.wiki.Wiki;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiRepository extends JpaRepository<Wiki, Long>{
  
}
