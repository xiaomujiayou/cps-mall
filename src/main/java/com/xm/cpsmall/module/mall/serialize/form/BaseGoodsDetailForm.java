package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.utils.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseGoodsDetailForm extends BaseForm {
    @NotNull
    private String goodsId;
}
