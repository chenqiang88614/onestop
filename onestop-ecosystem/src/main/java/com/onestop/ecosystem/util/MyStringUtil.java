package com.onestop.ecosystem.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @description: 自己实现的String转换类
 * @author: chenq
 * @date: 2019/9/6 10:26
 */
public class MyStringUtil {

    /**
     * @description 将输入的字符串转换成首字母小写
     * @param word 输入字符
     * @param upperOrLower true:大写，false:小写
     * @return
     */
    public static String fisrt2UpperOrLower(String word, boolean upperOrLower) {
        String s1 = word.substring(0, 1);
        if (upperOrLower) {
            s1 = s1.toUpperCase();
        } else {
            s1 = s1.toLowerCase();
        }
        s1 = s1.concat(word.substring(1));
        return s1;
    }
}
