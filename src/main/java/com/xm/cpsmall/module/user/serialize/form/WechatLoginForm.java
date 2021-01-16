package com.xm.cpsmall.module.user.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WechatLoginForm {
    @NotEmpty(message = "code不能为空")
    private String code;
    private Integer shareUserId;
    private String from;
}
