package com.tmax.eTest.Contents.util;

import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class CommonUtils {

  public static boolean stringNullCheck(String str) {
    return StringUtils.isBlank(str);
  }

  public static boolean objectNullcheck(Object obj) {
    return ObjectUtils.isEmpty(obj);
  }

  public static long zeroIfNull(Long num) {
    return Optional.ofNullable(num).orElse(0L);
  }

  public static int zeroIfNull(Integer num) {
    return Optional.ofNullable(num).orElse(0);
  }
}
