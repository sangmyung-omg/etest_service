package com.tmax.eTest.Contents.util;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {
  // private static CommonUtils common = new CommonUtils();

  // private CommonUtils() {
  // }

  // public static CommonUtils getCommon() {
  // return common;
  // }

  public static boolean stringNullCheck(String str) {
    return StringUtils.isBlank(str);
  }
}
