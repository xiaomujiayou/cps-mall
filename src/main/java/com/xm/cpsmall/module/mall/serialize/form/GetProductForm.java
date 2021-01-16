package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetProductForm {
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
    //用户商品类型
    @NotNull(message = "suProductType不能为空")
    private Integer suProductType;
}
