package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.utils.form.ListForm;
import lombok.Data;

/**
 * 商品列表表单
 */
@Data
public class GoodsListForm extends ListForm {
    private String listType;
}
