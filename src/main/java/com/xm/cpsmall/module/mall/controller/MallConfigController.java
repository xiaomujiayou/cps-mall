package com.xm.cpsmall.module.mall.controller;

import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.serialize.entity.SmConfigEntity;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.utils.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/api-mall/config")
public class MallConfigController  {

    @Autowired
    private MallConfigService mallConfigService;

    @GetMapping("/one")
    public SmConfigEntity getOneConfig(Integer userId, String configName, Integer configType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return mallConfigService.getConfig(userId, EnumUtils.getEnum(ConfigEnmu.class,"name",configName),configType);
    }
}
