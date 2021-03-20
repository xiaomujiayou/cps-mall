//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xm.cpsmall.component.sdk.pdd;

import com.pdd.pop.ext.fasterxml.jackson.annotation.JsonProperty;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import java.util.List;

public class PddDdkThemeListGetResponse extends PopBaseHttpResponse {
    @JsonProperty("theme_list_get_response")
    private PddDdkThemeListGetResponse.ThemeListGetResponse themeListGetResponse;

    public PddDdkThemeListGetResponse() {
    }

    public PddDdkThemeListGetResponse.ThemeListGetResponse getThemeListGetResponse() {
        return this.themeListGetResponse;
    }

    public static class ThemeListGetResponse {
        @JsonProperty("theme_list")
        private List<PddDdkThemeListGetResponse.ThemeListGetResponseThemeListItem> themeList;
        @JsonProperty("total")
        private Integer total;

        public ThemeListGetResponse() {
        }

        public List<PddDdkThemeListGetResponse.ThemeListGetResponseThemeListItem> getThemeList() {
            return this.themeList;
        }

        public Integer getTotal() {
            return this.total;
        }
    }

    public static class ThemeListGetResponseThemeListItem {
        @JsonProperty("goods_num")
        private Long goodsNum;
        @JsonProperty("id")
        private Long id;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("name")
        private String name;

        public ThemeListGetResponseThemeListItem() {
        }

        public Long getGoodsNum() {
            return this.goodsNum;
        }

        public Long getId() {
            return this.id;
        }

        public String getImageUrl() {
            return this.imageUrl;
        }

        public String getName() {
            return this.name;
        }
    }
}
