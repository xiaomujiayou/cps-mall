package com.xm.cpsmall.module.user.serialize.form;

import lombok.Data;

@Data
public class GetUserInfoForm {
    private Integer shareUserId;
    private String from;
    private String openId;
    private String code;
    private String ip;
//    private Integer userId;
}
