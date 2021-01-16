package com.xm.cpsmall.comm.api.mgj;

import com.alibaba.fastjson.JSON;
import com.mogujie.openapi.exceptions.ApiCheckException;
import com.mogujie.openapi.request.BaseMgjRequest;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class XiaoDianCpsdataPromItemGetRequest extends BaseMgjRequest<XiaoDianCpsdataPromItemGetResponse> {

    private PromInfoQueryBean promInfoQueryBean;

    public XiaoDianCpsdataPromItemGetRequest(PromInfoQueryBean promInfoQueryBean) {
        this.promInfoQueryBean = promInfoQueryBean;
    }

    @Override
    public String getMethodName() {
        return "xiaodian.cpsdata.promitem.get";
    }

    @Override
    public Map<String, String> getTextParams() {

        Map txtParams = new HashMap();
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        txtParams.put("promInfoQuery", JSON.toJSONString(this.promInfoQueryBean));
        return txtParams;
    }

    @Override
    public Class getResponseClass() {
        return XiaoDianCpsdataPromItemGetResponse.class;
    }

    @Override
    public void check() throws ApiCheckException {

    }
}
