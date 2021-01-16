package com.xm.cpsmall.module.wind.constant;

public enum  CreditConfigEnmu {

    DEFAULT_CREDIT_SCORES("default_credit_scores","新用户默认信用积分"),
    DEFAULT_CREDIT_UNBIND_DELAY("default_credit_unbind_delay","信用账单提前解绑时间（打款十天后无异常）"),
    DEFAULT_CREDIT_HAS_CONF_DESC("default_credit_has_conf_desc","信用及格提示"),
    DEFAULT_CREDIT_NO_CONF_DESC("default_credit_no_conf_desc","信用不及格提示"),
    GET_CREDIT_METHOD("get_credit_method","获取信用的方法"),
    ;

    CreditConfigEnmu(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
    private String desc;
    private String name;
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
