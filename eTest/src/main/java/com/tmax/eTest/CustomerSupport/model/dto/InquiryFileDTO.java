package com.tmax.eTest.CustomerSupport.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InquiryFileDTO {
    Long id;
    String imageEncoding;
    String type;
    String name;
    Long size;
}
