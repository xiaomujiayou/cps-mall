package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import java.util.List;

@Data
public class GoodsDetailsForm extends GoodsListForm {
    private List<String> goodsIds;
}
