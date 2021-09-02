package com.tmax.eTest.Common.repository.uk;

import java.util.Set;

import com.tmax.eTest.Common.model.uk.UkMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UkMasterRepo extends JpaRepository<UkMaster, Integer>{
    @Query("select distinct u.part from UkMaster u where u.part is not null order by u.part")
    Set<String> getDistinctPartList();
}
