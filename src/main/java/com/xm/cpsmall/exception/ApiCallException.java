package com.xm.cpsmall.exception;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class ApiCallException extends RuntimeException {
    public ApiCallException(String plarform, String apiName, String errorCode, String errorMsg, Object req, Object res) {
        super( String.format("API 异常 所属平台：[%s] 接口名称：[%s] 错误代码：[%s] 错误信息：[%s] 请求数据：[%s] 响应数据：[%s]",
                                plarform,
                                apiName,
                                errorCode,
                                errorMsg,
                                JSON.toJSONString(req),
                                JSON.toJSONString(res)));
        this.plarform = plarform;
        this.apiName = apiName;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.req = req;
        this.res = res;

    }

    private String plarform;
    private String apiName;
    private String errorCode;
    private String errorMsg;
    private Object req;
    private Object res;
}
