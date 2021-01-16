package com.xm.cpsmall.comm.api.mgj;

import com.alibaba.fastjson.JSON;
import com.mogujie.openapi.exceptions.ApiCheckException;
import com.mogujie.openapi.request.BaseMgjRequest;

import java.util.HashMap;
import java.util.Map;

public class XiaoDianCpsdataOrderListGetRequest extends BaseMgjRequest<XiaoDianCpsdataOrderListGetResponse> {
    private OrderInfoQueryBean orderInfoQueryBean;

    public XiaoDianCpsdataOrderListGetRequest(OrderInfoQueryBean orderInfoQueryBean) {
        this.orderInfoQueryBean = orderInfoQueryBean;
    }

    @Override
    public String getMethodName() {
        return "xiaodian.cpsdata.order.list.get";
    }

    @Override
    public Map<String, String> getTextParams() {

        Map txtParams = new HashMap();
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        txtParams.put("orderInfoQuery", JSON.toJSONString(this.orderInfoQueryBean));
        return txtParams;
    }

    @Override
    public Class getResponseClass() {
        return XiaoDianCpsdataOrderListGetResponse.class;
    }

    @Override
    public void check() throws ApiCheckException {

    }
}
