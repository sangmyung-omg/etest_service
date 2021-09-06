package com.tmax.eTest.Admin.managementHistory.repository;

import com.tmax.eTest.Admin.managementHistory.model.RequestMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestMapperRepository extends JpaRepository<RequestMapper, Long> {
    RequestMapper findByMethodAndControllerAndName(String method, String controller, String name);
}