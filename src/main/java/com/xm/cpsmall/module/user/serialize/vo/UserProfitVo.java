package com.xm.cpsmall.module.user.serialize.vo;

import lombok.Data;

@Data
public class UserProfitVo {
    public UserProfitVo() {}
    public UserProfitVo(String name, String value, String url) {
        this.name = name;
        this.value = value;
        this.url = url;
        this.help = false;
        this.msg = "";
    }

    public UserProfitVo(String name, String value, String url, Boolean help, String msg) {
        this.name = name;
        this.value = value;
        this.url = url;
        this.help = help;
        this.msg = msg;
    }

    private String name;
    private String value;
    private String url;
    private Boolean help;
    private String msg;
}
