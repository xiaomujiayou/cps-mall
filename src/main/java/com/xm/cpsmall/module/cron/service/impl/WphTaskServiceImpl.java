package com.xm.cpsmall.module.cron.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.vip.adp.api.open.service.*;
import com.vip.osp.sdk.context.ClientInvocationContext;
import com.vip.osp.sdk.exception.OspException;
import com.xm.cpsmall.comm.mq.message.config.OrderMqConfig;
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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("wphTaskService")
public class WphTaskServiceImpl extends AbsTask {

    /**
     * 更新四个小时前的订单（单位：秒）
     */
    private static final int UPDATE_BEFORE = 60 * 60 * 24;
    private static final int PAGE_SIZE = 20;

    @Autowired
    private ClientInvocationContext clientInvocationContext;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void start() {
        log.debug("唯品会 订单同步开始");
        Date endTime = null;
        try {
            endTime = DateUtil.dateNew(getTime());
            Date startTime = DateUtil.offset(endTime, DateField.SECOND,-UPDATE_BEFORE);
            processOrders(startTime,endTime,1,PAGE_SIZE);
            log.debug("唯品会 订单同步结束");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("唯品会 系统时间获取失败");
        }
    }

    @Override
    protected PlatformTypeEnum getPlatform() {
        return PlatformTypeEnum.WPH;
    }


    @Override
    public PageBean<OrderWithResBo> getOrderByIncrement(Date startTime, Date endTime, Integer pageNum, Integer pageSize) throws Exception {
        UnionOrderServiceHelper.UnionOrderServiceClient client = new UnionOrderServiceHelper.UnionOrderServiceClient();
        client.setClientInvocationContext(clientInvocationContext);
        OrderQueryModel request = new OrderQueryModel();
        request.setRequestId(UUID.randomUUID().toString());
        request.setUpdateTimeStart(startTime.getTime());
        request.setUpdateTimeEnd(endTime.getTime());
        request.setPage(pageNum);
        request.setPageSize(pageSize);
        OrderResponse response = client.orderList(request);
        if(response == null || response.getOrderInfoList() == null || response.getOrderInfoList().isEmpty())
            return null;
        List<OrderWithResBo> orderInfos = new ArrayList<>();
        for (OrderInfo orderInfo : response.getOrderInfoList()) {
            orderInfos.addAll(convertOrder(orderInfo));
        }
        PageBean<OrderWithResBo> pageBean = new PageBean<>();
        pageBean.setList(orderInfos);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        pageBean.setTotal(response.getTotal());
        return pageBean;
    }

    @Override
    public List<OrderWithResBo> getOrderByNum(String orderNum) throws OspException {
        UnionOrderServiceHelper.UnionOrderServiceClient client = new UnionOrderServiceHelper.UnionOrderServiceClient();
        client.setClientInvocationContext(clientInvocationContext);
        OrderQueryModel request = new OrderQueryModel();
        request.setRequestId(UUID.randomUUID().toString());
        request.setOrderSnList(Arrays.asList(orderNum));
        OrderResponse response = client.orderList(request);
        if(response == null || response.getOrderInfoList() == null || response.getOrderInfoList().isEmpty())
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR,"唯品会 没有找到该单号对应的订单");
        List<OrderWithResBo> suOrderEntities = null;
        for (OrderInfo orderInfo : response.getOrderInfoList()) {
            suOrderEntities = convertOrder(orderInfo);
//            for (OrderWithResBo suOrderEntity : suOrderEntities) {
//                rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
//            }
        }
        return suOrderEntities;
    }

    @Override
    public void test(String orderJsonStr) {
        OrderInfo orderInfo = JSON.parseObject(orderJsonStr,OrderInfo.class);
        System.out.println(JSON.toJSONString(orderInfo));
        List<OrderWithResBo> orderWithResBos = convertOrder(orderInfo);
        System.out.println(JSON.toJSONString(orderWithResBos));
        for (OrderWithResBo orderWithResBo : orderWithResBos) {
            System.out.println(JSON.toJSONString(orderWithResBo));
            rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,orderWithResBo.getSuOrderEntity());
        }
    }

    private List<OrderWithResBo> convertOrder(OrderInfo orderInfo){
        String cart = orderInfo.getDetailList().stream().map(OrderDetailInfo::getGoodsId).collect(Collectors.joining(","));
        int i = 0;
        Map<String,Object> stateMap = OrderStateParseUtil.parse(PlatformTypeConstant.WPH,orderInfo.getOrderSubStatusName());
        List<OrderWithResBo> result = new ArrayList<>();
        for (OrderDetailInfo orderDetailInfo : orderInfo.getDetailList()) {
            ScOrderStateRecordEntity stateRecordEntity = new ScOrderStateRecordEntity();
            stateRecordEntity.setOrderSn(orderInfo.getOrderSn());
            stateRecordEntity.setOrderSubSn(orderInfo.getOrderSn() + "-" + orderDetailInfo.getGoodsId());
            stateRecordEntity.setPlatformType(PlatformTypeConstant.WPH);
            stateRecordEntity.setOriginState(orderInfo.getOrderSubStatusName());
            stateRecordEntity.setOriginStateDes((String) stateMap.get(OrderStateParseUtil.DES));
            stateRecordEntity.setState((Integer) stateMap.get(OrderStateParseUtil.STATE));
            stateRecordEntity.setRes(JSON.toJSONString(orderInfo));

            SuOrderEntity orderEntity = new SuOrderEntity();
            orderEntity.setOrderSn(orderInfo.getOrderSn());
            orderEntity.setOrderSubSn(stateRecordEntity.getOrderSubSn());
            orderEntity.setProductId(orderDetailInfo.getGoodsId());
            orderEntity.setProductName(orderDetailInfo.getGoodsName());
            orderEntity.setImgUrl(orderDetailInfo.getGoodsThumb());
            orderEntity.setPlatformType(PlatformTypeConstant.WPH);
            orderEntity.setState(stateRecordEntity.getState());
            orderEntity.setPId(orderInfo.getPid());
            orderEntity.setOriginalPrice(Double.valueOf(NumberUtil.mul(Double.valueOf(orderDetailInfo.getCommissionTotalCost()),Double.valueOf(100d))).intValue());
            orderEntity.setQuantity(orderDetailInfo.getGoodsCount());
            orderEntity.setAmount(orderEntity.getOriginalPrice());
            orderEntity.setPromotionRate(Double.valueOf(NumberUtil.mul(Double.valueOf(orderDetailInfo.getCommissionRate()),Double.valueOf(100d))).intValue());
            orderEntity.setPromotionAmount(Double.valueOf(NumberUtil.mul(Double.valueOf(orderDetailInfo.getCommission()),Double.valueOf(100d))).intValue());
            orderEntity.setType(0);
            orderEntity.setOrderModifyAt(new Date(orderInfo.getLastUpdateTime()));
            orderEntity.setCustomParameters(null);
            orderEntity.setCart(cart);

            OrderWithResBo orderWithResBo = new OrderWithResBo();
            orderWithResBo.setSuOrderEntity(orderEntity);
            orderWithResBo.setScOrderStateRecordEntity(stateRecordEntity);
            result.add(orderWithResBo);

        }
        return result;
    }
}
