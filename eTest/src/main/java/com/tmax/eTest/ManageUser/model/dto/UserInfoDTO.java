package com.tmax.eTest.ManageUser.model.dto;

import com.tmax.eTest.Auth.authenum.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    @Data
    @Builder
    public static class UserInfo{
        String userUuid;
        LocalDateTime createDate;
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
    @Builder
    public static class MinitestInfo{
        Integer count;
        Integer averageScore;
    }

    @Data
    @Builder
    public static class ContentsInfo{
        Integer videoViews;
        Integer bookViews;
        Integer wikiViews;
        Integer articleViews;
        Integer totalBookmarkCount;
        public ContentsInfo setTotalBookmarkCount(){
            this.totalBookmarkCount = this.videoViews + this.bookViews + this.wikiViews + this.articleViews;
            return this;
        }
    }

    @Data
    @Builder
    public static class LRSInfo{
        Integer totalVisitCount;
        String averageActiveTime;
        Integer noActivityDays;

    }



    //TODO LRSinfo

    UserInfo userInfo;
    DiagnosisInfo diagnosisInfo;
    MinitestInfo minitestInfo;
    ContentsInfo contentsInfo;
    LRSInfo lrsInfo;
}
