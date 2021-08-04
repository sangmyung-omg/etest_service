package com.tmax.eTest.Auth.repository;

import com.tmax.eTest.Auth.dto.AuthProvider;
import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("AU-UserRepository")
public interface UserRepository extends JpaRepository<UserMaster, Long> {
    Optional<UserMaster> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<UserMaster> findByUserUuid(String userUuid);
    @Query("select u from com.tmax.eTest.Common.model.user.UserMaster u where u.providerId = :providerId and u.provider = :provider")
    Optional<UserMaster> findByProviderIdAndProvider(@Param("providerId") String providerId,
                                                     @Param("provider") AuthProvider provider);
}

