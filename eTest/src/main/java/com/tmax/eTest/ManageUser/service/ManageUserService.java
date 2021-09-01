package com.tmax.eTest.ManageUser.service;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Common.repository.user.UserMasterRepo;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.service.ContentsService;
import com.tmax.eTest.ManageUser.model.dto.UserInfoDTO;
import com.tmax.eTest.ManageUser.model.dto.UserPopupDTO;
import com.tmax.eTest.ManageUser.repository.DiagnosisReportRepository;
import com.tmax.eTest.ManageUser.repository.MIniTestReportRepository;
import com.tmax.eTest.ManageUser.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class ManageUserService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    DiagnosisReportRepository diagnosisReportRepository;

    @Autowired
    MIniTestReportRepository miniTestReportRepository;

    @Autowired
    ContentsService contentsService;

    public UserPopupDTO getUserPopupData(String user_uuid){
        UserMaster user = userRepository.findByUserUuid(user_uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found With Provided UUID"));

        return UserPopupDTO.builder()
                .user_uuid(user.getUserUuid())
                .nick_name(user.getNickname())
                .gender(user.getGender())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();

    }

    public void deleteUserById(String user_uuid){
        UserMaster user = userRepository.findByUserUuid(user_uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found With Provided UUID"));
        userRepository.delete(user);
    }

    public List<DiagnosisReport> getUserDiagnosisReports(String user_uuid){

        List<DiagnosisReport> diagnosisReports = diagnosisReportRepository.findAllByUserUuid(user_uuid);

        //List<DiagnosisReport> diagnosisReports = diagnosisReportRepository.findAll();

        if (diagnosisReports.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Diagnosis Report Found With Provided UUID ");
        }

        LOGGER.info(diagnosisReports);
        return diagnosisReports;
    }

    public List<UserMaster> findAllUser(){
        List<UserMaster> userList = userRepository.findAll();

        if (userList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User in DB");
        }
        return userList;
    }

    public List<UserInfoDTO> listUserInfo(){

        List<UserMaster> userList = findAllUser();

        return userList.stream()
                .map((user) -> buildUserInfoDTO(user.getUserUuid()))
                .collect(Collectors.toList());
    }

    public UserInfoDTO buildUserInfoDTO(String user_uuid){

        return UserInfoDTO.builder()
                .userInfo(getUserInfo(user_uuid))
                .diagnosisInfo(getUserDiagnosisInfo(user_uuid))
                .minitestInfo(getUserMinitestInfo(user_uuid))
                .contentsInfo(getUserContentsInfo(user_uuid))
                .build();
    }

    public UserInfoDTO.UserInfo getUserInfo(String user_uuid){
        UserMaster user = userRepository.findByUserUuid(user_uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found With Provided UUID" + user_uuid));

        return UserInfoDTO.UserInfo.builder()
                .userUuid(user.getUserUuid())
                .nickname(user.getNickname())
                .provider(user.getProvider())
                .event_sms_agreement(user.getEvent_sms_agreement())
                .build();
    }

    public UserInfoDTO.DiagnosisInfo getUserDiagnosisInfo(String user_uuid){

        List<DiagnosisReport> diagnosisReports = diagnosisReportRepository.findByUserUuid(user_uuid);

        Integer count = diagnosisReports.size();
        OptionalDouble averageScore = diagnosisReports.stream()
                .mapToDouble((selectedReport) -> selectedReport.getGiScore())
                .average();

        Integer score = averageScore.isPresent() ? (int) averageScore.getAsDouble() : null;

        return UserInfoDTO.DiagnosisInfo.builder()
                .count(count)
                .averageScore(score)
                .build();
    }

    public UserInfoDTO.MinitestInfo getUserMinitestInfo(String user_uuid){

        List<MinitestReport> minitestReports = miniTestReportRepository.findByUserUuid(user_uuid);

        Integer count = minitestReports.size();
        OptionalDouble averageScore = minitestReports.stream()
                .mapToDouble((selectedReport) -> selectedReport.getAvgUkMastery())
                .average();

        Integer score = averageScore.isPresent() ? (int) averageScore.getAsDouble() : null;

        return UserInfoDTO.MinitestInfo.builder()
                .count(count)
                .averageScore(score)
                .build();
    }

    public UserInfoDTO.ContentsInfo getUserContentsInfo(String user_uuid){

        ListDTO userBookmarks = contentsService.getBookmarkList(user_uuid);

        return UserInfoDTO.ContentsInfo.builder()
                .videoViews(userBookmarks.getVideo().getSize())
                .bookViews(userBookmarks.getBook().getSize())
                .wikiViews(userBookmarks.getWiki().getSize())
                .articleViews(userBookmarks.getArticle().getSize())
                .build().setTotalBookmarkCount();
    }

}
