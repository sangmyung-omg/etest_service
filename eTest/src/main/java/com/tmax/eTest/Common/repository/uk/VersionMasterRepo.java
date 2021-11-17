package com.tmax.eTest.Common.repository.uk;

import com.tmax.eTest.Common.model.uk.VersionMaster;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionMasterRepo extends JpaRepository<VersionMaster, Long> {
    VersionMaster findByIsDefault(String isDefault);
}
