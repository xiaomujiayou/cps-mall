package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.module.mall.serialize.vo.SmProductVo;
import com.xm.cpsmall.utils.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddUserHistoryForm extends BaseForm {
    @NotNull(message = "goodsId不能为空")
    private String goodsId;
    private Integer shareUserId;
    private SmProductVo smProductVo;
}
