package com.tmax.eTest.Push.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tmax.eTest.Push.dto.AdminPushRequestDTO;
import com.tmax.eTest.Push.dto.CategoryPushRequestDTO;
import com.tmax.eTest.Push.dto.UserNotificationConfigEditDTO;
import com.tmax.eTest.Push.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PushNotificationController {
    private final PushNotificationService pushNotificationService;

    @PostMapping("push/admin")
    public ResponseEntity<?> adminNotification(@RequestBody AdminPushRequestDTO data) throws FirebaseMessagingException {
        pushNotificationService.adminPushRequest(data);
        return new ResponseEntity<>("admin push sent", HttpStatus.OK);
    }

    @PostMapping("push/category")
    public void filteredNotification(@RequestBody CategoryPushRequestDTO data) throws FirebaseMessagingException {
        pushNotificationService.categoryPushRequest(data);
    }

    @PostMapping("push/config/add/token")
    public void addNewToken(@RequestParam String token) {
        pushNotificationService.addNewToken(token);
    }

    @PostMapping("push/config/add/user")
    public void linkTokenWithUserUuid(@RequestParam String userUuid, @RequestParam String token) {
        pushNotificationService.linkTokenWithUserUuid(userUuid, token);
    }

    @PutMapping("push/config/edit")
    public void editNotificationConfig(@RequestBody UserNotificationConfigEditDTO editData) {
        pushNotificationService.editNotificationConfig(editData);
    }

    @GetMapping("notification/list")
    public ResponseEntity<?> getNotificationList (
            @RequestParam(required = false) String userUuid,
            @RequestParam(required = false) String token) throws Exception {
        if (userUuid != null & token == null)
            return new ResponseEntity<>(pushNotificationService.getNotificationListByUserUuid(userUuid), HttpStatus.OK);
        else if (userUuid == null & token != null)
            return new ResponseEntity<>(pushNotificationService.getNotificationListByToken(token), HttpStatus.OK);
        else if (userUuid != null & token != null)
            throw new Exception("Both userUuid and token is given");
        throw new Exception("userUuid or token is not given");
    }

    @PutMapping("notification/read")
    public void notificationRead(@RequestParam Long id) {
        pushNotificationService.notificationRead(id);
    }

    @PutMapping("notification/readall")
    public void notificationReadAll(@RequestParam List<Long> ids) {
        pushNotificationService.notificationReadAll(ids);
    }
}
