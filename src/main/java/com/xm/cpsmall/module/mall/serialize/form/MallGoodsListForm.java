package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.utils.form.ListForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MallGoodsListForm extends ListForm {
    @NotNull(message = "店铺ID")
    private String mallId;
}
