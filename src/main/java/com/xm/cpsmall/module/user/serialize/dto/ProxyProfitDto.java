package com.xm.cpsmall.module.user.serialize.dto;

import lombok.Data;

@Data
public class ProxyProfitDto {
    private Integer proxyUserId;
    private String proxyName;
    private String proxyHeadImg;
    private Integer proxyNum;
    private Integer proxyProfit;
    private String createTime;
}
