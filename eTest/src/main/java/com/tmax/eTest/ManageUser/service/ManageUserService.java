package com.tmax.eTest.ManageUser.service;

import com.google.common.collect.Iterables;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.service.ContentsService;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.ManageUser.model.dto.UserInfoDTO;
import com.tmax.eTest.ManageUser.model.dto.UserPopupDTO;
import com.tmax.eTest.ManageUser.repository.DiagnosisReportRepository;
import com.tmax.eTest.ManageUser.repository.MIniTestReportRepository;
import com.tmax.eTest.ManageUser.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManageUserService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("ManageUserDiagnosisReportRepository")
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
                .map((user) -> {
                    try {
                        return buildUserInfoDTO(user.getUserUuid());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LRS PARSE EXCEPTION");
                })
                .collect(Collectors.toList());
    }

    public UserInfoDTO buildUserInfoDTO(String user_uuid) throws ParseException {

        return UserInfoDTO.builder()
                .userInfo(getUserInfo(user_uuid))
                .diagnosisInfo(getUserDiagnosisInfo(user_uuid))
                .minitestInfo(getUserMinitestInfo(user_uuid))
                .contentsInfo(getUserContentsInfo(user_uuid))
                .lrsInfo(getUserLRSInfo(user_uuid))
                .build();
    }

    public UserInfoDTO.UserInfo getUserInfo(String user_uuid){
        UserMaster user = userRepository.findByUserUuid(user_uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found With Provided UUID" + user_uuid));

        return UserInfoDTO.UserInfo.builder()
                .userUuid(user.getUserUuid())
                .createDate(user.getCreateDate())
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

        Integer score = averageScore.isPresent() ? (int) averageScore.getAsDouble() : 0;

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

        Integer score = averageScore.isPresent() ? (int) averageScore.getAsDouble() : 0;

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

    @Autowired
    LRSAPIManager LRS;
    public UserInfoDTO.LRSInfo getUserLRSInfo(String user_uuid) throws ParseException {

        GetStatementInfoDTO loginQuery = GetStatementInfoDTO.builder()
            .actionTypeList(Arrays.asList("enter"))
            .sourceTypeList(Arrays.asList("application"))
            .userIdList(Arrays.asList(user_uuid))
            .build();

        List<StatementDTO> lrsStatement = LRS.getStatementList(loginQuery);

        //LOGGER.info(lrsStatement.size());

        StatementDTO lastLoginRecord = Iterables.getLast(lrsStatement, null);
        String lastLoginTime = lastLoginRecord==null ? null : lastLoginRecord.getTimestamp();

        Integer noActivityDays = lastLoginTime == null ? null : daysBetweenCurrentTime(timeStringToTimestampObj(lastLoginTime));

        return UserInfoDTO.LRSInfo.builder()
                .totalVisitCount(lrsStatement.size())
                .noActivityDays(noActivityDays)
                .build();

    }

    private Timestamp timeStringToTimestampObj(String timestampStr)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA);
        Timestamp timestampObj = null;

        try {
            timestampObj = new Timestamp(dateFormat.parse(timestampStr).getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return null;
        }

        return timestampObj;
    }

    private Integer daysBetweenCurrentTime(Timestamp lastTimestamp){

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        Integer dayDifference = (int) ChronoUnit.DAYS.between(lastTimestamp.toInstant(), currentTimestamp.toInstant());

        return dayDifference;
    }

}
