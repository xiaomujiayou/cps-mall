package com.xm.cpsmall.comm.api.config;

import com.xm.cpsmall.comm.api.client.MyTaobaoClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tb")
public class TbApiConfig {

    private String url;
    private String appKey;
    private String appSecret;
    private Long adzoneId;

    @Bean
    public MyTaobaoClient getTbHttpClient(){
        MyTaobaoClient myTaobaoClient = new MyTaobaoClient(url,appKey,appSecret);
        return myTaobaoClient;
    }
}

