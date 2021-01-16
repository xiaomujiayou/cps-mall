package com.xm.cpsmall.module.user.serialize.bo;

import lombok.Data;

@Data
public class OrderCustomParameters {
    private Integer uid;
    private Integer userId;
    private Integer shareUserId;
    private Integer fromApp;
    private String pid;
}
