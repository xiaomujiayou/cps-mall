package com.xm.cpsmall.module.mall.constant;

/**
 * 商品类型
 */
public enum  PlatformTypeEnum {
    PDD("PDD",1,"拼多多","pdd"),
    JD("JD",2,"京东","jd"),
    TB("TB",3,"淘宝","tb"),
    MGJ("MGJ",4,"蘑菇街","mgj"),
    WPH("WPH",5,"唯品会","wph");

    private String name;
    private Integer type;
    private String desc;
    private String servicePrefix;

    PlatformTypeEnum(String name, Integer type, String desc, String servicePrefix) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.servicePrefix = servicePrefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServicePrefix() {
        return servicePrefix;
    }

    public void setServicePrefix(String servicePrefix) {
        this.servicePrefix = servicePrefix;
    }
}
