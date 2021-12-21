package com.tmax.eTest.Event.service;

import javax.transaction.Transactional;

import com.tmax.eTest.Common.model.event.Event;
import com.tmax.eTest.Common.repository.event.EventRepository;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Event.dto.EventDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private EventRepository eventRepository;

  public EventDTO getUserEventInfo(String userId) {
    Event event = findById(userId);
    return new EventDTO(event);
  }

  @Transactional
  public SuccessDTO deleteUserEventInfo(String userId) {
    eventRepository.delete(findById(userId));
    return new SuccessDTO(true);
  }

  @Transactional
  public EventDTO createUserEventInfo(String userId, EventDTO eventDTO) {
    if (eventRepository.existsById(userId))
      throw new ContentsException(ErrorCode.EVENT_ERROR, "User already participate in the event.");
    Event event = Event.builder().userUuid(userId).channel(eventDTO.getChannel()).step(eventDTO.getStep()).build();
    Event create = eventRepository.save(event);
    return new EventDTO(create);
  }

  @Transactional
  public EventDTO updateUserEventInfo(String userId, EventDTO eventDTO) {
    Event event = findById(userId);
    if (event.getStep() == 3)
      throw new ContentsException(ErrorCode.EVENT_ERROR, "Already Apply Event!");
    Integer step = eventDTO.getStep();
    event.setStep(eventDTO.getStep());
    if (step == 1) {
      throw new ContentsException(ErrorCode.EVENT_ERROR);
    } else if (step == 3) {
      String phoneNumber = eventDTO.getPhoneNumber();
      String privacyPolicyAgree = eventDTO.getPrivacyPolicyAgree();
      String thirdPartyAgree = eventDTO.getThirdPartyAgree();
      checkDupPhoneNum(userId, phoneNumber);
      checkAgree(privacyPolicyAgree, thirdPartyAgree);
      event.setPhoneNumber(phoneNumber);
      event.setPrivacyPolicyAgree(privacyPolicyAgree);
      event.setThirdPartyAgree(thirdPartyAgree);
    }
    Event update = eventRepository.save(event);
    return new EventDTO(update);
  }

  private Event findById(String userId) {
    Event event = eventRepository.findById(userId).orElseThrow(
        () -> new ContentsException(ErrorCode.EVENT_ERROR, "User didn't participate in the event."));
    return event;
  }

  private void checkDupPhoneNum(String userId, String phoneNumber) {
    if (commonUtils.stringNullCheck(phoneNumber))
      throw new ContentsException(ErrorCode.EVENT_ERROR, "Phone Number Is Necessary!");

    if (eventRepository.existsByPhoneNumber(phoneNumber))
      throw new ContentsException(ErrorCode.EVENT_ERROR, "Duplicate Phone Number.");
  }

  private void checkAgree(String privacyPolicyAgree, String thirdPartyAgree) {
    if (commonUtils.stringNullCheck(privacyPolicyAgree) || commonUtils.stringNullCheck(thirdPartyAgree))
      throw new ContentsException(ErrorCode.EVENT_ERROR, "Agree Is Necessary!");
  }
}
