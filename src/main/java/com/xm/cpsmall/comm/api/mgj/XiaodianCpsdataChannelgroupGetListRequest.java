package com.xm.cpsmall.comm.api.mgj;

import com.mogujie.openapi.exceptions.ApiCheckException;
import com.mogujie.openapi.request.BaseMgjRequest;

import java.util.HashMap;
import java.util.Map;

public class XiaodianCpsdataChannelgroupGetListRequest extends BaseMgjRequest<XiaodianCpsdataChannelgroupGetListResponse> {

    @Override
    public String getMethodName() {
        return "xiaodian.cpsdata.channelgroup.getList";
    }

    @Override
    public Map<String, String> getTextParams() {
        Map txtParams = new HashMap();
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    @Override
    public Class getResponseClass() {
        return XiaodianCpsdataChannelgroupGetListResponse.class;
    }

    @Override
    public void check() throws ApiCheckException {

    }
}
