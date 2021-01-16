package com.xm.cpsmall.comm.api.config;

import com.xm.cpsmall.comm.api.client.MyMogujieClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Data
@Configuration
@ConfigurationProperties(prefix = "mgj")
public class MgjApiConfig {
    public static final String MGJ_API_TOKEN_REDIS_KEY = "mgj_api_token";
    public static final String MGJ_API_REFRESH_TOKEN_REDIS_KEY = "mgj_api_refresh_token";

    private String appKey;
    private String appSecret;
    private String url;
    private String redirectUri;
    private String uid;
    private String weAppId;

    @Bean
    public MyMogujieClient getMgjHttpClient(StringRedisTemplate stringRedisTemplate){
        MyMogujieClient mogujieClient = new MyMogujieClient(this,stringRedisTemplate);
        mogujieClient.setIgnoreSSLCheck(true);
        return mogujieClient;
    }

}

