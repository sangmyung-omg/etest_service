package com.tmax.eTest.Support.service;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Common.model.support.FAQ;
import com.tmax.eTest.Support.dto.UpdateFAQViewDto;
import com.tmax.eTest.Support.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FAQService {
    @Autowired
    @Qualifier("SU-FAQRepository")
    FAQRepository faqRepository;

    @Transactional
    public CMRespDto<?> updateFAQView(UpdateFAQViewDto updateFAQViewDto) {
        Optional<FAQ> faqOptional = faqRepository.findById(updateFAQViewDto.getId());
        if (faqOptional.isPresent()) {
            FAQ faq = faqOptional.get();
            Long view = faq.getViews();
            faq.setViews(view+1);
            return new CMRespDto<>(200,"success",view+1);
        }
        throw new IllegalArgumentException("faq id is not exist");
    }
}
