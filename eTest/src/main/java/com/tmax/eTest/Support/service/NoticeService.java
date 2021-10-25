package com.tmax.eTest.Support.service;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Common.model.support.Notice;
import com.tmax.eTest.Support.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class NoticeService {
    @Autowired
    @Qualifier("SU-NoticeRepository")
    private NoticeRepository noticeRepository;

}
