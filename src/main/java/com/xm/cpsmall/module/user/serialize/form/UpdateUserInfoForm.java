package com.xm.cpsmall.module.user.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateUserInfoForm {
    @NotNull(message = "昵称不能为空")
    private String nickName;
    @NotNull(message = "头像不能为空")
    private String avatarUrl;
    @NotNull(message = "性别不能为空")
    private Integer gender;
    private String language;
    private String city;
    private String province;
    private String country;
}
