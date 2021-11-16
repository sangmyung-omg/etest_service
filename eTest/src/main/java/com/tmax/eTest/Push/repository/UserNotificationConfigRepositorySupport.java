package com.tmax.eTest.Push.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Push.model.UserNotificationConfig;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tmax.eTest.Common.model.user.QUserMaster.userMaster;
import static com.tmax.eTest.Push.model.QUserNotificationConfig.userNotificationConfig;

@Repository
public class UserNotificationConfigRepositorySupport {
    private final JPAQueryFactory query;

    public UserNotificationConfigRepositorySupport(EntityManager entityManager) { this.query = new JPAQueryFactory(entityManager); }

    public List<String> getNewUsers() {
        return query.select(userMaster.userUuid)
                .from(userMaster)
                .leftJoin(userNotificationConfig).on(userMaster.userUuid.eq(userNotificationConfig.userUuid))
                .where(userNotificationConfig.userUuid.isNull())
                .fetch();
    }

    public List<String> getUserUuidByToken(String token) {
        return query.select(userNotificationConfig.userUuid)
                .from(userNotificationConfig)
                .where(userNotificationConfig.token.eq(token))
                .fetch();
    }

    public List<UserNotificationConfig> getUserNotificationConfigByUserUuid(String userUuid) {
        return query.select(userNotificationConfig)
                .from(userNotificationConfig)
                .where(userNotificationConfig.userUuid.eq(userUuid))
                .fetch();
    }

    public List<UserNotificationConfig> getUserNotificationConfigByToken(String token) {
        return query.select(userNotificationConfig)
                .from(userNotificationConfig)
                .where(userNotificationConfig.token.eq(token))
                .fetch();
    }

    private BooleanExpression itemFilter(String item) {
        switch (item) {
            case "notice":
                return userNotificationConfig.notice.eq("Y");
            case "inquiry":
                return userNotificationConfig.inquiry.eq("Y");
            case "content":
                return userNotificationConfig.content.eq("Y");
        }
        return null;
    }

    public List<String> getFilteredTokens(String item) {
        return query.select(userNotificationConfig.token)
                .from(userNotificationConfig)
//                .leftJoin(userNotificationConfig).on(userNotificationConfig.userUuid.eq(userMaster.userUuid))
                .where(itemFilter(item).and(userNotificationConfig.global.eq("Y")))
                .fetch();
    }
}
