package com.tmax.eTest.Test.util;

import com.tmax.eTest.Common.repository.uk.VersionMasterRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UkVersionManager {
    @Autowired
    private VersionMasterRepo versionRepo;

    public static Long currentUkVersionId = Long.parseLong("-1");

    @EventListener
    public void startUpGenerator(ApplicationStartedEvent event) {
        log.debug("> Initially checking current default UK version ID from DB...");
        currentUkVersionId = getDefaultVersionId();
        log.info(Long.toString(currentUkVersionId));
    }

    @Scheduled(fixedRate = 60*60*1000, initialDelay = 60*60*1000)
    public void scheduledUpdater() {
        log.debug("> Periodically checking current default UK version ID from DB...");
        currentUkVersionId = getDefaultVersionId();
    }

    private Long getDefaultVersionId() {
        String checkIsDefault = "1";
        return versionRepo.findByIsDefault(checkIsDefault).getVersionId();
    }

    public Long getCurrentUkVersionId() {
        return currentUkVersionId;
    }

    public void setCurrentUkVersionId(Long newId) {
        currentUkVersionId = newId;
    }
}
