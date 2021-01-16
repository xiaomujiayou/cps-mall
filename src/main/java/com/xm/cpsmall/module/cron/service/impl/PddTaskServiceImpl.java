package com.xm.cpsmall.module.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkOrderDetailGetRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkOrderListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkOrderDetailGetResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkOrderListIncrementGetResponse;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.cron.serialize.bo.OrderWithResBo;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("pddTaskService")
public class PddTaskServiceImpl extends AbsTask {

    /**
     * 更新四个小时前的订单（单位：秒）
     */
    private static final int UPDATE_BEFORE = 60 * 60 * 4;
    private static final int PAGE_SIZE = 40;

    @Autowired
    private PopHttpClient popHttpClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void start() {
        log.debug("拼多多 订单同步开始");
        Date endTime = null;
        try {
            endTime = DateUtil.dateNew(getTime());
            Date startTime = DateUtil.offset(endTime, DateField.SECOND,-UPDATE_BEFORE);
            processOrders(startTime,endTime,1,PAGE_SIZE);
            log.debug("拼多多 订单同步结束");
        } catch (Exception e) {
            log.error("拼多多 系统时间获取失败");
        }
    }

    @Override
    protected PlatformTypeEnum getPlatform() {
        return PlatformTypeEnum.PDD;
    }

    @Override
    public PageBean<OrderWithResBo> getOrderByIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) throws Exception {
        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
        request.setStartUpdateTime(startUpdateTime.getTime()/1000);
        request.setEndUpdateTime(endUpdateTime.getTime()/1000);
        request.setPage(pageNum);
        request.setPageSize(pageSize);
        request.setReturnCount(true);
        PddDdkOrderListIncrementGetResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem> listItem = response.getOrderListGetResponse().getOrderList();
        List<OrderWithResBo> orderEntities = null;
        if(listItem != null) {
            orderEntities = listItem.stream().map(o -> {
                return convertOrder(o);
            }).collect(Collectors.toList());
        }
        PageBean<OrderWithResBo> pageBean = new PageBean<>(orderEntities);
        pageBean.setList(orderEntities);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        pageBean.setTotal(response.getOrderListGetResponse().getTotalCount());
        return pageBean;
    }

    @Override
    public List<OrderWithResBo> getOrderByNum(String orderNum) throws Exception {
        PddDdkOrderDetailGetRequest request = new PddDdkOrderDetailGetRequest();
        request.setOrderSn(orderNum);
        PddDdkOrderDetailGetResponse response = popHttpClient.syncInvoke(request);
        if(response.getOrderDetailResponse() == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"无效订单号");
        OrderWithResBo orderEntity = convertOrderDetail(response.getOrderDetailResponse());
//        rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,orderEntity);
        return CollUtil.newArrayList(orderEntity);
    }


    private OrderWithResBo convertOrderDetail(PddDdkOrderDetailGetResponse.OrderDetailResponse item){

        Map<String,Object> stateMap = OrderStateParseUtil.parse(PlatformTypeConstant.PDD,item.getOrderStatus().toString());
        ScOrderStateRecordEntity stateRecordEntity = new ScOrderStateRecordEntity();
        stateRecordEntity.setOrderSn(item.getOrderSn());
        stateRecordEntity.setOrderSubSn(item.getOrderSn() + "-" + item.getGoodsId().toString());
        stateRecordEntity.setPlatformType(PlatformTypeConstant.PDD);
        stateRecordEntity.setOriginState(item.getOrderStatus().toString());
        stateRecordEntity.setOriginStateDes((String) stateMap.get(OrderStateParseUtil.DES));
        stateRecordEntity.setState((Integer) stateMap.get(OrderStateParseUtil.STATE));
        stateRecordEntity.setRes(JSON.toJSONString(item));


        SuOrderEntity orderEntity = new SuOrderEntity();
        orderEntity.setOrderSn(stateRecordEntity.getOrderSn());
        orderEntity.setOrderSubSn(stateRecordEntity.getOrderSubSn());
        orderEntity.setProductId(item.getGoodsId().toString());
        orderEntity.setProductName(item.getGoodsName());
        orderEntity.setImgUrl(item.getGoodsThumbnailUrl());
        orderEntity.setPlatformType(PlatformTypeConstant.PDD);
        orderEntity.setState(stateRecordEntity.getState());
//        orderEntity.setFailReason(item);
        orderEntity.setPId(item.getPid());
        orderEntity.setOriginalPrice(item.getGoodsPrice().intValue());
        orderEntity.setQuantity(item.getGoodsQuantity().intValue());
        orderEntity.setAmount(item.getOrderAmount().intValue());
        orderEntity.setPromotionRate(item.getPromotionRate().intValue() * 10);
        orderEntity.setPromotionAmount(item.getPromotionAmount().intValue());
        orderEntity.setType(item.getType());
        orderEntity.setCustomParameters(item.getCustomParameters());
        orderEntity.setOrderModifyAt(new Date(item.getOrderModifyAt() * 1000));

        OrderWithResBo orderWithResBo = new OrderWithResBo();
        orderWithResBo.setSuOrderEntity(orderEntity);
        orderWithResBo.setScOrderStateRecordEntity(stateRecordEntity);
        return orderWithResBo;
    }
    private OrderWithResBo convertOrder(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){

        Map<String,Object> stateMap = OrderStateParseUtil.parse(PlatformTypeConstant.PDD,item.getOrderStatus().toString());
        ScOrderStateRecordEntity stateRecordEntity = new ScOrderStateRecordEntity();
        stateRecordEntity.setOrderSn(item.getOrderSn());
        stateRecordEntity.setOrderSubSn(item.getOrderSn() + "-" + item.getGoodsId().toString());
        stateRecordEntity.setPlatformType(PlatformTypeConstant.PDD);
        stateRecordEntity.setOriginState(item.getOrderStatus().toString());
        stateRecordEntity.setOriginStateDes((String) stateMap.get(OrderStateParseUtil.DES));
        stateRecordEntity.setState((Integer) stateMap.get(OrderStateParseUtil.STATE));
        stateRecordEntity.setRes(JSON.toJSONString(item));


        SuOrderEntity orderEntity = new SuOrderEntity();
        orderEntity.setOrderSn(stateRecordEntity.getOrderSn());
        orderEntity.setOrderSubSn(stateRecordEntity.getOrderSubSn());
        orderEntity.setProductId(item.getGoodsId().toString());
        orderEntity.setProductName(item.getGoodsName());
        orderEntity.setImgUrl(item.getGoodsThumbnailUrl());
        orderEntity.setPlatformType(PlatformTypeConstant.PDD);
        orderEntity.setState(stateRecordEntity.getState());
        orderEntity.setFailReason(item.getFailReason());
        orderEntity.setPId(item.getPId());
        orderEntity.setOriginalPrice(item.getGoodsPrice().intValue());
        orderEntity.setQuantity(item.getGoodsQuantity().intValue());
        orderEntity.setAmount(item.getOrderAmount().intValue());
        orderEntity.setPromotionRate(item.getPromotionRate().intValue() * 10);
        orderEntity.setPromotionAmount(item.getPromotionAmount().intValue());
        orderEntity.setType(item.getType());
        orderEntity.setCustomParameters(item.getCustomParameters());
        orderEntity.setOrderModifyAt(new Date(item.getOrderModifyAt() * 1000));

        OrderWithResBo orderWithResBo = new OrderWithResBo();
        orderWithResBo.setSuOrderEntity(orderEntity);
        orderWithResBo.setScOrderStateRecordEntity(stateRecordEntity);
        return orderWithResBo;
    }
}
