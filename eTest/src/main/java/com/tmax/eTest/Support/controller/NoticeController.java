package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Common.model.support.Notice;
import com.tmax.eTest.Support.dto.CreateNoticeDto;
import com.tmax.eTest.Support.dto.CreateNoticeRequestDto;
import com.tmax.eTest.Support.dto.NoticeImageRequestDto;
import com.tmax.eTest.Support.repository.NoticeRepository;
import com.tmax.eTest.Support.service.NoticeService;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    @Qualifier("SU-NoticeRepository")
    private NoticeRepository noticeRepository;
    @Value("${file.path}")
    private String rootPath;
    @GetMapping("/notice")
    public CMRespDto<?> findAllNotice() {
        List<Notice> noticeList =  noticeRepository.findAll();
        return new CMRespDto<>(200,"공지사항 가져오기 성공",noticeList);
    }

    @PostMapping("/create/notice")
    public CMRespDto<?> createNotice(@ModelAttribute CreateNoticeRequestDto createNoticeRequestDto) {

        return noticeService.createNotice(createNoticeRequestDto);
    }

    @GetMapping(value = "/notice/image")
    public ResponseEntity<Resource> getNoticeImage(@Param("fileName")String fileName ) {
        HttpHeaders header = new HttpHeaders();
        Path imageUrl = Paths.get(rootPath + "/notice/" + fileName);

        Tika tika = new Tika();
        String mimeType;
        try {
            mimeType = tika.detect(imageUrl);
            header.add("Content-Type", mimeType);
        } catch (IOException e) {
            throw new IllegalArgumentException("mime type error");
        }
        return noticeService.getNoticeImage(fileName);
    }
}
