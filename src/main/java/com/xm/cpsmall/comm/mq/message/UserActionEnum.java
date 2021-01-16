package com.xm.cpsmall.comm.mq.message;

import com.xm.cpsmall.comm.mq.message.impl.*;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;

/**
 * 用户动态信息
 * 站在用户的层面上考虑
 */
public enum UserActionEnum {

    //用户分享事件
    USER_FRIST_LOGIN(10000,"首次登录","api-user", UserFristLoginMessage.class,true),
    USER_ADD_PROXY(10001,"新增一个下级用户","api-user", UserAddProxyMessage.class,true),
    USER_SHARE_GOODS(10002,"分享商品被点击","api-mail", UserShareGoodsMessage.class,true),
    USER_CLICK_GOODS(10003,"用户点击一个商品","api-mail", UserClickGoodsMessage.class,true),
    USER_SHARE_APP(10004,"分享程序被点击","", SuUserEntity.class,false),
    USER_LOGIN(10005,"登录","api-user", UserLoginMessage.class,true),

    //用户订单事件
    ORDER_CREATE(10101,"用户下单","api-user", OrderCreateMessage.class,true),
    ORDER_STATE_CHANGE(10102,"用户订单状态变更","api-user", OrderStateChangeMessage.class,true),
    ORDER_SETTLEMENT_SUCESS(10103,"用户订单交易成功","api-user", OrderSettlementSucessMessage.class,true),
    ORDER_SETTLEMENT_FAIL(10104,"用户订单交易失败","api-user",OrderSettlementFailMessage.class,true),
    ORDER_COMMISSION_SUCESS(10105,"用户订单佣金结算成功","", OrderCommissionSucessMessage.class,false),
    ORDER_COMMISSION_FAIL(10106,"用户订单佣金结算失败","", OrderCommissionFailMessage.class,false),

    //用户账单事件,
    USER_BILL_CREATE(10301,"用户账单已创建","api-user", UserBillCreateMessage.class,true),
    USER_BILL_STATE_CHANGE(10302,"用户账单状态已改变","api-user", UserBillStateChangeMessage.class,true),
    USER_BILL_COMMISSION_SUCESS(10303,"用户账单结算成功(钱到位)","", UserBillCommissionSucessMessage.class,false),
    USER_BILL_COMMISSION_FAIL(10304,"用户账单结算失败(钱到位)","", UserBillCommissionFailSucessMessage.class,false),
    USER_PAYMENT_SUCESS(10305,"用户佣金发放成功","",UserPaymentSucessMessage.class,true),
    USER_CREDIT_PAY_BILL(10306,"用户信用预支一笔订单","",UserCreditPayBillCreateMessage.class,true),
    USER_CREDIT_BILL_COUNT_DOWN(10307,"用户信用预支开始倒计时","",UserCreditBillCountDownMessage.class,true),
    USER_BIND_CREDIT_BILL(10308,"用户信用绑定一笔账单","",UserBindCreditBillMessage.class,true),
    USER_UN_BIND_CREDIT_BILL(10309,"用户信用解绑一笔订单","",UserUnBindCreditBillMessage.class,true),
    USER_MALICE_CREDIT_BILL(10310,"用户恶意退款（收货返现后退款）","",UserMaliceCreditBillMessage.class,true),
    USER_CREDIT_CHANGE(10311,"用户信用变更","",UserCreditChangeMessage.class,true),

    //用户使用事件
    USER_OPEN_APP(10201,"用户打开程序","", UserOpenAppMessage.class,false),
    USER_SHOW_GOODS(10202,"用户浏览一个商品","", UserShowGoodsMessage.class,true),
    USER_SEARCH_GOODS(10203,"用户搜索一个商品","", UserSearchGoodsMessage.class,true),
    USER_SMART_SEARCH_GOODS(10203,"用户智能搜索一个商品","", UserSmartSearchGoodsMessage.class,true),


    //支付相关事件
    PAY_ORDER_CRREATE(10401,"支付订单创建","api-pay",PayOrderCreateMessage.class,true),
    PAY_ORDER_NOTIFY_SUCESS(10402,"支付订单付款成功消息","api-pay",PayOrderSucessMessage.class,true),


    //活动相关
    USER_ACTIVE_BILL_CREATE(10501,"用户活动账单创建","api-active",UserActiveBillCreateMessage.class,true),

    ;
    UserActionEnum(Integer type, String name, String from, Class messageType, boolean ready) {
        this.type = type;
        this.name = name;
        this.from = from;
        this.messageType = messageType;
        this.ready = ready;
    }

    //事件类型
    private Integer type;
    //事件名称
    private String name;
    //消息来源
    private String from;
    //消息类型
    private Class messageType;
    //消息是否编写就绪
    private boolean ready;

    public Class getMessageType() {
        return messageType;
    }

    public void setMessageType(Class messageType) {
        this.messageType = messageType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
