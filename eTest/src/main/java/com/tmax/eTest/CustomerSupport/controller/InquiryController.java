package com.tmax.eTest.CustomerSupport.controller;


import com.tmax.eTest.Auth.model.PrincipalDetails;
import com.tmax.eTest.CustomerSupport.model.dto.InquiryAnswerDTO;
import com.tmax.eTest.CustomerSupport.model.dto.InquiryDTO;
import com.tmax.eTest.CustomerSupport.service.InquiryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("CustomerSupportInquiryController")
@RequestMapping(value="submaster/customerSupport", produces = MediaType.APPLICATION_JSON_VALUE)
public class InquiryController {
    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    InquiryService inquiryService;

    @RequestMapping(value="/inquiries", method = RequestMethod.GET)
    public ResponseEntity<?> getInquiryList() {

        List<InquiryDTO> inquiryList = inquiryService.getInquiryList();
        return ResponseEntity.ok().body(inquiryList);
    }

    @RequestMapping(value="/inquiry", method = RequestMethod.GET)
    public ResponseEntity<?> getInquiryDetail(@RequestParam(value="inquiryId") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails){

        InquiryDTO inquiry = inquiryService.getInquiryDetails(id);
        return ResponseEntity.ok().body(inquiry);
    }

    @RequestMapping(value="/inquiry", method = RequestMethod.PUT)
    public ResponseEntity<?> answerInquiry(@RequestParam(value="inquiryId") Long id,
                                           @RequestBody InquiryAnswerDTO inquiryAnswerDTO,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        String admin_uuid = principalDetails.getUserUuid();
        String admin_nickname = principalDetails.getNickname();
        InquiryDTO inquiry = inquiryService.answerInquiry(id,admin_uuid, admin_nickname, inquiryAnswerDTO.getAnswer());
        return ResponseEntity.ok().body(inquiry);
    }

    @RequestMapping(value="/inquiry", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInquiry(@RequestParam(value="inquiryId") Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.ok().body("success delete inquiry");
    }




}
