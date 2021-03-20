//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xm.cpsmall.component.sdk.pdd;

import com.pdd.pop.ext.fasterxml.jackson.annotation.JsonProperty;
import com.pdd.pop.sdk.http.HttpMethod;
import com.pdd.pop.sdk.http.PopBaseHttpRequest;

import java.util.Map;

public class PddDdkThemeGoodsSearchRequest extends PopBaseHttpRequest<PddDdkThemeGoodsSearchResponse> {
    @JsonProperty("theme_id")
    private Long themeId;

    public PddDdkThemeGoodsSearchRequest() {
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
        return "pdd.ddk.theme.goods.search";
    }

    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    public Class<PddDdkThemeGoodsSearchResponse> getResponseClass() {
        return PddDdkThemeGoodsSearchResponse.class;
    }

    protected void setUserParams(Map<String, String> params) {
        this.setUserParam(params, "theme_id", this.themeId);
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }
}
