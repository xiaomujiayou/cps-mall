package com.xm.cpsmall.module.user.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WechatProp {
    private String appId;
    private String secret;
    private String token;
    private String encodingAESKey;
}
