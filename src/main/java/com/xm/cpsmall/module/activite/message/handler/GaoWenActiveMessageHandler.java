package com.xm.cpsmall.module.activite.message.handler;

import cn.hutool.core.util.StrUtil;
import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.impl.OrderCreateMessage;
import com.xm.cpsmall.comm.mq.message.impl.OrderStateChangeMessage;
import com.xm.cpsmall.module.activite.constant.ActiveConstant;
import com.xm.cpsmall.module.activite.mapper.SaActiveMapper;
import com.xm.cpsmall.module.activite.mapper.SaBillMapper;
import com.xm.cpsmall.module.activite.serialize.entity.SaActiveEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import com.xm.cpsmall.module.activite.service.ActiviteBillService;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;
import com.xm.cpsmall.utils.lock.LockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 高温补贴活动
 */
@Slf4j
@Component
public class GaoWenActiveMessageHandler implements MessageHandler {

    @Autowired
    private SaActiveMapper saActiveMapper;
    @Autowired
    private SaBillMapper saBillMapper;
    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private ActiviteBillService activiteBillService;

    @Override
    public List<Class> getType() {
        return Arrays.asList(
                OrderCreateMessage.class,
                OrderStateChangeMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if (message instanceof OrderCreateMessage) {
            //创建高温补贴
            SaActiveEntity gaoWen = saActiveMapper.selectByPrimaryKey(ActiveConstant.GAO_WEN_BU_TIE_ACTIVE_ID);
            if (gaoWen == null || gaoWen.getState() != 1)
                return;
            OrderCreateMessage orderCreateMessage = (OrderCreateMessage) message;
            createBillByOrder(orderCreateMessage, gaoWen);
        } else if (message instanceof OrderStateChangeMessage) {
            OrderStateChangeMessage orderStateChangeMessage = (OrderStateChangeMessage) message;
            SaBillEntity example = new SaBillEntity();
            example.setUserId(orderStateChangeMessage.getUserId());
            example.setState(6);
            example.setActiveId(ActiveConstant.GAO_WEN_BU_TIE_ACTIVE_ID);
            example.setAttach(orderStateChangeMessage.getOldOrder().getOrderSubSn());
            example.setUserId(orderStateChangeMessage.getUserId());
            SaBillEntity saBillEntity = saBillMapper.selectOne(example);
            if (saBillEntity == null || saBillEntity.getId() == null)
                return;
            if (orderStateChangeMessage.getOldOrder().getState().equals(OrderStateConstant.PAY) && orderStateChangeMessage.getNewState().equals(OrderStateConstant.CONFIRM_RECEIPT)) {
                SaActiveEntity gaoWen = saActiveMapper.selectByPrimaryKey(ActiveConstant.GAO_WEN_BU_TIE_ACTIVE_ID);
                //发放高温补贴
                activiteBillService.cashOut(saBillEntity, "粉饰生活-" + gaoWen == null ? "" : gaoWen.getName() + "活动-" + saBillEntity.getAttachDes());
            } else if (Arrays.asList(OrderStateConstant.FAIL, OrderStateConstant.FAIL_SETTLED).contains(orderStateChangeMessage.getNewState())) {
                //订单异常，结算失败
                saBillEntity.setState(4);
                saBillEntity.setFailReason(orderStateChangeMessage.getNewOrder().getFailReason());
                saBillMapper.updateByPrimaryKeySelective(saBillEntity);
            }
        }
    }

    private void createBillByOrder(OrderCreateMessage orderCreateMessage, SaActiveEntity gaoWen) {
        if (StrUtil.isBlank(orderCreateMessage.getSuOrderEntity().getOrderSubSn())) {
            log.error("高温补贴活动 订单单号不存在，创建活动奖励失败：{}", orderCreateMessage);
            return;
        }
        Lock lock = redisLockRegistry.obtain(this.getClass().getSimpleName() + ":" + orderCreateMessage.getSuOrderEntity().getOrderSubSn());
        LockUtil.lock(lock, () -> {
            SaBillEntity saBillEntity = new SaBillEntity();
            saBillEntity.setAttach(orderCreateMessage.getSuOrderEntity().getOrderSubSn());
            int count = saBillMapper.selectCount(saBillEntity);
            if (count > 0) {
                log.error("高温补贴活动 订单单号:{} 相关活动奖励已存在", orderCreateMessage.getSuOrderEntity().getOrderSubSn());
                return;
            }
            activiteBillService.createBill(
                    orderCreateMessage.getUserId(),
                    ActiveConstant.GAO_WEN_BU_TIE_ACTIVE_ID,
                    1,
                    gaoWen.getMoney(),
                    6,
                    orderCreateMessage.getSuOrderEntity().getOrderSubSn(),
                    orderCreateMessage.getSuOrderEntity().getProductName(),
                    null
            );
        });
    }


    @Override
    public void onError(Exception e) {

    }
}
