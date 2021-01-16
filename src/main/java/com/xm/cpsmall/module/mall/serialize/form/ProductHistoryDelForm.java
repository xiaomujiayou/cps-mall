package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductHistoryDelForm {
    //历史记录Id
//    @NotNull(message = "id不能为空")
    private Integer id;
    //是否全部删除
    @NotNull(message = "isAll不能为空")
    private Boolean isAll;
}
