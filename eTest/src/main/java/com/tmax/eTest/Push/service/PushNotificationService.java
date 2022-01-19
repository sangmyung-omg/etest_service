package com.tmax.eTest.Push.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.tmax.eTest.Push.dto.PushRequestDTO;
import com.tmax.eTest.Push.dto.UserNotificationConfigEditDTO;
import com.tmax.eTest.Push.dto.UserNotificationConfigInfoDTO;
import com.tmax.eTest.Push.model.UserNotificationConfig;
import com.tmax.eTest.Push.repository.NotificationRepository;
import com.tmax.eTest.Push.repository.UserNotificationConfigRepository;
import com.tmax.eTest.Push.repository.UserNotificationConfigRepositorySupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@PropertySource("classpath:push-config.properties")
@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final UserNotificationConfigRepository userNotificationConfigRepository;
    private final UserNotificationConfigRepositorySupport userNotificationConfigRepositorySupport;
    private final NotificationRepository notificationRepository;
    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${firebase.admin-sdk-credentials}")
    String firebaseAdminSdkCredentials;

    @Value("${firebase.create-scoped}")
    String fireBaseCreateScoped;

    @Value("${firebase.multicast-message-size}")
    Long multicastMessageSize;

    private FirebaseMessaging instance;

    @PostConstruct
    public void firebaseSetting() throws IOException {
        if (!new ClassPathResource(firebaseAdminSdkCredentials).exists()) {
            logger.error("Firebase admin sdk credentials not exists.");
            logger.error("Firebase Cloud Messaging service might not work.");
        } else {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                    new ClassPathResource(firebaseAdminSdkCredentials).getInputStream())
                    .createScoped((Arrays.asList(fireBaseCreateScoped)));

            FirebaseOptions secondaryAppConfig = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(secondaryAppConfig);
            this.instance = FirebaseMessaging.getInstance(app);
        }
    }

    public String sendMessage(Message message) throws FirebaseMessagingException {
        return this.instance.send(message);
    }

    public BatchResponse sendMessage(MulticastMessage message) throws FirebaseMessagingException {
        return this.instance.sendMulticast(message);
    }

    public void addNewToken(String fcmToken) {
        if (!userNotificationConfigRepository.findById(fcmToken).isPresent()) {
            userNotificationConfigRepository.save(
                    UserNotificationConfig.builder()
                            .token(fcmToken)
                            .global("Y")
                            .notice("Y")
                            .inquiry("Y")
                            .content("Y")
                            .build());
            logger.info("added new FCM token");
        }
    }

    public void linkTokenWithUserUuid(String jwtToken, String fcmToken) {
        String userUuid = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken.substring(7)).getBody()
                .get("userUuid").toString();
        List<UserNotificationConfig> userNotificationConfigList = userNotificationConfigRepositorySupport
                .getUserNotificationConfigByUserUuid(userUuid);
        UserNotificationConfig userNotificationConfig;

        if (userNotificationConfigList.size() > 0) {
            UserNotificationConfig u = userNotificationConfigList.get(0);
            userNotificationConfig = userNotificationConfigRepository.findById(fcmToken).get();
            userNotificationConfig.setUserUuid(userUuid);
            userNotificationConfig.setGlobal(u.getGlobal());
            userNotificationConfig.setNotice(u.getNotice());
            userNotificationConfig.setInquiry(u.getInquiry());
            userNotificationConfig.setContent(u.getContent());
        } else
            userNotificationConfig = UserNotificationConfig.builder()
                    .token(fcmToken)
                    .userUuid(userUuid)
                    .global("Y")
                    .notice("Y")
                    .inquiry("Y")
                    .content("Y")
                    .build();
        userNotificationConfigRepository.save(userNotificationConfig);
    }

    public String deleteFcmToken(String fcmToken) {
        if (userNotificationConfigRepository.findById(fcmToken).isPresent()) {
            userNotificationConfigRepository.deleteById(fcmToken);
            return "deleted FCM token";
        } else
            return "cannot found requested FCM token";
    }

    public String refreshFcmToken(String expiredFcmToken, String refreshFcmToken) {
        if (userNotificationConfigRepository.findById(expiredFcmToken).isPresent()) {
            UserNotificationConfig userNotificationConfig = userNotificationConfigRepository.findById(expiredFcmToken)
                    .get();
            userNotificationConfigRepository.deleteById(expiredFcmToken);
            userNotificationConfig.setToken(refreshFcmToken);
            userNotificationConfigRepository.save(userNotificationConfig);
            return "refreshed FCM token";
        } else
            return "cannot found requested FCM token";
    }

    private void saveNotificationList(List<String> userUuidList, String category, String title, Timestamp currentDateTime) {
        List<com.tmax.eTest.Push.model.Notification> notificationList = new ArrayList<>();
        for (String userUuid : userUuidList)
            notificationList.add(
                    com.tmax.eTest.Push.model.Notification.builder()
                            .userUuid(userUuid)
                            .read("N")
                            .category(category)
                            .title(title)
                            .timestamp(currentDateTime)
                            .build());
        notificationRepository.saveAll(notificationList);
    }

    private void pushRequest(PushRequestDTO data, List<String> fcmTokenList, Timestamp currentDateTime)
            throws FirebaseMessagingException {
        List<String> userUuidList = new ArrayList<>();
        for (String token : fcmTokenList) {
            String userUuid = userNotificationConfigRepositorySupport.getUserUuidByToken(token).get(0);
            if (userUuidList.size() == 0)
                userUuidList.add(userUuid);
            for (int i = 0; i < userUuidList.size(); i++){
                String userUuidInList = userUuidList.get(i);
                if (!userUuidInList.equals(userUuid))
                    userUuidList.add(userUuid);
            }
        }
        saveNotificationList(userUuidList, data.getCategory(), data.getBody(), currentDateTime);
        logger.info("new notifications inserted.");

        Notification notification = Notification.builder()
                .setTitle(data.getTitle())
                .setBody(data.getBody())
                .setImage(data.getImage())
                .build();

        for (int i = 0; i < fcmTokenList.size(); i += multicastMessageSize) {
            MulticastMessage.Builder builder = MulticastMessage.builder();
            MulticastMessage message = builder
                    .setNotification(notification)
                    .putData("url", data.getUrl())
                    .addAllTokens(fcmTokenList.subList(i, (int) Math.min(i + multicastMessageSize, fcmTokenList.size())))
                    .build();
            sendMessage(message);
        }
    }

    public void adminPushRequestByToken(PushRequestDTO data) throws FirebaseMessagingException {
        logger.info("admin push request by token received.");
        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        List<String> fcmTokenList = data.getToken();
        pushRequest(data, fcmTokenList, currentDateTime);
        logger.info("admin push successfully sent.");
    }

    public void adminPushRequestByUserUuid(PushRequestDTO data) throws FirebaseMessagingException {
        logger.info("admin push request by userUuid received.");
        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        List<String> fcmTokenList = new ArrayList<>();
        for (String userUuid : data.getUserUuid())
            fcmTokenList.addAll(userNotificationConfigRepositorySupport.getTokenByUserUuid(userUuid));
        pushRequest(data, fcmTokenList, currentDateTime);
        logger.info("admin push successfully sent.");
    }

    public void categoryPushRequest(PushRequestDTO data) throws FirebaseMessagingException {
        logger.info("{} push request received.", data.getCategory());
        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        List<String> fcmTokenList = userNotificationConfigRepositorySupport.getFilteredTokens(data.getCategory());
        pushRequest(data, fcmTokenList, currentDateTime);
        logger.info("{} push successfully sent.", data.getCategory());
    }

    public void categoryPushRequestByUserUuid(PushRequestDTO data) throws FirebaseMessagingException {
        logger.info("{} push request to user(s) received.", data.getCategory());
        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        List<String> fcmTokenList = new ArrayList<>();
        for (String userUuid : data.getUserUuid())
            fcmTokenList.addAll(
                    userNotificationConfigRepositorySupport.getFilteredTokensByUserUuid(data.getCategory(), userUuid)
            );
        pushRequest(data, fcmTokenList, currentDateTime);
        logger.info("{} push successfully sent to user(s).", data.getCategory());
    }

    public void editNotificationConfig(String jwtToken, UserNotificationConfigEditDTO editData) {
        String userUuid = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken.substring(7)).getBody()
                .get("userUuid").toString();
        List<UserNotificationConfig> userNotificationConfigList = userNotificationConfigRepositorySupport
                .getUserNotificationConfigByUserUuid(userUuid);
        for (UserNotificationConfig userNotificationConfig : userNotificationConfigList)
            switch (editData.getCategory()) {
                case "global":
                    userNotificationConfig.setGlobal(editData.getValue());
                    break;
                case "notice":
                    userNotificationConfig.setNotice(editData.getValue());
                    break;
                case "inquiry":
                    userNotificationConfig.setInquiry(editData.getValue());
                    break;
                case "content":
                    userNotificationConfig.setContent(editData.getValue());
                    break;
            }
        userNotificationConfigRepository.saveAll(userNotificationConfigList);
    }

    public List<com.tmax.eTest.Push.model.Notification> getNotificationListByJwtToken(String jwtToken) {
        String userUuid
                = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken.substring(7)).getBody().get("userUuid").toString();
        return getNotificationListByUserUuid(userUuid);
    }

    public List<com.tmax.eTest.Push.model.Notification> getNotificationListByUserUuid(String userUuid) {
        return notificationRepository.findAllByUserUuid(userUuid);
    }


    public UserNotificationConfigInfoDTO getUserNotificationConfigByJwtToken(String jwtToken) {
        String userUuid
                = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken.substring(7)).getBody().get("userUuid").toString();
        UserNotificationConfig userNotificationConfig
                = userNotificationConfigRepositorySupport.getUserNotificationConfigByUserUuid(userUuid).get(0);
        return UserNotificationConfigInfoDTO.builder()
                .global(userNotificationConfig.getGlobal())
                .notice(userNotificationConfig.getNotice())
                .inquiry(userNotificationConfig.getInquiry())
                .content(userNotificationConfig.getContent())
                .build();
    }

    public void notificationRead(Long id) {
        Optional<com.tmax.eTest.Push.model.Notification> optionalNotification = notificationRepository.findById(id);
        if (!optionalNotification.isPresent())
            throw new EntityNotFoundException("Cannot find entity");
        com.tmax.eTest.Push.model.Notification notification = optionalNotification.get();
        notification.setRead("Y");
        notificationRepository.save(notification);
    }

    public void notificationReadAll(List<Long> ids) {
        List<com.tmax.eTest.Push.model.Notification> notificationList = new ArrayList<>();
        for (Long id : ids) {
            Optional<com.tmax.eTest.Push.model.Notification> optionalNotification = notificationRepository.findById(id);
            if (!optionalNotification.isPresent())
                throw new EntityNotFoundException("Cannot find entity");
            com.tmax.eTest.Push.model.Notification notification = optionalNotification.get();
            notification.setRead("Y");
            notificationList.add(notification);
        }
        notificationRepository.saveAll(notificationList);
    }
}
