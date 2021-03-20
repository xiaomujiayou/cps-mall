//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xm.cpsmall.component.sdk.pdd;

import com.pdd.pop.ext.fasterxml.jackson.annotation.JsonProperty;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import java.util.List;

public class PddDdkThemeGoodsSearchResponse extends PopBaseHttpResponse {
    @JsonProperty("theme_list_get_response")
    private PddDdkThemeGoodsSearchResponse.ThemeListGetResponse themeListGetResponse;

    public PddDdkThemeGoodsSearchResponse() {
    }

    public PddDdkThemeGoodsSearchResponse.ThemeListGetResponse getThemeListGetResponse() {
        return this.themeListGetResponse;
    }

    public static class ThemeListGetResponse {
        @JsonProperty("goods_list")
        private List<PddDdkThemeGoodsSearchResponse.ThemeListGetResponseGoodsListItem> goodsList;
        @JsonProperty("total")
        private Long total;

        public ThemeListGetResponse() {
        }

        public List<PddDdkThemeGoodsSearchResponse.ThemeListGetResponseGoodsListItem> getGoodsList() {
            return this.goodsList;
        }

        public Long getTotal() {
            return this.total;
        }
    }

    public static class ThemeListGetResponseGoodsListItem {
        @JsonProperty("cat_ids")
        private List<Integer> catIds;
        @JsonProperty("coupon_discount")
        private Long couponDiscount;
        @JsonProperty("coupon_end_time")
        private Long couponEndTime;
        @JsonProperty("coupon_min_order_amount")
        private Long couponMinOrderAmount;
        @JsonProperty("coupon_remain_quantity")
        private Long couponRemainQuantity;
        @JsonProperty("coupon_start_time")
        private Long couponStartTime;
        @JsonProperty("coupon_total_quantity")
        private Long couponTotalQuantity;
        @JsonProperty("desc_txt")
        private String descTxt;
        @JsonProperty("goods_desc")
        private String goodsDesc;
        @JsonProperty("goods_gallery_urls")
        private List<String> goodsGalleryUrls;
        @JsonProperty("goods_id")
        private Long goodsId;
        @JsonProperty("goods_image_url")
        private String goodsImageUrl;
        @JsonProperty("goods_name")
        private String goodsName;
        @JsonProperty("goods_thumbnail_url")
        private String goodsThumbnailUrl;
        @JsonProperty("has_coupon")
        private Boolean hasCoupon;
        @JsonProperty("lgst_txt")
        private String lgstTxt;
        @JsonProperty("mall_name")
        private String mallName;
        @JsonProperty("min_group_price")
        private Long minGroupPrice;
        @JsonProperty("min_normal_price")
        private Long minNormalPrice;
        @JsonProperty("opt_id")
        private Long optId;
        @JsonProperty("opt_name")
        private String optName;
        @JsonProperty("promotion_rate")
        private Long promotionRate;
        @JsonProperty("sales_tip")
        private String salesTip;
        @JsonProperty("serv_txt")
        private String servTxt;

        public ThemeListGetResponseGoodsListItem() {
        }

        public List<Integer> getCatIds() {
            return this.catIds;
        }

        public Long getCouponDiscount() {
            return this.couponDiscount;
        }

        public Long getCouponEndTime() {
            return this.couponEndTime;
        }

        public Long getCouponMinOrderAmount() {
            return this.couponMinOrderAmount;
        }

        public Long getCouponRemainQuantity() {
            return this.couponRemainQuantity;
        }

        public Long getCouponStartTime() {
            return this.couponStartTime;
        }

        public Long getCouponTotalQuantity() {
            return this.couponTotalQuantity;
        }

        public String getDescTxt() {
            return this.descTxt;
        }

        public String getGoodsDesc() {
            return this.goodsDesc;
        }

        public List<String> getGoodsGalleryUrls() {
            return this.goodsGalleryUrls;
        }

        public Long getGoodsId() {
            return this.goodsId;
        }

        public String getGoodsImageUrl() {
            return this.goodsImageUrl;
        }

        public String getGoodsName() {
            return this.goodsName;
        }

        public String getGoodsThumbnailUrl() {
            return this.goodsThumbnailUrl;
        }

        public Boolean getHasCoupon() {
            return this.hasCoupon;
        }

        public String getLgstTxt() {
            return this.lgstTxt;
        }

        public String getMallName() {
            return this.mallName;
        }

        public Long getMinGroupPrice() {
            return this.minGroupPrice;
        }

        public Long getMinNormalPrice() {
            return this.minNormalPrice;
        }

        public Long getOptId() {
            return this.optId;
        }

        public String getOptName() {
            return this.optName;
        }

        public Long getPromotionRate() {
            return this.promotionRate;
        }

        public String getSalesTip() {
            return this.salesTip;
        }

        public String getServTxt() {
            return this.servTxt;
        }
    }
}
