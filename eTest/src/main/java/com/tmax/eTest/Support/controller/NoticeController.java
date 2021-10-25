package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Common.model.support.Notice;
import com.tmax.eTest.Support.dto.CreateNoticeDto;
import com.tmax.eTest.Support.repository.NoticeRepository;
import com.tmax.eTest.Support.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    @Autowired
    @Qualifier("SU-NoticeRepository")
    private NoticeRepository noticeRepository;

    @GetMapping("/notice")
    public CMRespDto<?> findAllNotice() {
        List<Notice> noticeList =  noticeRepository.findAll();
        return new CMRespDto<>(200,"공지사항 가져오기 성공",noticeList);
    }

}
