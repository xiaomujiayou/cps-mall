package com.xm.cpsmall.module.user.serialize.form;

import com.xm.cpsmall.utils.form.ListForm;
import lombok.Data;

@Data
public class GetOrderForm extends ListForm {
    private Integer state;
    //订单类型(1:自购订单,2:分享订单)
    private Integer type;
}
