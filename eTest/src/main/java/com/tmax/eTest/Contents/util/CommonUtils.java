package com.tmax.eTest.Contents.util;

import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component(value = "CommonUtils")
public class CommonUtils {

  public boolean stringNullCheck(String str) {
    return StringUtils.isBlank(str);
  }

  public boolean objectNullcheck(Object obj) {
    return ObjectUtils.isEmpty(obj);
  }

  public long zeroIfNull(Long num) {
    return Optional.ofNullable(num).orElse(0L);
  }

  public int zeroIfNull(Integer num) {
    return Optional.ofNullable(num).orElse(0);
  }
}
