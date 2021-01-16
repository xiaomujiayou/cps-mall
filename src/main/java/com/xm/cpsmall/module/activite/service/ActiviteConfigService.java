package com.xm.cpsmall.module.activite.service;

import com.xm.cpsmall.module.activite.serialize.entity.SaConfigEntity;

public interface ActiviteConfigService {
    /**
     * 获取配置
     * @param name
     * @return
     */
    SaConfigEntity getConfig(String name);
}
