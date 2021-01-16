package com.xm.cpsmall.utils.number;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽奖工具
 */

public class LuckUtil {
    @Getter
    private Config[] configs = null;

    Map<Config,Integer> test = new HashMap<>();

    private LuckUtil() { }

    public static LuckUtil get(Config... configs){
        int total = 0;
        for (Config config : configs) {
            if(config.weight == null)
                continue;
            total += config.weight;
        }

        for (Config config : configs) {
            config.setRate(config.weight == null ? 0 : (config.weight * 10000) / total);
        }
        LuckUtil luckUtil = new LuckUtil();
        luckUtil.configs = configs;
        return luckUtil;
    }

    public int random(){
        int random = NumberUtil.generateRandomNumber(1,10000,1)[0];
        for (Config config : configs) {
            random -= config.getRate();
            if(random <= 0) {
                test.compute(config,(k,v)->{
                    if(v == null)
                        return 1;
                    return v + 1;
                });
                return NumberUtil.generateRandomNumber(config.min, config.max, 1)[0];
            }
        }
        return -1;
    }

    public static void main(String[] args){
        LuckUtil util = LuckUtil.get(new Config(30,40,70),new Config(40,60,30),new Config(70,100,5),new Config(100,200,1));
        for (int i = 0; i < 100; i++) {
            System.out.println(util.random());
        }
        System.out.println(JSON.toJSONString(util.test, SerializerFeature.PrettyFormat));
        int count = 0;
        for (Map.Entry<Config, Integer> configIntegerEntry : util.test.entrySet()) {
            count += configIntegerEntry.getValue();
        }
        System.out.println(count);
    }

    @Data
    public static class Config{
        private Integer min;
        private Integer max;
        private Integer weight;
        private Integer rate;

        public Config(Integer min, Integer max, Integer weight) {
            this.min = min;
            this.max = max;
            this.weight = weight;
        }
    }
}
