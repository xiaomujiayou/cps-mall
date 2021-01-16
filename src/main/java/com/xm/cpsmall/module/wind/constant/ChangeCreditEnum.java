package com.xm.cpsmall.module.wind.constant;

public enum  ChangeCreditEnum {
    LOGIN_DAY("每日登录",1,1,1,5,"每日登录信用奖励，每个月最多奖励5次"),
    ADD_PROXY("邀请奖励",2,1,2,5,"邀请好友奖励，每月最多奖励5次"),
    ORDER_COMPLETE("订单交易完成",3,5,0,0,"订单交易完成，不限次数"),
    ORDER_MALICE("恶意退单",4,-20,0,0,"恶意退单，信用降低"),
    ;

    ChangeCreditEnum(String name, Integer type, Integer scores, Integer limitByDay, Integer limitByMonth, String desc) {
        this.name = name;
        this.type = type;
        this.scores = scores;
        this.limitByDay = limitByDay;
        this.limitByMonth = limitByMonth;
        this.desc = desc;
    }

    private String name;
    private Integer type;
    private Integer scores;
    private Integer limitByDay;
    private Integer limitByMonth;
    private String desc;

    public Integer getLimitByDay() {
        return limitByDay;
    }

    public void setLimitByDay(Integer limitByDay) {
        this.limitByDay = limitByDay;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLimitByMonth() {
        return limitByMonth;
    }

    public void setLimitByMonth(Integer limitByMonth) {
        this.limitByMonth = limitByMonth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScores() {
        return scores;
    }

    public void setScores(Integer scores) {
        this.scores = scores;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
