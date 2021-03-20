//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xm.cpsmall.component.sdk.pdd;

import com.pdd.pop.ext.fasterxml.jackson.annotation.JsonProperty;
import com.pdd.pop.sdk.http.HttpMethod;
import com.pdd.pop.sdk.http.PopBaseHttpRequest;

import java.util.Map;

public class PddDdkThemeListGetRequest extends PopBaseHttpRequest<PddDdkThemeListGetResponse> {
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("page_size")
    private Integer pageSize;

    public PddDdkThemeListGetRequest() {
    }

    public String getVersion() {
        return "V1";
    }

    public String getDataType() {
        return "JSON";
    }

    public Integer getPlatform() {
        return 0;
    }

    public String getType() {
        return "pdd.ddk.theme.list.get";
    }

    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    public Class<PddDdkThemeListGetResponse> getResponseClass() {
        return PddDdkThemeListGetResponse.class;
    }

    protected void setUserParams(Map<String, String> params) {
        this.setUserParam(params, "page", this.page);
        this.setUserParam(params, "page_size", this.pageSize);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
