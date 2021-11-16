package com.tmax.eTest.Push.repository;

import com.tmax.eTest.Push.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserUuid(String userUuid);
    List<Notification> findAllByToken(String token);
}
