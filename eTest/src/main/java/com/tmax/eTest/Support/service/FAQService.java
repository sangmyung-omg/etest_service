package com.tmax.eTest.Support.service;

import com.tmax.eTest.Common.model.support.FAQ;
import com.tmax.eTest.Support.Dto.CreateFAQDto;
import com.tmax.eTest.Support.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FAQService {
    @Autowired
    @Qualifier("SU-FAQRepository")
    FAQRepository faqRepository;

    @Transactional
    public Long createFAQ(CreateFAQDto createFaqDto){
        FAQ faq = new FAQ(createFaqDto.getTitle(), createFaqDto.getContent());
        faqRepository.save(faq);
        return faq.getId();
    }

}
