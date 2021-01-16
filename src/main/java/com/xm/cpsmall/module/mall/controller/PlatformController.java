package com.xm.cpsmall.module.mall.controller;

import cn.hutool.core.map.MapUtil;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api-mall/platform")
public class PlatformController {

    @Autowired
    private MallConfigService mallConfigService;

    /**
     *  平台按钮配置
     * @param userId
     * @return
     */
    @GetMapping("/config")
    public Map<String,Object> config(@LoginUser Integer userId){
        Map<String,Object> result = new HashMap<>();
        String isOn = mallConfigService.getConfig(userId, ConfigEnmu.PLATFORM_CHOOSE_BTN, ConfigTypeConstant.PROXY_CONFIG).getVal();
        result.put("isOn",isOn.equals("1")?true:false);
        return result;
    }

    /**
     * 获取平台列表
     * @param userId
     * @return
     */
    @GetMapping
    public Object get(@LoginUser Integer userId){
        String[] platformNames = mallConfigService.getConfig(userId, ConfigEnmu.PLATFORM_NAME, ConfigTypeConstant.PROXY_CONFIG).getVal().split(",");
        String[] platformIds = mallConfigService.getConfig(userId, ConfigEnmu.PLATFORM_ID, ConfigTypeConstant.PROXY_CONFIG).getVal().split(",");
        List<Map<String,Object>> result = IntStream
                .range(0,platformIds.length)
                .boxed()
                .collect(Collectors
                        .toMap(i->platformIds[i],i->platformNames[i]))
                .entrySet()
                .stream()
                .map(o-> MapUtil.builder("type",(Object) Integer.valueOf(o.getKey()))
                        .put("name",o.getValue())
                        .build())
                .collect(Collectors.toList());

        return result;
    }

}
