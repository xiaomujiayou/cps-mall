package com.xm.cpsmall.module.wind.message.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import com.xm.cpsmall.comm.mq.message.config.WindMqConfig;
import com.xm.cpsmall.comm.mq.message.impl.OrderStateChangeMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserBillCreateMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserCreditBillCountDownMessage;
import com.xm.cpsmall.module.user.constant.BillStateConstant;
import com.xm.cpsmall.module.user.constant.BillTypeConstant;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.constant.CreditBillUnbindConstant;
import com.xm.cpsmall.module.wind.mapper.SwCreditBillBindRecordMapper;
import com.xm.cpsmall.module.wind.mapper.SwCreditBillPayRecordMapper;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillBindRecordEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillConfEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillPayRecordEntity;
import com.xm.cpsmall.module.wind.serialize.entity.SwCreditRecordEntity;
import com.xm.cpsmall.module.wind.service.CreditBillService;
import com.xm.cpsmall.module.wind.service.UserCreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

import static com.xm.cpsmall.comm.mq.constant.RabbitMqConstant.DELAY_MESSAGE_HEAD_NAME;

/**
 * 预支订单处理
 */
@Slf4j
@Component
public class CreditBillProcessHandler implements MessageHandler {

    //每天晚上九点打款
    private static final int PAY_HOURS = 21;

