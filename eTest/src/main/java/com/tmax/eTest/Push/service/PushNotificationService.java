package com.tmax.eTest.Push.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.tmax.eTest.Push.dto.AdminPushRequestDTO;
import com.tmax.eTest.Push.dto.CategoryPushRequestDTO;
import com.tmax.eTest.Push.dto.UserNotificationConfigEditDTO;
import com.tmax.eTest.Push.model.UserNotificationConfig;
import com.tmax.eTest.Push.repository.*;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        }
        else{
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

    public void addNewToken(String token) {
        userNotificationConfigRepository.save(
                UserNotificationConfig.builder()
                        .token(token)
                        .global("Y")
                        .notice("Y")
                        .inquiry("Y")
                        .content("Y")
                        .build()
        );
    }

    public void linkTokenWithUserUuid(String userUuid, String token) {
        List<UserNotificationConfig> userNotificationConfigList
                = userNotificationConfigRepositorySupport.getUserNotificationConfigByUserUuid(userUuid);
        UserNotificationConfig userNotificationConfig;

        if (userNotificationConfigList.size() > 0)
            userNotificationConfig = userNotificationConfigList.get(0);
        else
            userNotificationConfig = UserNotificationConfig.builder()
                    .userUuid(userUuid)
                    .global("Y")
                    .notice("Y")
                    .inquiry("Y")
                    .content("Y")
                    .build();

        userNotificationConfig.setToken(token);
        userNotificationConfigRepository.save(userNotificationConfig);
    }

    private void saveNotificationList
            (List<String> tokenList, String category, String title, Timestamp currentDateTime) {
        List<com.tmax.eTest.Push.model.Notification> notificationList = new ArrayList<>();
        for (String token : tokenList) {
            List<String> userUuidList = userNotificationConfigRepositorySupport.getUserUuidByToken(token);
            String userUuid = (userUuidList.size() == 0) ? null : userUuidList.get(0);
            notificationList.add(
                    com.tmax.eTest.Push.model.Notification.builder()
                            .token(token)
                            .userUuid(userUuid)
                            .read("N")
                            .category(category)
                            .title(title)
                            .timestamp(currentDateTime)
                            .build()
            );
        }
        notificationRepository.saveAll(notificationList);
    }

    public void adminPushRequest(AdminPushRequestDTO data) throws FirebaseMessagingException {
        logger.info("admin push request received.");
        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        List<String> tokenList = data.getToken();
        saveNotificationList(data.getToken(), data.getCategory(), data.getTitle(), currentDateTime);
        logger.info("new notifications inserted.");

        Notification notification = Notification.builder()
                .setTitle(data.getTitle())
                .setBody(data.getBody())
                .setImage(data.getImage())
                .build();

        for (int i = 0; i < tokenList.size(); i += multicastMessageSize){
            MulticastMessage.Builder builder = MulticastMessage.builder();
            MulticastMessage message = builder
                    .setNotification(notification)
                    .addAllTokens(tokenList.subList(i, (int) Math.min(i + multicastMessageSize, tokenList.size())))
                    .build();
            sendMessage(message);
        }
        logger.info("admin push successfully sent.");
    }

    public void categoryPushRequest(CategoryPushRequestDTO data) throws FirebaseMessagingException {
        logger.info("{} push request received.", data.getCategory());
        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        List<String> tokenList = userNotificationConfigRepositorySupport.getFilteredTokens(data.getCategory());
        saveNotificationList(tokenList, data.getCategory(), data.getTitle(), currentDateTime);
        logger.info("new notifications inserted.");

        Notification notification = Notification.builder()
                .setTitle(data.getTitle())
                .setBody(data.getBody())
                .setImage(data.getImage())
                .build();

        for (int i = 0; i < tokenList.size(); i += multicastMessageSize) {
            MulticastMessage.Builder builder = MulticastMessage.builder();
            MulticastMessage message = builder
                    .setNotification(notification)
                    .addAllTokens(tokenList.subList(i, (int) Math.min(i + multicastMessageSize, tokenList.size())))
                    .build();
            sendMessage(message);
        }
        logger.info("{} push successfully sent.", data.getCategory());
    }

    public void editNotificationConfig(UserNotificationConfigEditDTO editData){
        List<UserNotificationConfig> userNotificationConfigList
                = userNotificationConfigRepositorySupport.getUserNotificationConfigByUserUuid(editData.getUserUuid());
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
        String userUuid = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken.substring(7)).getBody().get("userUuid").toString();
        return getNotificationListByUserUuid(userUuid);
    }

    public List<com.tmax.eTest.Push.model.Notification> getNotificationListByUserUuid(String userUuid) {
        return notificationRepository.findAllByUserUuid(userUuid);
    }

    public List<com.tmax.eTest.Push.model.Notification> getNotificationListByToken(String token) {
        return notificationRepository.findAllByToken(token);
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
