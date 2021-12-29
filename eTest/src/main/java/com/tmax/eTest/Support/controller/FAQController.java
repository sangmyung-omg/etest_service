package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Common.model.support.FAQ;
import com.tmax.eTest.Support.dto.UpdateFAQViewDto;
import com.tmax.eTest.Support.repository.FAQRepository;
import com.tmax.eTest.Support.service.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FAQController {

    @Autowired
    @Qualifier("SU-FAQRepository")
    private FAQRepository faqRepository;

    @Autowired
    private FAQService faqService;

    @GetMapping("/faq")
    private CMRespDto<?> faq() {
        List<FAQ> faqList = faqRepository.findAll();
        return new CMRespDto<>(200, "faqList 반환 성공", faqList);
    }

    @PostMapping("/faq/update/view")
    private CMRespDto<?> updateFAQView(@RequestBody UpdateFAQViewDto updateFAQViewDto) {
        return faqService.updateFAQView(updateFAQViewDto);
    }
}
