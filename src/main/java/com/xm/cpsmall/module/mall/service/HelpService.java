package com.xm.cpsmall.module.mall.service;

import com.xm.cpsmall.module.mall.serialize.entity.SmHelpEntity;

public interface HelpService {

    /**
     * 获取页面帮助信息
     * @param userId
     * @param url
     * @return
     */
    public SmHelpEntity getHelp(Integer userId, String url);
}