    @Autowired
    private SwCreditBillPayRecordMapper swCreditBillPayRecordMapper;
    @Autowired
    private SwCreditBillBindRecordMapper swCreditBillBindRecordMapper;
    @Autowired
    private CreditBillService creditBillService;
    @Autowired
    private UserCreditService userCreditService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Override
    public List<Class> getType() {
        return CollUtil.newArrayList(
                UserBillCreateMessage.class,
                OrderStateChangeMessage.class
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handle(AbsUserActionMessage message) {
        if(message instanceof UserBillCreateMessage) {
            /**
             * 检测账单是否满足信用支付
             */
            UserBillCreateMessage userBillCreateMessage = (UserBillCreateMessage) message;
            //自购类型检测
            if (!CollUtil.newArrayList(BillTypeConstant.BUY_NORMAL, BillTypeConstant.BUY_SHARE).contains(userBillCreateMessage.getSuBillEntity().getType()))
                return;
            //状态检测
            if (!userBillCreateMessage.getSuBillEntity().getState().equals(BillStateConstant.WAIT))
                return;
            //重复检测
            if (exist(userBillCreateMessage.getSuBillEntity().getBillSn()))
                return;
            creditBillService.payBillByCredit(userBillCreateMessage.getSuBillEntity(),userBillCreateMessage.getSuOrderEntity().getProductName());
        }else if(message instanceof OrderStateChangeMessage) {
            /**
             * 用户确认收货 -> 重新考核账单是否满足信用支付 -> 计算返现时间
             */
            OrderStateChangeMessage orderStateChangeMessage = (OrderStateChangeMessage) message;
            if (!orderStateChangeMessage.getOldOrder().getState().equals(OrderStateConstant.PAY) || !orderStateChangeMessage.getNewState().equals(OrderStateConstant.CONFIRM_RECEIPT))
                return;
            SuBillEntity suBillEntity = orderStateChangeMessage.getSuBillEntity();
            if (suBillEntity == null || suBillEntity.getId() == null) {
                log.warn("用户：{} 订单：{} 相关账单不存在!",
                        orderStateChangeMessage.getUserId(),
                        orderStateChangeMessage.getOldOrder().getOrderSubSn(),
                        suBillEntity.getBillSn());
                return;
            } else if (!suBillEntity.getState().equals(BillStateConstant.WAIT)) {
                log.warn("用户：{} 订单：{} 相关账单:{} 账单状态异常!",
                        orderStateChangeMessage.getUserId(),
                        orderStateChangeMessage.getOldOrder().getOrderSubSn(),
                        suBillEntity.getBillSn());
                return;
            }
            if (suBillEntity.getCreditState() == null || suBillEntity.getCreditState() != 1) {
                log.debug("用户：{} 账单：{} 非信用账单", suBillEntity.getUserId(), suBillEntity.getBillSn());
                return;
            }


            Lock lock = null;
            try {
                lock = redisLockRegistry.obtain(CreditBillProcessHandler.class.getSimpleName()+":"+suBillEntity.getUserId());
                lock.lock();
                SwCreditRecordEntity swCreditRecordEntity = creditBillService.getUserCredit(suBillEntity.getUserId());
                SwCreditBillConfEntity swCreditBillConfEntity = creditBillService.getConfByScores(swCreditRecordEntity);
                SwCreditBillBindRecordEntity example = new SwCreditBillBindRecordEntity();
                example.setUserId(suBillEntity.getUserId());
                example.setState(1);
                List<SwCreditBillBindRecordEntity> bindRecordEntities = swCreditBillBindRecordMapper.select(example);
                //绑定的总额度
                Integer totalMoney = bindRecordEntities == null ? 0 : bindRecordEntities.stream().mapToInt(SwCreditBillBindRecordEntity::getBillMoney).sum();
                //绑定的总数
                Integer totalCount = bindRecordEntities == null ? 0 : bindRecordEntities.size();
                if (swCreditBillConfEntity == null) {
                    creditBillService.creditUnBindBill(suBillEntity, swCreditRecordEntity, CreditBillUnbindConstant.CREDIT_REDUCE, String.format("转变为普通订单：用户信用不足，转变为普通订单 当前信用：%s", swCreditRecordEntity.getScores()));
                } else if (suBillEntity.getMoney() > swCreditBillConfEntity.getQuota()) {
                    creditBillService.creditUnBindBill(suBillEntity, swCreditRecordEntity, CreditBillUnbindConstant.CREDIT_REDUCE, String.format("转变为普通订单：预支单笔额度超限 当前信用：%s 单笔预支最大额度：%s元 当前：%s元，", swCreditRecordEntity.getScores(), swCreditBillConfEntity.getQuota() / 100d, suBillEntity.getMoney() / 100d));
                } else if (totalMoney > swCreditBillConfEntity.getMaxQuota()) {
                    creditBillService.creditUnBindBill(suBillEntity, swCreditRecordEntity,CreditBillUnbindConstant.CREDIT_REDUCE, String.format("转变为普通订单：预支额度超限 当前信用：%s 最大总额度：%s元 当前：%s元，", swCreditRecordEntity.getScores(), swCreditBillConfEntity.getMaxQuota() / 100d, suBillEntity.getMoney() / 100d));
                } else if (totalCount > swCreditBillConfEntity.getAdvanceCount()) {
                    creditBillService.creditUnBindBill(suBillEntity, swCreditRecordEntity,CreditBillUnbindConstant.CREDIT_REDUCE, String.format("转变为普通订单：用户可预支次数不足 当前信用：%s 可预支总数：%s 已使用：%s", swCreditRecordEntity.getScores(), swCreditBillConfEntity.getAdvanceCount(), bindRecordEntities.size()));
                } else {
                    //满足打款条件
                    //最后一笔订单发放时间
                    Date lastPayTime = bindRecordEntities == null ? null : bindRecordEntities.stream().filter(o -> o.getPayTime() != null).max((o1, o2) -> o1.getPayTime().compareTo(o2.getPayTime())).orElse(new SwCreditBillBindRecordEntity()).getPayTime();
                    Date[] dates = calcCreditPayTime(lastPayTime, swCreditBillConfEntity);
                    //跟新绑定记录的支付时间
                    example.setBillSn(suBillEntity.getBillSn());
                    SwCreditBillBindRecordEntity billBindRecordEntity = swCreditBillBindRecordMapper.selectOne(example);
                    billBindRecordEntity.setPayTime(dates[1]);
                    swCreditBillBindRecordMapper.updateByPrimaryKeySelective(billBindRecordEntity);
                    suBillEntity.setPayTime(dates[1]);
                    //发送延时队列延期支付
                    rabbitTemplate.convertAndSend(WindMqConfig.EXCHANGE_DELAY, WindMqConfig.KEY_CREDIT_DELAY, suBillEntity, msg -> {
                        // 设置消息属性-过期时间
                        Long ms = null;
                        if (new Date().getTime() < dates[0].getTime()) {
                            ms = dates[0].getTime() - new Date().getTime();
                            msg.getMessageProperties().setHeader(DELAY_MESSAGE_HEAD_NAME,ms);
                        }
                        return msg;
                    });
                    rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE, "", new UserCreditBillCountDownMessage(suBillEntity.getUserId(), suBillEntity));
                    return;
                }
            }finally {
                if(lock != null)
                    lock.unlock();
            }
        }
    }

    /**
     * 计算信用账单支付时间
     * @return
     */
    private Date[] calcCreditPayTime(Date lastPayTime, SwCreditBillConfEntity userConf){
        Date start = null;
        if(lastPayTime == null){
            if(DateUtil.thisHour(true) > PAY_HOURS - 1){
                //距离打款时间一小时，明天支付
                start = DateUtil.tomorrow();
            }else {
                //今天支付
                start = DateUtil.parse(DateUtil.today());
            }
        }else {
            start = DateUtil.beginOfDay(lastPayTime).offset(DateField.HOUR_OF_DAY, 24 * userConf.getPayDays());
        }
        Date payTime = DateUtil.offset(start, DateField.HOUR_OF_DAY,PAY_HOURS);
        Date[] dates = {start,payTime};
        return dates;
    }

    /**
     * 记录是否存在
     * @param billSn
     * @return
     */
    private boolean exist(String billSn){
        SwCreditBillPayRecordEntity recordEntity = new SwCreditBillPayRecordEntity();
        recordEntity.setBillSn(billSn);
        int count = swCreditBillPayRecordMapper.selectCount(recordEntity);
        return count > 0;
    }

    @Override
    public void onError(Exception e) {

    }
}
