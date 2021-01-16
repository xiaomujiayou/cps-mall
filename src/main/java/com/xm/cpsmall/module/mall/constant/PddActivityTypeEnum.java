package com.xm.cpsmall.module.mall.constant;

public enum PddActivityTypeEnum {
    NO(0,"无活动",true),
    SECKILL(1,"秒杀",true),
    LIMITED_DISCOUNT(3,"限量折扣",true),
    LIMITED_TIME_DISCOUNT(12,"限时折扣",true),
    PROMOTION(13,"大促活动",true),
    FAMOUS_DISCOUNT(14,"名品折扣",true),
    FAMOUS_CLEAR(15,"品牌清仓",true),
    FOOD_SUPERMARKET(16,"食品超市",true),
    LARKEY(17,"一元幸运团",true),
    AI_GUANG_JIE(18,"爱逛街",true),
    SHI_SHANG_CHUAN_dA(19,"时尚穿搭",true),
    MAN_TEAM(20,"男人帮",true),
    TEAN(21,"9块9",true),
    JING_JIA(22,"竞价活动",true),
    BANG_DAN(23,"榜单活动",true),
    XING_YUN(24,"幸运半价购",true),
    DING_JIN(25,"定金预售",true),
    REN_QI_GOU(26,"幸运人气购",true),
    TE_SHE(27,"特色主题活动",true),
    DUA_NMA(28,"断码清仓",true),
    YI_YUAN(29,"一元话费",true),
    DIAN_QI_CHENG(30,"电器城",true),
    MEI_RI(31,"每日好店",true),
    PIN_PAI(32,"品牌卡",true),
    DA_CU(101,"大促搜索池",true),
    FEN_LEI(102,"大促品类分会场",true)
    ;

    private Integer activityId;
    private String activityName;
    private Boolean show;

    PddActivityTypeEnum(Integer activityId, String activityName, Boolean show) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.show = show;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
