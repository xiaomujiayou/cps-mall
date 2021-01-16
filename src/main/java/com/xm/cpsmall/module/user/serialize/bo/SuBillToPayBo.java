package com.xm.cpsmall.module.user.serialize.bo;

import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import lombok.Data;

@Data
public class SuBillToPayBo extends SuBillEntity {
    private String openId;
    //客户端ip
    private String clientIp;
}
