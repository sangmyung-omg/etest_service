package com.tmax.eTest.Support.service;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Common.model.support.FAQ;
import com.tmax.eTest.Support.dto.UpdateFAQViewDto;
import com.tmax.eTest.Support.repository.FAQRepository;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Service
public class FAQService {
    private static final Logger logger = LoggerFactory.getLogger(FAQService.class);

    @Autowired
    @Qualifier("SU-FAQRepository")
    FAQRepository faqRepository;

    @Value("${file.path}")
    private String path;

    @Transactional
    public CMRespDto<?> updateFAQView(UpdateFAQViewDto updateFAQViewDto) {
        Optional<FAQ> faqOptional = faqRepository.findById(updateFAQViewDto.getId());
        if (faqOptional.isPresent()) {
            FAQ faq = faqOptional.get();
            Long view = faq.getViews();
            faq.setViews(view + 1);
            return new CMRespDto<>(200, "success", view + 1);
        }
        throw new IllegalArgumentException("faq id is not exist");
    }

    public ResponseEntity<Resource> attachment(String filename) {
        String temp = path+"/faq/";
        Path filePath = null;
        filePath = Paths.get(temp+filename);
        HttpHeaders header = new HttpHeaders();
        Tika tika = new Tika();
        String mimeType;
        try {
            mimeType = tika.detect(filePath);
            header.add("Content-Type", mimeType);
        } catch (IOException e) {
            logger.debug("mime type Error");
        }

        Resource resource = new FileSystemResource(filePath);
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }
}
