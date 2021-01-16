package com.xm.cpsmall.module.user.serialize.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVo implements Serializable {
    private Integer id;
    private String userSn;
    private String nickname;
    private String headImg;
    private String openId;
    private Integer sex;
    private Integer state;
    private Integer pid;
}
