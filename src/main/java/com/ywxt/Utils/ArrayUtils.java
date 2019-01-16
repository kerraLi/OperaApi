package com.ywxt.Utils;

public class ArrayUtils {

    /**
     * 字符串数组中是否存在该字符串
     */
    public static boolean hasString(String[] str, String s) {
        /*此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串*/
        int i = str.length;
        while (i-- > 0) {
            if (str[i].equals(s)) {
                return true;
            }
        }
        return false;
    }
}
