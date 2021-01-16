package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.utils.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UrlParseForm extends BaseForm {
    @NotNull(message = "url 不能为空")
    private String url;
}
