package com.xm.cpsmall.module.pay.serialize.message;

import com.xm.cpsmall.module.cron.serialize.entity.ScBillPayEntity;
import lombok.Data;

/**
 *  订单返现消息
 */
@Data
public class EntPayMessage {
    private String retryTradeNo;
    private String desc;
    private String ip;
    private ScBillPayEntity scBillPayEntity;
}
