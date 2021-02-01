package com.xm.cpsmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@EnableScheduling
@EnableCaching
@ComponentScan("com.xm")
@MapperScan(basePackages = {"com.xm.*.module.*.mapper","com.xm.*.module.*.mapper.custom"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CpsMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpsMallApplication.class, args);
    }

}
