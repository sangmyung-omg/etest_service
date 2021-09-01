package com.tmax.eTest.Support.repository;

import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.dto.InquiryHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("SU-InquiryRepository")
public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

      @Query(value = "SELECT * FROM cs_inquiry WHERE Inquiry_ID = :Inquiry_ID", nativeQuery = true)
      Optional<Inquiry> findByInquiryId(@Param("Inquiry_ID") Long Inquiry_ID);

      @Query(value = "SELECT INQUIRY_ID FROM CS_INQUIRY WHERE USER_UUID = :USER_UUID", nativeQuery = true)
      List<Long> findAllIdByUserUuid(@Param("USER_UUID") String userUuid);

      @Query(value = "SELECT * FROM CS_INQUIRY WHERE USER_UUID = :USER_UUID", nativeQuery = true)
      List<InquiryHistoryDTO> findAllInquiryHistoryByUserUuid(@Param("USER_UUID") String userUuid);

}
