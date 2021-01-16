package com.xm.cpsmall.module.user.serialize.form;

import com.xm.cpsmall.utils.form.ListForm;
import lombok.Data;

import java.util.Date;

@Data
public class OrderSearchForm extends ListForm {
    private Integer id;
    private Integer userId;
    private Integer shareUserId;
    private String orderSn;
    private String orderSubSn;
    private String productId;
    private String productName;
    private Integer platformType;
    private Integer state;
    private Integer pId;
    private Integer promotionAmountMin;
    private Integer promotionAmountMax;
    private Integer formType;
    private Integer fromApp;
    private Date createStart;
    private Date createEnd;
}
