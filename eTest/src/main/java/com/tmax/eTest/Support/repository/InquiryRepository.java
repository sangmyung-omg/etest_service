package com.tmax.eTest.Support.repository;

import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("SU-InquiryRepository")
public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

//    List<Inquiry> findAllByListUserUuid(@Param("userUuid") String userUuid);
//      Optional<Inquiry> findById(Long id);
//      @Query("select i from Inquiry i inner join i.UserMaster u where u.userUuid = :userUuid")
      @Query("select i from Inquiry i where i.userUuid = :userUuid")

      List<Inquiry> findAllByUserUuid(@Param("userUuid") String userUuid);

}
