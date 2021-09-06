package com.tmax.eTest.Admin.managementHistory.repository;

import com.tmax.eTest.Admin.managementHistory.model.ManagementHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagementHistoryRepository extends JpaRepository<ManagementHistory, Long> {}