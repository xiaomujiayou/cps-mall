package com.xm.cpsmall.utils.response;

import lombok.Data;

@Data
public class Msg<T> {

    private Integer code;
    private String msg;
    private T data;

    public Msg() {}

    public Msg(MsgEnum msgEnum, T data) {
        this.code = msgEnum.getCode();
        this.msg = msgEnum.getMsg();
        this.data = data;
    }

    public Msg(MsgEnum msgEnum, String errorMsg) {
        this.code = msgEnum.getCode();
        this.msg = errorMsg;
        this.data = null;
    }
    public Msg(MsgEnum msgEnum) {
        this.code = msgEnum.getCode();
        this.msg = msgEnum.getMsg();
        this.data = null;
    }
}
