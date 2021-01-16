package com.xm.cpsmall.comm.api.mgj;

import com.alibaba.fastjson.JSON;
import com.mogujie.openapi.exceptions.ApiCheckException;
import com.mogujie.openapi.request.BaseMgjRequest;

import java.util.HashMap;
import java.util.Map;

public class XiaoDianCpsdataWxcodeGetRequest extends BaseMgjRequest<XiaoDianCpsdataPromItemGetResponse> {
    private WxCodeParamBean wxCodeParamBean;

    public XiaoDianCpsdataWxcodeGetRequest(WxCodeParamBean wxCodeParamBean) {
        this.wxCodeParamBean = wxCodeParamBean;
    }

    @Override
    public String getMethodName() {
        return "xiaodian.cpsdata.wxcode.get";
    }

    @Override
    public Map<String, String> getTextParams() {

        Map txtParams = new HashMap();
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        txtParams.put("wxcodeParam", JSON.toJSONString(this.wxCodeParamBean));
        return txtParams;
    }

    @Override
    public Class getResponseClass() {
        return XiaoDianCpsdataWxcodeGetResponse.class;
    }

    @Override
    public void check() throws ApiCheckException {

    }
}
