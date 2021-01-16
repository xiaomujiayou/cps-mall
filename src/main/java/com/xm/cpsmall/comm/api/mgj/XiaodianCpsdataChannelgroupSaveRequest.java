package com.xm.cpsmall.comm.api.mgj;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.mogujie.openapi.exceptions.ApiCheckException;
import com.mogujie.openapi.request.BaseMgjRequest;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class XiaodianCpsdataChannelgroupSaveRequest extends BaseMgjRequest<XiaodianCpsdataChannelgroupSaveResponse> {

    private String name;

    public XiaodianCpsdataChannelgroupSaveRequest(String name) {
        this.name = name;
    }

    @Override
    public String getMethodName() {
        return "xiaodian.cpsdata.channelgroup.save";
    }

    @Override
    public Map<String, String> getTextParams() {
        Map txtParams = new HashMap();
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        txtParams.put("CpsChannelGroupParam", JSON.toJSONString(MapUtil.builder().put("name",name).build()));
        return txtParams;
    }

    @Override
    public Class getResponseClass() {
        return XiaodianCpsdataChannelgroupSaveResponse.class;
    }

    @Override
    public void check() throws ApiCheckException {

    }
}
