package com.xm.cpsmall.comm.api.mgj;

import com.mogujie.openapi.exceptions.ApiCheckException;
import com.mogujie.openapi.request.BaseMgjRequest;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class XiaoDianCpsdataItemGetRequest extends BaseMgjRequest<XiaoDianCpsdataItemGetResponse> {

    private String url;

    public XiaoDianCpsdataItemGetRequest(String url) {
        this.url = url;
    }

    @Override
    public String getMethodName() {
        return "xiaodian.cpsdata.item.get";
    }

    @Override
    public Map<String, String> getTextParams() {

        Map txtParams = new HashMap();
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        txtParams.put("url",url);
        return txtParams;
    }

    @Override
    public Class getResponseClass() {
        return XiaoDianCpsdataItemGetResponse.class;
    }

    @Override
    public void check() throws ApiCheckException {

    }
}
