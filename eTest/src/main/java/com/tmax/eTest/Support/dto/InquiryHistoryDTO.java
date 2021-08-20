package com.tmax.eTest.Support.dto;


import java.time.LocalDateTime;

public interface InquiryHistoryDTO {
    Long getINQUIRY_ID();
    String getINQUIRY_STATUS();
    String getINQUIRY_TITLE();
    String getINQUIRY_TYPE();
    LocalDateTime getCREATE_DATE();
}
