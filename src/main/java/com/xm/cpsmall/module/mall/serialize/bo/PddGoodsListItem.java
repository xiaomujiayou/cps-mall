package com.xm.cpsmall.module.mall.serialize.bo;

import lombok.Data;

import java.util.List;

@Data
public class PddGoodsListItem {
    private Long goodsId;
    private String goodsName;
    private String goodsDesc;
    private String goodsThumbnailUrl;
    private String goodsImageUrl;
    private List<String> goodsGalleryUrls;
    private Long minGroupPrice;
    private Long minNormalPrice;
    private String mallName;
    private Long optId;
    private String optName;
    private List<Integer> catIds;
    private Integer mallCps;
    private Boolean hasCoupon;
    private Long couponMinOrderAmount;
    private Long couponDiscount;
    private Long couponTotalQuantity;
    private Long couponRemainQuantity;
    private Long couponStartTime;
    private Long couponEndTime;
    private Long promotionRate;
    private Long goodsEvalCount;
    private String salesTip;
    private String descTxt;
    private String servTxt;
    private String lgstTxt;

}
