package com.xm.cpsmall.module.user.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddSearchForm {
    @NotEmpty(message = "keyWords不能为空")
    private String keyWords;
}
