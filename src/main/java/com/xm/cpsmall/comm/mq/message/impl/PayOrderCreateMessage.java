package com.xm.cpsmall.comm.mq.message.impl;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.UserActionEnum;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxOrderInEntity;
import com.xm.cpsmall.module.pay.serialize.vo.WxPayOrderResultVo;
import com.xm.cpsmall.module.user.serialize.bo.SuBillToPayBo;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import lombok.Data;

@Data
public class PayOrderCreateMessage extends AbsUserActionMessage {

    public PayOrderCreateMessage() {}

    public PayOrderCreateMessage(Integer userId, SuBillEntity suBillEntity, SpWxOrderInEntity spWxOrderInEntity, SuBillToPayBo suBillToPayBo, WxPayOrderResultVo wxPayOrderResultVo) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.spWxOrderInEntity = spWxOrderInEntity;
        this.suBillToPayBo = suBillToPayBo;
        this.wxPayOrderResultVo = wxPayOrderResultVo;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.PAY_ORDER_CRREATE;
    private SuBillEntity suBillEntity;
    private SpWxOrderInEntity spWxOrderInEntity;
    private SuBillToPayBo suBillToPayBo;
    private WxPayOrderResultVo wxPayOrderResultVo;

}
