package com.xm.cpsmall.comm.api.mgj;

import lombok.Data;

@Data
public class WxCodeParamBean {
    //加密形式的商品id
    private String itemId;
    //加密形式的优惠券id
    private String promId;
    //加密形式的uid
    private String uid;
    //渠道id，可以不填，默认为0
    private String gid;
    //默认为true，表示需要生成小程序码
    private Boolean genWxcode;

    private String feedBack;
}
