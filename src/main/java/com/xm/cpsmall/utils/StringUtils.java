package com.xm.cpsmall.utils;

import com.xm.cpsmall.exception.GlobleException;

public class StringUtils {

    /**
     * 获取字符串最大字节长度
     * @param targetStr     ：目标文本
     * @param legnth        ：待取的最大长度
     * @return
     */
    public static String getMaxByteLength(String targetStr,Integer legnth){
        if(legnth < 3)
            throw new GlobleException("最小长度为3！");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < targetStr.length(); i++) {
            legnth -= new String(new char[]{targetStr.charAt(i)}).getBytes().length;
            if(legnth - 3 >= 0) {
                sb.append(targetStr.charAt(i));
            }else{
                sb.append("...");
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 取出中间文本
     * @param before
     * @param after
     * @return
     */
    public static String getBetweenStr(String text,String before,String after){
        Integer start = text.indexOf(before);
        if(start < 0)
            throw new RuntimeException("找不到前文本");
        start += before.length();
        Integer end = text.indexOf(after,start);
        if(end < 0)
            throw new RuntimeException("找不到后文本");
        return text.substring(start,end);
    }

    /**
     * 收缩文本长度到指定范围
     * @param targetStr
     * @param length
     * @return
     */
    public static String shrinkText(String targetStr,Integer length){
        if(targetStr == null || targetStr.length() <= length){
            return targetStr;
        }
        targetStr = targetStr.replaceAll("[【】\\（\\）\\《\\》\\——\\；\\，\\。\\“\\”\\<\\>]","");
        if(targetStr.length() <= length){
            return  targetStr;
        }
        targetStr = targetStr.replaceAll("[\\[\\]\\(\\)\\<\\>\\-\\;\\,\\.\\{\\}\\*\\$\\#]","");
        if(targetStr.length() <= length){
            return  targetStr;
        }
        targetStr = targetStr.replaceAll("\\s","");
        if(targetStr.length() <= length){
            return  targetStr;
        }
        return targetStr.substring(0,length);
    }

    public static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }




    public static void main(String[] args){
        System.out.println(isNumeric("56456"));
        System.out.println(isNumeric("66"));
        System.out.println(isNumeric("66a"));

    }
}
