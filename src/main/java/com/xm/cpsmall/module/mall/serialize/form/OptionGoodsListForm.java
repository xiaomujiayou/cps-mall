package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品列表表单
 */
@Data
public class OptionGoodsListForm extends GoodsListForm {
    @NotNull(message = "optionId 不能为空")
    private Integer optionId;
}
