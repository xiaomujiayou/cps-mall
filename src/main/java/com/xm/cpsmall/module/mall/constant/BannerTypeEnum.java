package com.xm.cpsmall.module.mall.constant;

public enum  BannerTypeEnum {
    HOME("首页轮播图",1),
    HOME_SLIDE_MUEN("首页首页滑动菜单",2),
    ;

    private String name;
    private Integer type;

    BannerTypeEnum(String name, Integer type) {
        this.name = name;
        this.type = type;
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
}
