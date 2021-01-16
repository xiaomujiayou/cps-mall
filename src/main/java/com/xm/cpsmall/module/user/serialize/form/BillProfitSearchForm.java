package com.xm.cpsmall.module.user.serialize.form;

import lombok.Data;

import java.util.Date;

@Data
public class BillProfitSearchForm {
    private Integer state;
    private Date createStart;
    private Date createEnd;
}
