package com.tmax.eTest.Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CMRespDto<T> {
    private int code; // 200 성공 500 실패
    private String message;
    private T data;
}
