package com.xm.cpsmall.comm.api.mgj;

import com.mogujie.openapi.exceptions.ApiCheckException;
import com.mogujie.openapi.request.BaseMgjRequest;

import java.util.HashMap;
import java.util.Map;

public class XiaodianCpsdataUrlShortenRequest extends BaseMgjRequest<XiaodianCpsdataUrlShortenResponse> {
    private String url;

    public XiaodianCpsdataUrlShortenRequest(String url) {
        this.url = url;
    }

    @Override
    public String getMethodName() {
        return "xiaodian.cpsdata.url.shorten";
    }

    @Override
    public Map<String, String> getTextParams() {
        Map txtParams = new HashMap();
        txtParams.put("url", url);
        return txtParams;
    }

    @Override
    public Class getResponseClass() {
        return XiaodianCpsdataUrlShortenResponse.class;
    }

    @Override
    public void check() throws ApiCheckException {

    }
}
