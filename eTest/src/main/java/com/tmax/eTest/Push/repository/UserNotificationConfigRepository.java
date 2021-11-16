package com.tmax.eTest.Push.repository;

import com.tmax.eTest.Push.model.UserNotificationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationConfigRepository extends JpaRepository<UserNotificationConfig, String> {}
