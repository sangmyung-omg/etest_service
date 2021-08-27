package com.tmax.eTest.ManageUser.model.dto;

import com.tmax.eTest.Auth.dto.AuthProvider;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    @Data
    @Builder
    public static class UserInfo{
        String userUuid;
        String nickname;
        AuthProvider provider;
        Boolean event_sms_agreement; //isMarketing???
        //TODO isMarketing??
    }

    @Data
    @Builder
    public static class DiagnosisInfo{
        Integer count;
        Integer averageScore;
    }

    @Data
    public class MinitestInfo{
        Integer count;
        Integer averageScore;
    }

    @Data
    public class ContentsInfo{
        Integer videoViews;
        Integer bookViews;
        Integer wikiViews;
        Integer articleViews;
        Integer bookmarkCount;
    }

    //TODO LRSinfo

    UserInfo userInfo;
    DiagnosisInfo diagnosisInfo;
    MinitestInfo minitestInfo;
    ContentsInfo ContentsInfo;
}
