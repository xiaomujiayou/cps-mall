package com.xm.cpsmall.module.cron.serialize.form;

import lombok.Data;

import java.util.Date;

@Data
public class BillPayForm {
    private Integer userId;
    private Integer billId;
    private Integer state;
    private Date createStart;
    private Date createEnd;
}
