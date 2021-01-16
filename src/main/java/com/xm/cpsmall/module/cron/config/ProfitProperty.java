package com.xm.cpsmall.module.cron.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "profit")
public class ProfitProperty {
    private Integer minMoney;
    private String ip;
    private Integer pageSize;
    private String payDesc;
}
