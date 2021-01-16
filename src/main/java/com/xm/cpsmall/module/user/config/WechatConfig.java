package com.xm.cpsmall.module.user.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.xm.cpsmall.module.user.prop.WechatProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatConfig {

    @Bean
    public WxMaService getWxMpService(WechatProp wechatProp){
        WxMaDefaultConfigImpl wxMpDefaultConfigImpl = new WxMaDefaultConfigImpl();
        wxMpDefaultConfigImpl.setAppid(wechatProp.getAppId());
        wxMpDefaultConfigImpl.setSecret(wechatProp.getSecret());
        wxMpDefaultConfigImpl.setToken(wechatProp.getToken());
        wxMpDefaultConfigImpl.setAesKey(wechatProp.getEncodingAESKey());
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMpDefaultConfigImpl);
        return wxMaService;
    }
}
