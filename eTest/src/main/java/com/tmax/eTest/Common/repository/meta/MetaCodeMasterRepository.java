package com.tmax.eTest.Common.repository.meta;

import com.tmax.eTest.Common.model.meta.MetaCodeMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaCodeMasterRepository extends JpaRepository<MetaCodeMaster, String> {

}
