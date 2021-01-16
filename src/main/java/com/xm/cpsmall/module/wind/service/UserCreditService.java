package com.xm.cpsmall.module.wind.service;


import com.xm.cpsmall.module.wind.constant.ChangeCreditEnum;

public interface UserCreditService {

    /**
     * 用户信用变动
     */
    public void changeCredit(Integer userId, ChangeCreditEnum changeCreditEnum, String attached);
}
