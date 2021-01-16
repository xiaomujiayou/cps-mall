package com.xm.cpsmall.module.cron.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mogujie.openapi.exceptions.ApiException;
import com.mogujie.openapi.response.MgjResponse;
import com.xm.cpsmall.comm.api.client.MyMogujieClient;
import com.xm.cpsmall.comm.api.mgj.OrderInfoQueryBean;
import com.xm.cpsmall.comm.api.mgj.XiaoDianCpsdataOrderListGetRequest;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.cron.mapper.ScMgjOrderRecordMapper;
import com.xm.cpsmall.module.cron.serialize.bo.OrderWithResBo;
import com.xm.cpsmall.module.cron.serialize.entity.ScMgjOrderRecordEntity;
import com.xm.cpsmall.module.cron.serialize.entity.ScOrderStateRecordEntity;
import com.xm.cpsmall.module.cron.utils.OrderStateParseUtil;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.constant.PlatformTypeEnum;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("mgjTaskService")
public class MgjTaskServiceImpl extends AbsTask {

    private static final int PAGE_SIZE = 20;

    @Autowired
    private MyMogujieClient myMogujieClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ScMgjOrderRecordMapper scMgjOrderRecordMapper;

    @Override
    public void start() {
        log.debug("蘑菇街 订单同步开始");
        Date startTime = DateUtil.yesterday();
        Date endTime = DateUtil.parse(DateUtil.today());
        processOrders(startTime,endTime,1,PAGE_SIZE);
        log.debug("蘑菇街 订单同步结束");
    }

    @Override
    protected PlatformTypeEnum getPlatform() {
        return PlatformTypeEnum.MGJ;
    }


    @Override
    public PageBean<OrderWithResBo> getOrderByIncrement(Date startTime, Date endTime, Integer pageNum, Integer pageSize) throws Exception {
        Integer startTimeInt = Integer.valueOf(DateUtil.format(startTime,"yyyyMMdd"));
        Integer endTimeInt = Integer.valueOf(DateUtil.format(endTime,"yyyyMMdd"));
        OrderInfoQueryBean queryBean = new OrderInfoQueryBean();
        queryBean.setStart(startTimeInt);
        queryBean.setEnd(endTimeInt);
        queryBean.setPage(pageNum);
        queryBean.setPagesize(pageSize);
        MgjResponse<String> res = myMogujieClient.execute(new XiaoDianCpsdataOrderListGetRequest(queryBean));
        if(res.getResult().getData() == null)
            return null;
        JSONObject jsonResult = JSON.parseObject(res.getResult().getData());
        JSONArray listItem = jsonResult.getJSONArray("orders");
        List<OrderWithResBo> orderEntities = new ArrayList<>();
        for (int i = 0; i < listItem.size(); i++) {
            orderEntities.addAll(convertOrder((JSONObject) listItem.get(i)));
        }
        orderEntities.stream().forEach(o->{
            ScMgjOrderRecordEntity scMgjOrderRecordEntity = new ScMgjOrderRecordEntity();
            scMgjOrderRecordEntity.setOrderSubSn(o.getSuOrderEntity().getOrderSubSn());
            int count = scMgjOrderRecordMapper.selectCount(scMgjOrderRecordEntity);
            if(count <= 0){
                scMgjOrderRecordEntity.setOrderSn(o.getSuOrderEntity().getOrderSn());
                scMgjOrderRecordEntity.setOrderSubSn(o.getSuOrderEntity().getOrderSubSn());
                scMgjOrderRecordEntity.setState(o.getSuOrderEntity().getState());
                scMgjOrderRecordEntity.setLastUpdate(new Date());
                scMgjOrderRecordEntity.setCreateTime(scMgjOrderRecordEntity.getLastUpdate());
                scMgjOrderRecordMapper.insertSelective(scMgjOrderRecordEntity);
            }
        });
        PageBean<OrderWithResBo> pageBean = new PageBean<>(orderEntities);
        pageBean.setList(orderEntities);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        pageBean.setTotal(jsonResult.getInteger("total"));
        return pageBean;
    }

