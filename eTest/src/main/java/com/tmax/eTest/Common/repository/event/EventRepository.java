package com.tmax.eTest.Common.repository.event;

import com.tmax.eTest.Common.model.event.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
  Boolean existsByPhoneNumber(String phoneNumber);

  Event findByPhoneNumber(String phoneNumber);
}
