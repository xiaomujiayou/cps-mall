package com.xm.cpsmall.module.mall.serialize.bo;

import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ProductCriteriaBo {
    private Integer userId;
    private Integer appType;
    private Integer pageNum;
    private Integer pageSize;

    private String pid;
    private String optionId;
    private String keyword;
    private Integer orderBy;
    private Integer minPrice;
    private Integer maxPrice;
    private List<String> goodsIdList;
    private List<Integer> activityTags;
    private Boolean hasCoupon;
    private String ip;

    //是否包邮
    private Boolean parcels;
    //是否为天猫
    private Boolean isTmall;
    //发货地
    private String location;


    //排序
    // 0-综合排序;
    // 1-按佣金比率升序;
    // 2-按佣金比例降序;
    // 3-按价格升序;
    // 4-按价格降序;
    // 5-按销量升序;
    // 6-按销量降序;
    // 7-优惠券金额排序升序;
    // 8-优惠券金额排序降序;
    // 9-券后价升序排序;
    // 10-券后价降序排序;
    public Object getOrderBy(Integer platformType) {
        Map<Integer,Object> pdd = new HashMap<>();
        pdd.put(0,0);
        pdd.put(1,1);
        pdd.put(2,2);
        pdd.put(3,3);
        pdd.put(4,4);
        pdd.put(5,5);
        pdd.put(6,6);
        pdd.put(7,7);
        pdd.put(8,8);
        pdd.put(9,9);
        pdd.put(10,10);
        pdd.put(13,13);
        pdd.put(14,14);

        Map<Integer,Integer> mgj = new HashMap<>();
        mgj.put(0,0);
        mgj.put(1,11);
        mgj.put(2,12);
        mgj.put(3,21);
        mgj.put(4,22);
        mgj.put(6,32);
        mgj.put(7,41);
        mgj.put(8,42);

        Map<Integer,Object> tb = new HashMap<>();
        tb.put(0,null);
        tb.put(1,"tk_rate_asc");
        tb.put(2,"tk_rate_des");
        tb.put(3,"price_asc");
        tb.put(4,"price_des");
        tb.put(5,"total_sales_asc");
        tb.put(6,"total_sales_des");
        tb.put(7,"tk_total_commi_asc");
        tb.put(8,"tk_total_commi_des");
        tb.put(9,"tk_rate_asc");
        tb.put(10,"tk_rate_des");

        Map<Integer,Object> wph = new HashMap<>();
        wph.put(0,null);
        wph.put(1,"COMM_RATE 0");
        wph.put(2,"COMM_RATE 1");
        wph.put(3,"PRICE 0");
        wph.put(4,"PRICE 1");
        wph.put(5,"SALES 0");
        wph.put(6,"SALES 1");
        wph.put(7,"COMMISSION 0");
        wph.put(8,"COMMISSION 1");
        wph.put(9,"DISCOUNT 0");
        wph.put(10,"DISCOUNT 1");


        Map<Integer,Object> jd = new HashMap<>();

        switch (platformType){
            case PlatformTypeConstant.PDD:
                return pdd.get(orderBy);
            case PlatformTypeConstant.MGJ:
                return mgj.get(orderBy);
            case PlatformTypeConstant.TB:
                return tb.get(orderBy);
            case PlatformTypeConstant.WPH:
                return wph.get(orderBy);
            case PlatformTypeConstant.JD:
                return jd.get(orderBy);
        }
        return orderBy;
    }
}
