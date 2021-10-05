package com.tmax.eTest.Admin.permissionManagement.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Admin.permissionManagement.dto.PermissionManagementDTO;
import com.tmax.eTest.Admin.permissionManagement.dto.PermissionUserMasterSearchDTO;
import com.tmax.eTest.Auth.dto.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static com.tmax.eTest.Common.model.user.QUserMaster.userMaster;

@Repository
public class PermissionManagementRepository {
    private final JPAQueryFactory query;

    public PermissionManagementRepository(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    private BooleanExpression masterFilter(Role role) {
        if (role == null)
            return userMaster.role.eq(Role.MASTER).or
                    (userMaster.role.eq(Role.SUB_MASTER));
        return userMaster.role.eq(role);
    }

    private BooleanExpression masterSearchFilter(String search) {
        if (search == null)
            return null;
        return userMaster.name.contains(search)
                .or(userMaster.email.contains(search));
    }

    private BooleanExpression userSearchFilter(String search) {
        if (search == null)
            return null;
        return userMaster.name.contains(search);
    }

    public List<PermissionManagementDTO> searchMaster(Role role, String search){
        return query.select(Projections.constructor(PermissionManagementDTO.class,
                    userMaster.userUuid,
                    userMaster.role,
                    userMaster.name,
                    userMaster.email,
                    userMaster.ip,
                    userMaster.createDate))
                .from(userMaster)
                .where(masterFilter(role).and(masterSearchFilter(search)))
                .fetch();
    }

    public List<PermissionUserMasterSearchDTO> addMasterSearch(String search) {
        return query.select(Projections.constructor(PermissionUserMasterSearchDTO.class,
                    userMaster.userUuid,
                    userMaster.role,
                    userMaster.name))
                .from(userMaster)
                .where(userMaster.role.eq(Role.MASTER).or(userMaster.role.eq(Role.SUB_MASTER))
                        .and(userSearchFilter(search)))
                .fetch();
    }

    @Transactional
    public void updatePermission(String userUuid, Role role, String ip){
        if (role == Role.USER)
            query.update(userMaster)
                    .where(userMaster.userUuid.eq(userUuid))
                    .set(userMaster.role, role)
                    .set(userMaster.ip, "")
                    .set(userMaster.providerId, "")
                    .execute();
        else
            query.update(userMaster)
                .where(userMaster.userUuid.eq(userUuid))
                .set(userMaster.role, role)
                .set(userMaster.ip, ip)
                .execute();
    }
}
