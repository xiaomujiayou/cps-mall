package com.xm.cpsmall.module.cron.utils;

import cn.hutool.core.map.MapBuilder;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;

import java.util.HashMap;
import java.util.Map;

public class OrderStateParseUtil {

    private static final Map<Integer,Map<String,Map<String,Object>>> ORDER_STATE_ALL = new HashMap<>();
    public static final String DES = "des";
    public static final String STATE = "state";

    static {
        //拼多多订单状态
        Map<String,Map<String,Object>> pddState = new HashMap<>();
        pddState.put("-1",MapBuilder.create(new HashMap<String,Object>()).put(DES,"未支付").put(STATE, OrderStateConstant.UN_PAY).build());
        pddState.put("0",MapBuilder.create(new HashMap<String,Object>()).put(DES,"已支付").put(STATE, OrderStateConstant.PAY).build());
        pddState.put("1",MapBuilder.create(new HashMap<String,Object>()).put(DES,"已成团").put(STATE,OrderStateConstant.PAY).build());
        pddState.put("2",MapBuilder.create(new HashMap<String,Object>()).put(DES,"确认收货").put(STATE,OrderStateConstant.CONFIRM_RECEIPT).build());
        pddState.put("3",MapBuilder.create(new HashMap<String,Object>()).put(DES,"审核成功").put(STATE,OrderStateConstant.ALREADY_SETTLED).build());
        pddState.put("4",MapBuilder.create(new HashMap<String,Object>()).put(DES,"审核失败").put(STATE,OrderStateConstant.FAIL_SETTLED).build());
        pddState.put("5",MapBuilder.create(new HashMap<String,Object>()).put(DES,"已经结算").put(STATE,OrderStateConstant.ALREADY_SETTLED).build());
        pddState.put("8",MapBuilder.create(new HashMap<String,Object>()).put(DES,"非多多进宝商品（无佣金订单）").put(STATE,OrderStateConstant.FAIL).build());
        ORDER_STATE_ALL.put(PlatformTypeConstant.PDD,pddState);

        //蘑菇街订单状态
        Map<String,Map<String,Object>> mgjState = new HashMap<>();
        mgjState.put("10000",MapBuilder.create(new HashMap<String,Object>()).put(DES,"未支付").put(STATE, OrderStateConstant.UN_PAY).build());
        mgjState.put("20000",MapBuilder.create(new HashMap<String,Object>()).put(DES,"已支付").put(STATE,OrderStateConstant.PAY).build());
        mgjState.put("30000",MapBuilder.create(new HashMap<String,Object>()).put(DES,"已退款").put(STATE,OrderStateConstant.FAIL_SETTLED).build());
        mgjState.put("40000",MapBuilder.create(new HashMap<String,Object>()).put(DES,"确认收货").put(STATE,OrderStateConstant.CONFIRM_RECEIPT).build());
        mgjState.put("45000",MapBuilder.create(new HashMap<String,Object>()).put(DES,"审核成功").put(STATE,OrderStateConstant.ALREADY_SETTLED).build());
        mgjState.put("90000",MapBuilder.create(new HashMap<String,Object>()).put(DES,"审核失败").put(STATE,OrderStateConstant.FAIL_SETTLED).build());
        mgjState.put("95000",MapBuilder.create(new HashMap<String,Object>()).put(DES,"审核失败").put(STATE,OrderStateConstant.ALREADY_SETTLED).build());
        ORDER_STATE_ALL.put(PlatformTypeConstant.MGJ,mgjState);

        //唯品会订单状态
        Map<String,Map<String,Object>> wphState = new HashMap<>();
        wphState.put("已下单",MapBuilder.create(new HashMap<String,Object>()).put(DES,"未支付").put(STATE, OrderStateConstant.UN_PAY).build());
        wphState.put("已付款",MapBuilder.create(new HashMap<String,Object>()).put(DES,"已支付").put(STATE,OrderStateConstant.PAY).build());
        wphState.put("已签收",MapBuilder.create(new HashMap<String,Object>()).put(DES,"确认收货").put(STATE,OrderStateConstant.CONFIRM_RECEIPT).build());
        wphState.put("待结算",MapBuilder.create(new HashMap<String,Object>()).put(DES,"确认收货").put(STATE,OrderStateConstant.CONFIRM_RECEIPT).build());
        wphState.put("已结算",MapBuilder.create(new HashMap<String,Object>()).put(DES,"审核成功").put(STATE,OrderStateConstant.ALREADY_SETTLED).build());
        wphState.put("已失效",MapBuilder.create(new HashMap<String,Object>()).put(DES,"审核失败").put(STATE,OrderStateConstant.FAIL_SETTLED).build());
        ORDER_STATE_ALL.put(PlatformTypeConstant.WPH,wphState);
    }

    public static Map<String, Object> parse(Integer platformType, String originState){
        return ORDER_STATE_ALL.get(platformType).get(originState);
    }

}
