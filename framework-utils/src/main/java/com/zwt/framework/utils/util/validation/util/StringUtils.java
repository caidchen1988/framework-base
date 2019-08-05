package com.zwt.framework.utils.util.validation.util;

/**
 * @author zwt
 * @detail 部分工具类
 * @date 2019/8/5
 * @since 1.0
 */
public class StringUtils {

    /**
     * copy from org.apache.commons.lang3.StringUtils.isBlank
     * @param cs
     * @return
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
