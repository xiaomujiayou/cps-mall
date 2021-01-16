package com.xm.cpsmall.utils.number;

import cn.hutool.core.util.NumberUtil;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    private static Pattern MATCH_FLOAT_FROM_STR = Pattern.compile("[0-9.]+");

    /**
     * 千、万数值转换
     * @param strNum
     * @return
     */
    public static Integer strNumToInt(String strNum){
        if(strNum == null || "".equals(strNum)) {
            return null;
        }
        if(strNum.contains("万")){
            return (int)(Float.parseFloat(strNum.replace("万","").trim()) * 10000);
        }else if(strNum.contains("千")){
            return (int)(Float.parseFloat(strNum.replace("千","").trim()) * 1000);
        }else {
            return Integer.valueOf(strNum.trim());
        }
    }

    /**
     * 从字符串中匹配double
     * @return
     */
    public static float matchFloatFromStr(String str){

        Matcher matcher = MATCH_FLOAT_FROM_STR.matcher(str);
        if(!matcher.find()) {
            return 0f;
        }else {
            return Float.valueOf(matcher.group());
        }
    }

    /**
     * 取区间随机数(包含边界值)
     * @param min
     * @param max
     * @return
     */
    public static Integer getRandom(int min, int max){
        if(min == max)
            return max;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 分转元，且保留两位小数
     * @return
     */
    public static String fen2yuan(Integer money){
        if(money == null)
            return "0.00";
        return NumberUtil.roundStr(NumberUtil.div(Double.valueOf(money).doubleValue() ,100d),2);
    }
}
