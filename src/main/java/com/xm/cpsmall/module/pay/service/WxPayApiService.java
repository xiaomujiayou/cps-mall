package com.xm.cpsmall.module.pay.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxOrderNotifyEntity;
import com.xm.cpsmall.module.pay.serialize.message.ActiveAutoEntPayMessage;
import com.xm.cpsmall.module.pay.serialize.message.ActiveEntPayMessage;
import com.xm.cpsmall.module.pay.serialize.message.EntPayMessage;
import com.xm.cpsmall.module.pay.serialize.vo.WxPayOrderResultVo;
import com.xm.cpsmall.module.user.serialize.bo.SuBillToPayBo;

/**
 * 微信支付服务
 */
public interface WxPayApiService {

    /**
     * 收款
     * @param suBillToPayBo
     * @return
     */
    public WxPayOrderResultVo collection(SuBillToPayBo suBillToPayBo) throws WxPayException;

    /**
     * 订单返现付款
     * @param entPayMessage
     */
    public void payment(EntPayMessage entPayMessage) throws WxPayException;

    /**
     * 活动返现付款
     * @param activeEntPayMessage
     */
    public void paymentActive(ActiveEntPayMessage activeEntPayMessage);

    /**
     * 活动直接付款
     * @param activeAutoEntPayMessage
     */
    public void paymentActiveAuto(ActiveAutoEntPayMessage activeAutoEntPayMessage);

    /**
     * 微信支付成功回调
     * @param notifyResult
     */
    public void orderNotify(WxPayOrderNotifyResult notifyResult);

    /**
     * 微信支付成功处理流程
     * @param spWxOrderNotifyEntity
     */
    public void onPaySucess(SpWxOrderNotifyEntity spWxOrderNotifyEntity);


}
