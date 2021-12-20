package com.tmax.eTest.Support.service;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Common.model.support.Notice;
import com.tmax.eTest.Support.dto.CreateNoticeRequestDto;
import com.tmax.eTest.Support.repository.NoticeRepository;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.UUID;

@Service
public class NoticeService {
    private static final Logger logger = LoggerFactory.getLogger(NoticeService.class);

    @Value("${file.path}")
    private String rootPath;

    @Autowired
    @Qualifier("SU-NoticeRepository")
    private NoticeRepository noticeRepository;

    public ResponseEntity<Resource> getNoticeImage(String fileName) {
        String filePathString = rootPath + "/notice/";
        Path filePath = Paths.get(filePathString + fileName);
        HttpHeaders header = new HttpHeaders();
        Tika tika = new Tika();
        String mimeType;
        try {
            mimeType = tika.detect(filePath);
            header.add("Content-Type", mimeType);
        } catch (Exception e) {
            throw new IllegalArgumentException("mimeType error");
        }

        Resource resource = new FileSystemResource(filePath);
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

    @Transactional
    public CMRespDto<?> createNotice(CreateNoticeRequestDto createNoticeRequestDto) {
        String noticeImageFolderURL = rootPath + "notice/";
        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        Notice notice = null;
        if (createNoticeRequestDto.getImage() != null) {
            String imageName = UUID.randomUUID() + "_" + createNoticeRequestDto.getImage().getOriginalFilename();
            String imageUrlString = noticeImageFolderURL + imageName;
            Path imageUrlPath = Paths.get(imageUrlString);
            notice =
                    Notice.builder()
                            .content(createNoticeRequestDto.getContent())
                            .title(createNoticeRequestDto.getTitle())
                            .draft(0)
                            .views((long) 0)
                            .dateAdd(currentDateTime)
                            .dateEdit(currentDateTime)
                            .imageUrl(imageUrlString)
                            .build();
            try {
                Files.write(imageUrlPath, createNoticeRequestDto.getImage().getBytes());
            } catch (Exception e) {
                throw new IllegalArgumentException("notice image save error");
            }
            noticeRepository.save(notice);
        }
        if (createNoticeRequestDto.getImage() == null) {
            notice = Notice.builder()
                    .content(createNoticeRequestDto.getContent())
                    .draft(0)
                    .views((long) 0)
                    .dateAdd(currentDateTime)
                    .dateEdit(currentDateTime)
                    .build();
            noticeRepository.save(notice);
        }
        return new CMRespDto<>(200, "success", notice);
    }
}
