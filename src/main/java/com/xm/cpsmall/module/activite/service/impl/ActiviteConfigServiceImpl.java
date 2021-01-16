package com.xm.cpsmall.module.activite.service.impl;

import com.xm.cpsmall.module.activite.mapper.SaConfigMapper;
import com.xm.cpsmall.module.activite.serialize.entity.SaConfigEntity;
import com.xm.cpsmall.module.activite.service.ActiviteConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("activiteConfigService")
public class ActiviteConfigServiceImpl implements ActiviteConfigService {

    @Autowired
    private SaConfigMapper saConfigMapper;

    @Override
    public SaConfigEntity getConfig(String name) {
        SaConfigEntity configEntity = new SaConfigEntity();
        configEntity.setName(name);
        return saConfigMapper.selectOne(configEntity);
    }
}
