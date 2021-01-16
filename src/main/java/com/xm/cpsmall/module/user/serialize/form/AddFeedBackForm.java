package com.xm.cpsmall.module.user.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddFeedBackForm {

    @NotEmpty(message = "请填写问题描述")
    private String desc;

    private List<String> imgs;
}
