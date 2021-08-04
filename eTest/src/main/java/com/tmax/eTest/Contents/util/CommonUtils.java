package com.tmax.eTest.Contents.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class CommonUtils {

  public static boolean stringNullCheck(String str) {
    return StringUtils.isBlank(str);
  }

  public static boolean objectNullcheck(Object obj) {
    return ObjectUtils.isEmpty(obj);
  }
}
