package com.xm.cpsmall.module.lottery.serialize.vo;

import lombok.Data;

import java.util.List;

@Data
public class SlPropVo {

    /**
     * name : VIP会员
     * price : ￥10
     * has : false
     * specDes : 请选择购买时长
     * des : 畅想vip权益
     * disable : false
     * disableTips :
     * spec : [{"name":"7天","price":"5","des":"返5元","choose":true}]
     */
    private String name;
    private Integer price;
    private Boolean has;
    private String specDes;
    private String des;
    private String imgUrl;
    private List<SpecBean> spec;

    @Data
    public static class SpecBean {
        /**
         * name : 7天
         * price : 5
         * des : 返5元
         * choose : true
         */
        private Integer id;
        private String name;
        private Integer price;
        private Integer originalPrice;
        private String des;
        private Boolean choose;
        private Boolean disable;
        private String disableTips;
    }
}
