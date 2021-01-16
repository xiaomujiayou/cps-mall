package com.xm.cpsmall.module.mall.service;

import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.serialize.entity.SmConfigEntity;

import java.util.List;

/**
 * 配置服务
 */
public interface MallConfigService {

    /**
     * 获取某种配置
     * 配置不存在则获取系统配置
     * @param configEnmu
     * @return
     */
    public SmConfigEntity getConfig(Integer userId, ConfigEnmu configEnmu, int configType);

    /**
     * 获取用户所有配置信息
     * 配置不存在则获取系统配置
     * @param userId
     * @return
     */
    public List<SmConfigEntity> getAllConfig(Integer userId, int configType);
}