    @Override
    public List<OrderWithResBo> getOrderByNum(String orderNum) throws ApiException {
        OrderInfoQueryBean queryBean = new OrderInfoQueryBean();
        queryBean.setOrderNo(Long.valueOf(orderNum));
        try {
            MgjResponse<String> res = myMogujieClient.execute(new XiaoDianCpsdataOrderListGetRequest(queryBean));
            if(res.getResult().getData() == null)
                throw new GlobleException(MsgEnum.ORDER_INVALID_ERROR,"蘑菇街 无效单号：",orderNum);
            JSONObject jsonResult = JSON.parseObject(res.getResult().getData());
            JSONArray listItem = jsonResult.getJSONArray("orders");
            List<OrderWithResBo> suOrderEntities = convertOrder(listItem.getJSONObject(0));
//            suOrderEntities.stream().forEach(o->{
//                rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,o);
//            });
            return suOrderEntities;
        }catch (ApiException e){
            throw new GlobleException(MsgEnum.ORDER_INVALID_ERROR,"蘑菇街 无效单号：",orderNum);
        }
    }


    private List<OrderWithResBo> convertOrder(JSONObject item){

        JSONArray products = item.getJSONArray("products");
        String cart = products.stream().map(o->{
            JSONObject goodsInfo = (JSONObject)o;
            return goodsInfo.getString("productNo");
        }).collect(Collectors.joining(","));
        List<OrderWithResBo> list = products.stream().map(o->{
            JSONObject goodsInfo = (JSONObject)o;
            Map<String,Object> stateMap = OrderStateParseUtil.parse(PlatformTypeConstant.MGJ,goodsInfo.getString("orderStatus").toString());
            ScOrderStateRecordEntity stateRecordEntity = new ScOrderStateRecordEntity();
            stateRecordEntity.setOrderSn(item.getString("orderNo"));
            stateRecordEntity.setOrderSubSn(goodsInfo.getString("subOrderNo"));
            stateRecordEntity.setPlatformType(PlatformTypeConstant.MGJ);
            stateRecordEntity.setOriginState(goodsInfo.getString("orderStatus"));
            stateRecordEntity.setOriginStateDes((String) stateMap.get(OrderStateParseUtil.DES));
            stateRecordEntity.setState((Integer) stateMap.get(OrderStateParseUtil.STATE));
            stateRecordEntity.setRes(item.toJSONString());

            SuOrderEntity orderEntity = new SuOrderEntity();
            orderEntity.setOrderSn(item.getString("orderNo"));
            orderEntity.setOrderSubSn(goodsInfo.getString("subOrderNo"));
            orderEntity.setProductId(goodsInfo.getString("productNo"));
            orderEntity.setProductName(goodsInfo.getString("name"));
            orderEntity.setImgUrl(goodsInfo.getString("mainImg"));
            orderEntity.setPlatformType(PlatformTypeConstant.MGJ);
            orderEntity.setState(stateRecordEntity.getState());
            orderEntity.setPId(item.getString("groupId"));
            orderEntity.setOriginalPrice(new Double(goodsInfo.getDouble("price") * 100d).intValue());
            orderEntity.setQuantity(goodsInfo.getInteger("amount"));
            orderEntity.setAmount(orderEntity.getOriginalPrice());
            orderEntity.setPromotionRate(new Double(Double.valueOf(goodsInfo.getString("commission").replace("%",""))*100d).intValue());
            orderEntity.setPromotionAmount(goodsInfo.getInteger("expense"));
            orderEntity.setType(0);
            orderEntity.setOrderModifyAt(new Date(item.getInteger("updateTime") * 1000));
            orderEntity.setCustomParameters(null);
            orderEntity.setCart(cart);

            OrderWithResBo orderWithResBo = new OrderWithResBo();
            orderWithResBo.setSuOrderEntity(orderEntity);
            orderWithResBo.setScOrderStateRecordEntity(stateRecordEntity);
            return orderWithResBo;
        }).collect(Collectors.toList());
        return list;
    }


}
