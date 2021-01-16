package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品列表表单
 */
@Data
public class ThemeGoodsListForm extends GoodsListForm{
    @NotNull(message = "themeId 不能为空")
    private Integer themeId;
}
