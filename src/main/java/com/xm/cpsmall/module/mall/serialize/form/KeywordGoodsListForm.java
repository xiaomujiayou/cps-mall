package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 关键字商品搜索表单
 */
@Data
public class KeywordGoodsListForm extends GoodsListForm {
    @NotNull(message = "请输入要查询的关键字")
    private String keywords;
    private Integer sort;
    private Integer minPrice;
    private Integer maxPrice;
    private Boolean hasCoupon;

    //是否包邮
    private Boolean parcels;
    //是否为天猫
    private Boolean isTmall;
    //发货地
    private String location;
}
