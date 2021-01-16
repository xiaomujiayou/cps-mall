package com.xm.cpsmall.module.pay.serialize.message;

import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import lombok.Data;

@Data
public class ActiveAutoEntPayMessage {
    private String retryTradeNo;
    private String desc;
    private String ip;
    private Integer userId;
    private SaBillEntity saBillEntity;
}
