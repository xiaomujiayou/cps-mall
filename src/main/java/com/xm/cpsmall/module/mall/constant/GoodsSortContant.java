package com.xm.cpsmall.module.mall.constant;

/**
 * 商品排序
 *  0-综合排序;
 *  1-按佣金比率升序;
 *  2-按佣金比例降序;
 *  3-按价格升序;
 *  4-按价格降序;
 *  5-按销量升序;
 *  6-按销量降序;
 *  7-优惠券金额排序升序;
 *  8-优惠券金额排序降序;
 *  9-券后价升序排序;
 *  10-券后价降序排序;
 *  13-按佣金金额升序排序;
 *  14-按佣金金额降序排序;
 *
 */
public class GoodsSortContant {
    public static final int DEFAULT = 0;
    public static final int PROMOTION_RATE_ASC = 1;
    public static final int PROMOTION_RATE_DESC = 2;
    public static final int PRICE_ASC = 3;
    public static final int PRICE_DESC = 4;
    public static final int SALES_ASC = 5;
    public static final int SALES_DESC = 6;
    public static final int COUPON_ASC = 7;
    public static final int COUPON_DESC = 8;
    public static final int FINAL_PRICE_ASC = 9;
    public static final int FINAL_PRICE_DESC = 10;

    public static final int PROMOTION_PRICE_ASC = 13;
    public static final int PROMOTION_PRICE_DESC = 14;
}
