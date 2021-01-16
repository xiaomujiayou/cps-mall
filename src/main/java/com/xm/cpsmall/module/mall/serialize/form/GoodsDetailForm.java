package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.utils.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品列表表单
 */
@Data
public class GoodsDetailForm extends BaseForm {
    @NotNull(message = "goodsId 不能为空")
    private String goodsId;
    private Integer shareUserId;
}
