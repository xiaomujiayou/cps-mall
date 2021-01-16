package com.xm.cpsmall.module.pay.serialize.vo;

import lombok.Data;

/**
 * 微信支付所需参数
 */
@Data
public class WxPayOrderResultVo {
    private String packageValue;
    private String timeStamp;
    private String nonceStr;
    private String paySign;
}
