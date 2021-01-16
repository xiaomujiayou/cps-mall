package com.xm.cpsmall.module.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.constant.PlatformTypeEnum;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;
import com.xm.cpsmall.module.user.mapper.SuOrderMapper;
import com.xm.cpsmall.module.user.mapper.SuPidMapper;
import com.xm.cpsmall.module.user.mapper.SuProductMapper;
import com.xm.cpsmall.module.user.mapper.SuUserMapper;
import com.xm.cpsmall.module.user.mapper.custom.SuBillMapperEx;
import com.xm.cpsmall.module.user.serialize.bo.OrderCustomParameters;
import com.xm.cpsmall.module.user.serialize.dto.OrderBillDto;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuPidEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuProductEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.service.OrderService;
import com.xm.cpsmall.module.user.service.UserBillService;
import com.xm.cpsmall.utils.EnumUtils;
import com.xm.cpsmall.utils.LockHelper;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 计算收益分配到各个收益用户中
 * ①:分享订单只有【购买者】和【分享者】受益
 * ②:其他订单【购买者】、【代理人】受益
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Lazy
    @Autowired
    private OrderService orderService;
    @Autowired
    private SuOrderMapper suOrderMapper;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private SuBillMapperEx suBillMapperEx;
    @Autowired
    private SuPidMapper suPidMapper;
    @Autowired
    private SuUserMapper suUserMapper;
    @Autowired
    private SuProductMapper suProductMapper;

    /**
     * 处理订单消息
     * ①:添加新订单
     * ②:更新旧订单
     * @param order
     */
    @Override
    public void receiveOrderMsg(SuOrderEntity order) throws Exception{
        //判断是否为新收录订单（系统未曾收录的）
        SuOrderEntity oldOrder = getOldOrder(order);
        if(oldOrder == null){
            //收录订单并计算相关账单
            orderService.onOrderCreate(order);
            return;
        }else if(repeated(oldOrder,order))          //是否重复收录
            return;
        //更新订单状态，并根据情况发放佣金
        order.setId(oldOrder.getId());
        orderService.updateOrderState(order,oldOrder);
    }

    /**
     * 订单是否重复
     * @param oldOrder
     * @param order
     * @return
     */
    private boolean repeated(SuOrderEntity oldOrder, SuOrderEntity order) {
        if(oldOrder == null)
            return false;
        if(oldOrder.getPlatformType().equals(order.getPlatformType()) && oldOrder.getState().equals(order.getState()))
            return true;
        return false;
    }

    /**
     * 获取旧订单
     * @param order
     * @return      :null标识该订单未曾收录
     */
    private SuOrderEntity getOldOrder(SuOrderEntity order){
        if(StrUtil.isBlank(order.getOrderSubSn()) || order.getPlatformType() == null)
            throw new GlobleException( MsgEnum.DATA_INVALID_ERROR,"订单数据异常：{}",JSON.toJSONString(order));
        PageHelper.startPage(1,1,false);
        SuOrderEntity example = new SuOrderEntity();
        example.setOrderSubSn(order.getOrderSubSn());
        example.setPlatformType(order.getPlatformType());
        example = suOrderMapper.selectOne(example);
        if(example == null)
            return null;
        return example;
    }

    /**
     * 收到一笔新订单(系统不存在的)
     * ①:入库保存
     * ②:计算相关用户收益
     * @param order
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onOrderCreate(SuOrderEntity order) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(order.getState() != OrderStateConstant.PAY)
            return;
        //保存订单
        OrderCustomParameters params = JSON.parseObject(order.getCustomParameters(), OrderCustomParameters.class);
        if(Arrays.asList(PlatformTypeConstant.MGJ,PlatformTypeConstant.WPH).contains(order.getPlatformType())){
            //没有回调参数，因此只能通过pid 反向生成
            if(StrUtil.isBlank(order.getPId()))
                throw new GlobleException(MsgEnum.ORDER_INVALID_ERROR,"订单:[" + order.getOrderSn() + "] pid 不存在");
            params = parseOrderParamByPid(order.getProductId(),order.getOrderSubSn(),order.getPlatformType(),order.getPId(),order.getCart());
            order.setCustomParameters(JSON.toJSONString(params));
        }else if(order.getPlatformType() == PlatformTypeConstant.PDD && order.getShareUserId() == null){
            //从历史记录中获取分享者信息
            SuProductEntity productEntity = new SuProductEntity();
            productEntity.setUserId(params.getUserId());
            productEntity.setGoodsId(order.getProductId());
            productEntity.setPlatformType(order.getPlatformType());
            PageHelper.startPage(1,1).count(false);
            productEntity = suProductMapper.selectOne(productEntity);
            if(productEntity != null && productEntity.getId() != null){
                params.setShareUserId(productEntity.getShareUserId());
                order.setCustomParameters(JSON.toJSONString(params));
            }
        }
        order.setUserId(params.getUserId());
        order.setShareUserId(params.getShareUserId());
        if(order.getShareUserId() != null && !order.getShareUserId().equals("")){
            order.setFormType(2);
        }else {
            order.setFormType(1);
        }
        order.setFromApp(params.getFromApp());
        order.setCreateTime(new Date());
        suOrderMapper.insertUseGeneratedKeys(order);
        //创建订单收益账单
        userBillService.createByOrder(order);
    }

    private OrderCustomParameters parseOrderParamByPid(String goodsId,String orderSubSn,Integer platformType,String pid,String cart) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(StrUtil.isBlank(pid))
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS,"订单：" + orderSubSn +" pid 为空，订单解析失败");
        OrderCustomParameters parameters = new OrderCustomParameters();
        SuPidEntity record = new SuPidEntity();
        if(PlatformTypeConstant.MGJ == platformType)
            record.setMgj(pid);
        if(PlatformTypeConstant.WPH == platformType)
            record.setWph(pid);
        PageHelper.startPage(1,1).count(false);
        record = suPidMapper.selectOne(record);
        SuUserEntity suUserEntity = new SuUserEntity();
        suUserEntity.setPid(record.getId());
        PageHelper.startPage(1,1).count(false);
        suUserEntity = suUserMapper.selectOne(suUserEntity);
        parameters.setUserId(suUserEntity.getId());
        SuProductEntity productEntity = new SuProductEntity();
        productEntity.setUserId(suUserEntity.getId());
        productEntity.setGoodsId(goodsId);
        productEntity.setPlatformType(platformType);
        PageHelper.startPage(1,1).count(false);
        productEntity = suProductMapper.selectOne(productEntity);
        if(productEntity != null && productEntity.getId() != null){
            parameters.setFromApp(productEntity.getAppType());
            parameters.setShareUserId(productEntity.getShareUserId());
        }else {
            log.debug("{} 订单：{} 未能找到 商品id：{} 的历史记录", EnumUtils.getEnum(PlatformTypeEnum.class,"type",platformType).getName(),goodsId,orderSubSn);
        }
        return parameters;
    }


    /**
     * 更新订单状态
     * 处理达到要求的订单
     * @param newOrder
     * @param oldOrder
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderState(SuOrderEntity newOrder,SuOrderEntity oldOrder) {
        SuOrderEntity record = new SuOrderEntity();
        record.setOrderSubSn(oldOrder.getOrderSubSn());
        PageHelper.startPage(1,1).count(false);
        LockHelper.lock();
        record = suOrderMapper.selectOne(record);
        if(record == null || record.getState().equals(newOrder.getState()))
            return;
        record.setState(newOrder.getState());
        record.setFailReason(newOrder.getFailReason());
        record.setOrderModifyAt(new Date());
        suOrderMapper.updateByPrimaryKeySelective(record);
        userBillService.updateBillStateByOrderStateChange(oldOrder,newOrder.getState(),newOrder.getFailReason());
    }

    @Override
    public PageBean<OrderBillDto> getOrderBill(Integer userId, Integer type, Integer platformType, Integer state, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("so.create_time desc");
        List<OrderBillDto> list = suBillMapperEx.getOrderBill(userId ,type,platformType,state);
        return new PageBean<>(list);
    }

}
