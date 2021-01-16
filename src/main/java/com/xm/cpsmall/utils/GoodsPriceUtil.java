package com.xm.cpsmall.utils;

import cn.hutool.core.util.NumberUtil;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;

public class GoodsPriceUtil {

    //佣金比率单位
    private final int RATE = 10000;

    private Integer platfromType;

    private GoodsPriceUtil(){}

    public static GoodsPriceUtil type(Integer platformType){
        GoodsPriceUtil goodsPriceUtil = new GoodsPriceUtil();
        goodsPriceUtil.platfromType = platformType;
        return goodsPriceUtil;
    }

    /**
     * 根据费率计算商品总佣金
     *
     * (原始价 - 代金卷) * 佣金比例
     *
     * @param originalPrice     :原始价格
     * @param couponPrice       :代金卷价格
     * @param profitRate        :佣金比例
     * @return
     */
    public Double calcProfit(Double originalPrice,Double couponPrice ,Double profitRate){
        return NumberUtil.div(NumberUtil.mul((Double)NumberUtil.sub(originalPrice,couponPrice),profitRate),RATE);
    }
    /**
     * 根据费率计算商品总佣金
     *
     * (原始价 - 代金卷) * 佣金比例
     *
     * @param smProductEntity     :要计算的商品
     * @return
     */
    public Double calcProfit(SmProductEntity smProductEntity){
        return NumberUtil.div(NumberUtil.mul(NumberUtil.sub(smProductEntity.getOriginalPrice(),smProductEntity.getCouponPrice()),smProductEntity.getPromotionRate()),RATE).doubleValue();
    }

    /**
     * 计算用户分享佣金
     * @param originalPrice
     * @param couponPrice
     * @param profitRate
     * @param userShareRate         :分享费率
     * @return
     */
    public Double calcUserShareProfit(Double originalPrice,Double couponPrice ,Double profitRate,Double userShareRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(originalPrice,couponPrice,profitRate),userShareRate),RATE);
    }
    /**
     * 计算用户分享佣金
     * @param profit                :商品总佣金
     * @param userShareRate         :分享费率
     * @return
     */
    public Double calcUserShareProfit(Double profit,Double userShareRate){
        return NumberUtil.div(NumberUtil.mul(profit,userShareRate),RATE);
    }

    /**
     * 计算用户分享佣金
     * @param smProductEntity       :商品
     * @param userShareRate         :分享费率
     * @return
     */
    public Double calcUserShareProfit(SmProductEntity smProductEntity,Double userShareRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(smProductEntity),userShareRate),RATE);
    }

    /**
     * 计算用户分享购买佣金
     * @param originalPrice
     * @param couponPrice
     * @param profitRate
     * @param userShareBuyRate         :分享费率
     * @return
     */
    public Double calcUserShareBuyProfit(Double originalPrice,Double couponPrice ,Double profitRate,Double userShareBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(originalPrice,couponPrice,profitRate),userShareBuyRate),RATE);
    }
    /**
     * 计算用户分享购买佣金
     * @param profit                :商品总佣金
     * @param userShareBuyRate         :分享费率
     * @return
     */
    public Double calcUserShareBuyProfit(Double profit,Double userShareBuyRate){
        return NumberUtil.div(NumberUtil.mul(profit,userShareBuyRate),RATE);
    }

    /**
     * 计算用户分享购买佣金
     * @param smProductEntity          :商品总佣金
     * @param userShareBuyRate         :分享费率
     * @return
     */
    public Double calcUserShareBuyProfit(SmProductEntity smProductEntity,Double userShareBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(smProductEntity),userShareBuyRate),RATE);
    }

    /**
     * 计算用户直接购买佣金
     * @param profit                :商品总佣金
     * @param userBuyRate           :购买费率
     * @return
     */
    public Double calcUserBuyProfit(Double profit,Double userBuyRate){
        return NumberUtil.div(NumberUtil.mul(profit,userBuyRate),RATE);
    }

    /**
     * 计算用户直接购买佣金
     * @param originalPrice
     * @param couponPrice
     * @param profitRate
     * @param userBuyRate         :购买费率
     * @return
     */
    public Double calcUserBuyProfit(Double originalPrice,Double couponPrice ,Double profitRate,Double userBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(originalPrice,couponPrice,profitRate),userBuyRate),RATE);
    }

    /**
     * 计算用户直接购买佣金
     * @param smProductEntity
     * @param userBuyRate         :购买费率
     * @return
     */
    public Double calcUserBuyProfit(SmProductEntity smProductEntity,Double userBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(smProductEntity),userBuyRate),RATE);
    }

}
