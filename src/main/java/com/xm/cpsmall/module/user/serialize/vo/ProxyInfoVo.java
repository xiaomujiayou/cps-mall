package com.xm.cpsmall.module.user.serialize.vo;

import lombok.Data;

@Data
public class ProxyInfoVo {
    //直接锁定
    private Integer totalDirectProxy;
    //间接锁定
    private Integer totalIndirectProxy;
}
