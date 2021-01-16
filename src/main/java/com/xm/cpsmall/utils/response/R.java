package com.xm.cpsmall.utils.response;

public class R {
    public static <T> Msg<T> sucess(T data){
        return new Msg<T>(MsgEnum.SUCESS,data);
    }

    public static <T> Msg<T> sucess(){
        return sucess(null);
    }

    public static <T> Msg<T> error(MsgEnum msgEnum, T data){
        return new Msg<T>(msgEnum,data);
    }

    public static <T> Msg<T> error(MsgEnum msgEnum, String errorMsg){
        return new Msg<T>(msgEnum,errorMsg);
    }
    public static <T> Msg<T> error(MsgEnum msgEnum){
        return new Msg<T>(msgEnum);
    }
}
