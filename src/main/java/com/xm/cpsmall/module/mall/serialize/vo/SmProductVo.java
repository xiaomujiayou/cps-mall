package com.xm.cpsmall.module.mall.serialize.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xm.cpsmall.module.mall.serialize.ex.ActiveInfo;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SmProductVo {
    private Integer id;
    private Integer type;
    private String goodsId;
    private String goodsThumbnailUrl;
    private String goodsGalleryUrls;
    private String name;
    private String des;
    private Integer originalPrice;
    private Integer couponPrice;
    private Integer couponStartFee;
    private Integer sharePrice;
    private Integer buyPrice;
    private String mallId;
    private String mallName;
    private String salesTip;
    private Integer mallCps;
    private Integer hasCoupon;
    private String couponId;
    private String serviceTags;
    private String activityType;
    private java.util.Date createTime;
    //淘宝购买链接，用于生成淘口令
    private String tbBuyUrl;
    private String discount;
    private String commentsRate;
    private String brandLogoUrl;
    private String brandName;
    //是否支持急速返现
    private Boolean creditPay;
    //活动
    private List<ActiveInfo> activeInfos;

    public String getServiceTags() {
        if(creditPay == null || !creditPay)
            return serviceTags;
        if(serviceTags == null || StrUtil.isBlank(serviceTags))
            return "急速返现";
        List<String> list = CollUtil.newArrayList(serviceTags.split(","));
        list.add("急速返现");
        return list.stream().collect(Collectors.joining(","));

    }
}
