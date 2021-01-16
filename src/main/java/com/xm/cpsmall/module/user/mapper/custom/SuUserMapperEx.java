package com.xm.cpsmall.module.user.mapper.custom;

import com.xm.cpsmall.module.user.serialize.dto.ProxyProfitDto;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.utils.MyMapper;

import java.util.List;

public interface SuUserMapperEx extends MyMapper<SuUserEntity> {

    /**
     * 获取代理收益详情
     * @param userId
     * @return
     */
    List<ProxyProfitDto> getProxyProfit(Integer userId, Integer billState, String orderBy, Integer start, Integer size);

    Integer getIndirectUserCount(Integer userId);
}
