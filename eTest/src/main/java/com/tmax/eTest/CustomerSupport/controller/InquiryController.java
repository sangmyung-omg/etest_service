package com.tmax.eTest.CustomerSupport.controller;


import com.tmax.eTest.CustomerSupport.model.dto.InquiryDTO;
import com.tmax.eTest.CustomerSupport.service.InquiryService;
import com.tmax.eTest.ManageUser.model.dto.UserInfoDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("CustomerSupportInquiryController")
@RequestMapping(value="/customerSupport", produces = MediaType.APPLICATION_JSON_VALUE)
public class InquiryController {
    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    InquiryService inquiryService;

    @RequestMapping(value="/inquiries", method = RequestMethod.GET)
    public ResponseEntity<?> getInquiryList() {

        List<InquiryDTO> inquiryList = inquiryService.getInquiryList();
        return ResponseEntity.accepted().body(inquiryList);
    }

}
