package com.xm.cpsmall.module.mall.constant;

public enum  PddServiceTagEnum {
    RETURN_FREIGHT(3,"退货包运费",true),
    SEND_INSTALL(4,"送货入户并安装",true),
    SEND(5,"送货入户",true),
    BILL(6,"电子发票",true),
    BAD_FRUIT_COMPENSATION(9,"坏果包赔",true),
    QUICK_REFUND(11,"闪电退款",true),
    SEND_TIME_24(12,"24小时发货",false),
    SEND_TIME_48(13,"48小时发货",false),
    SHUN_FENG(17,"顺丰包邮",true),
    CHANGE_NO_REPAIR(18,"只换不修",true),
    GUARANTEE(19,"全国联保",true),
    BY_STAGES(20,"分期付款",true),
    FAST_REFUND(24,"急速退款",true),
    QUALITY_ASSURANCE(25,"品质保障",true),
    LACK_RETREAT(26,"缺重包退",true),
    SEND_SAME_DAY(27,"当日发货",true),
    CAN_CUSTOM(28,"可定制化",true),
    BOOKING_DELIVERY(29,"预约配送",true),
    QUALITY_BILL(1000001,"正品发票",true),
    SEND_INSTALL_NEW(1000002,"送货入户并安装",true)
    ;

    private Integer tagId;
    private String tagName;
    private Boolean show;

    PddServiceTagEnum(Integer tagId, String tagName, Boolean show) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.show = show;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
