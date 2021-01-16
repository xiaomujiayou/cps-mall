package com.xm.cpsmall.module.user.serialize.bo;

import lombok.Data;

@Data
public class UserProfitBo {
    //领取优惠券总额
    private Integer totalCoupon;
    //历史返现总额(已返)
    private Integer totalCommission;
    //当天返现总额(新成交)
    private Integer todayProfit;
    //当天返现总额(新成交)
    private Integer totalProfit;
    //待发放收益
    private Integer waitProfit;
    //准备发放
    private Integer readyProfit;
    //总消费
    private Integer totalConsumption;
    //分享订单总额(已下单付款)
    private Integer totalShare;
    //锁定用户
    private Integer totalProxyUser;

}
