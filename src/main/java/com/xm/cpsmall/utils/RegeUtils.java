package com.xm.cpsmall.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegeUtils {


    /**
     * 匹配第一个结果
     * @param text
     * @param pattern
     * @return
     */
    public static String matchFrist(String text, Pattern pattern){
        if(text == null)
            throw new NullPointerException("text 不能为null");
        if(pattern == null)
            throw new NullPointerException("pattern 不能为null");
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            return matcher.group();
        }else
            return null;
    }
    public static String matchFrist(String text, String pattern){
        return matchFrist(text,Pattern.compile(pattern));
    }



    /**
     * 匹配一段文本中所有结果
     * @param text
     * @param pattern
     * @return
     */
    public static List<String> match(String text, Pattern pattern){
        if(text == null)
            throw new NullPointerException("text 不能为null");
        if(pattern == null)
            throw new NullPointerException("pattern 不能为null");
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            result.add(matcher.group());
        }
        return result;
    }
    public static List<String> match(String text, String pattern){
        return match(text,Pattern.compile(pattern));
    }
}
