package com.xm.cpsmall.utils.product;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

public class GenNumUtil {

    /**
     * 生成wx企业付款订单号
     * @return
     */
    public static String genWxEntPayOrderNum(){
        return DateUtil.format(new Date(),"yyyyMMddHHmmss")+ RandomUtil.randomNumbers(6);
    }

    /**
     * 生成订单号
     * @return
     */
    public static String genOrderNum(){
        return DateUtil.format(new Date(),"yyyyMMddHHmmss")+ RandomUtil.randomNumbers(3);
    }

    /**
     * 生成账单号
     * @return
     */
    public static String genBillNum(){
        return DateUtil.format(new Date(),"yyyyMMddHHmmss")+ RandomUtil.randomNumbers(5);
    }
    /**
     * 生成账单号
     * @return
     */
    public static String genActiveBillNum(){
        return DateUtil.format(new Date(),"yyyyMMddHHmmss")+ RandomUtil.randomNumbers(3);
    }

    public static void main(String[] args){
        System.out.println(genBillNum());
    }
}
