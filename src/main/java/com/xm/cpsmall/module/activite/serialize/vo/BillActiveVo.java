package com.xm.cpsmall.module.activite.serialize.vo;

import lombok.Data;

@Data
public class BillActiveVo {
    private Integer billId;
    private Integer money;
    private Integer state;
    private String activeName;
    private String attachDes;
    private String failReason;
    private String createTime;
}
