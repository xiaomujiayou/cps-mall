package com.xm.cpsmall.module.pay.serialize.message;

import com.xm.cpsmall.module.activite.serialize.entity.SaCashOutRecordEntity;
import lombok.Data;

/**
 *  活动付款消息
 */
@Data
public class ActiveEntPayMessage {
    private String retryTradeNo;
    private String desc;
    private String ip;
    private Integer userId;
    private SaCashOutRecordEntity saCashOutRecordEntity;
}
