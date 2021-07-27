package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.stereotype.Repository;

@Repository("TE-UserRepository")

public interface UserRepository extends JpaRepository<UserMaster, Long> {
    UserMaster findByEmail(String email);


}
