package com.xm.cpsmall.module.user.service;

import com.xm.cpsmall.module.user.serialize.dto.OrderBillDto;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.utils.mybatis.PageBean;

import java.lang.reflect.InvocationTargetException;

/**
 * 用户订单服务
 */
public interface OrderService {


    /**
     * 收到一条订单消息
     * @param order
     */
    public void receiveOrderMsg(SuOrderEntity order) throws Exception;

    /**
     * 新订单处理
     * 订单处理的核心逻辑
     * @param order
     */
    public void onOrderCreate(SuOrderEntity order) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;


    /**
     * 更新订单状态以及相关信息
     * @param newOrder
     * @param oldOrder
     */
    public void updateOrderState(SuOrderEntity newOrder, SuOrderEntity oldOrder);

    /**
     * 查询用户订单
     * @param userId
     * @param type          1:自购订单,2:分享订单
     * @param platformType
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<OrderBillDto> getOrderBill(Integer userId, Integer type, Integer platformType, Integer state, Integer pageNum, Integer pageSize);
}
