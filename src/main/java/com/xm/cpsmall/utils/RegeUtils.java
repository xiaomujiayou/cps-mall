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
    
    public static void main(String[] args){
        String text = "09:13:17.873 [main] DEBUG com.xm.spiderclient.message.ProductUrlLitener - ProductUrlMessage(productUrl=https://detail.1688.com/offer/590343747611.html, pctUrl=https://detail.1688.com/offer/590343747611.html, pctUrl=https://detail.1688.com/offer/590343747611.html, productId=590343747611, productType=1)";
        Pattern productUrlPattern = Pattern.compile("(https://detail.1688.com/offer/)[0-9]+(.html)");
        System.out.println(match(text,productUrlPattern));
    }
}
