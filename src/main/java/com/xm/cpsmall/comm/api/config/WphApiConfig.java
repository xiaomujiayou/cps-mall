package com.xm.cpsmall.comm.api.config;

import com.vip.osp.sdk.context.ClientInvocationContext;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wph")
public class WphApiConfig {
    private String appKey;
    private String appSecret;
    private String url;
    private String weAppId;

    /**
     * 初始化唯品会api参数
     */
    @Bean
    public ClientInvocationContext clientInvocationContext(){
        ClientInvocationContext instance = new ClientInvocationContext();
        instance.setAppKey(appKey);
        instance.setAppSecret(appSecret);
        instance.setAppURL(url);
        return instance;
    }
}

