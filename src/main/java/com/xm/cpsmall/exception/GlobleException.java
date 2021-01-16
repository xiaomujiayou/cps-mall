package com.xm.cpsmall.exception;

import com.alibaba.fastjson.JSONObject;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.Data;

/**
 * 自定义全局异常（和FeignClientErrorDecoder配合使用）
 */
@Data
public class GlobleException extends RuntimeException{
    //错误类型
    private MsgEnum msgEnum;
    //自定义异常则为json序列化后的信息，其他异常则为原始异常的message信息。
    private String message;
    //错误信息(替换msgEnum中的msg)
    private String msg;
    //错误描述(补充错误信息，方便调试)
    private String[] stackInfo;

    /**
     * 包装系统异常
     * 由于feign阻断了原始异常信息，
     * 只能获取原始异常的message
     */
    public GlobleException(String message){
        this.message = message;
    }

    /**
     * 预定义异常
     * @param msgEnum
     */
    public GlobleException(MsgEnum msgEnum) {
        this(msgEnum,null,null);
    }

    /**
     * 替换MsgEnum中的msg
     * @param msgEnum
     * @param msg
     */
    public GlobleException(MsgEnum msgEnum, String msg) {
        this(msgEnum,msg,null);
    }

//    public GlobleException(MsgEnum msgEnum,String... stackInfo) {
//        this(msgEnum,null,stackInfo);
//    }

    public GlobleException(MsgEnum msgEnum, String msg, String... stackInfo) {
        this.msgEnum = msgEnum;
        this.msg = msg;
        this.stackInfo = stackInfo;
        JSONObject self = new JSONObject();
        self.put("message","");
        self.put("code",msgEnum.getCode());
        self.put("msg",this.msg);
        self.put("stackInfo",this.stackInfo);
        this.message = self.toJSONString();
    }
}
