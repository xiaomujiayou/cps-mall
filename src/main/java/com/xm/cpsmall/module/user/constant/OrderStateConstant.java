package com.xm.cpsmall.module.user.constant;

public class OrderStateConstant {
    //无效订单
    public static final int FAIL = -1;
    //未支付
    public static final int UN_PAY = 0;
    //已支付
    public static final int PAY = 1;
    //确认收货
    public static final int CONFIRM_RECEIPT = 2;
    //已结算
    public static final int ALREADY_SETTLED = 3;
    //结算失败
    public static final int FAIL_SETTLED = 4;
}
