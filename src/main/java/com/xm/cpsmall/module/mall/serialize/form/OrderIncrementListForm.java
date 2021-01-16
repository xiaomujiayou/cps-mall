package com.xm.cpsmall.module.mall.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class OrderIncrementListForm {

    @NotNull(message = "startUpdateTime不能为空")
    private Date startUpdateTime;
    @NotNull(message = "endUpdateTime不能为空")
    private Date endUpdateTime;
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
