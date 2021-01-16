package com.xm.cpsmall.module.user.aspect;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import com.xm.cpsmall.comm.mq.message.impl.*;
import com.xm.cpsmall.module.user.constant.BillStateConstant;
import com.xm.cpsmall.module.user.constant.BillTypeConstant;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;
import com.xm.cpsmall.module.user.mapper.SuBillMapper;
import com.xm.cpsmall.module.user.mapper.SuOrderMapper;
import com.xm.cpsmall.module.user.mapper.SuUserMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 消息生成器
 */
@Aspect
@Component
@Slf4j
public class UserActionMessageAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SuUserMapper suUserMapper;
    @Autowired
    private SuBillMapper suBillMapper;
    @Autowired
    private SuOrderMapper suOrderMapper;

    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.UserService.addNewUser(..))")
    public void addNewUserPointCut(){}
    /**
     * 生成
     * UserFristLoginMessage
     * UserAddProxyMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("addNewUserPointCut()")
    public Object addNewUser(ProceedingJoinPoint joinPoint) throws Throwable {
        SuUserEntity suUserEntity = (SuUserEntity)joinPoint.proceed();
        //首次登录消息
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserFristLoginMessage(suUserEntity.getId(),suUserEntity));
        if(suUserEntity.getParentId() != null) {
            //新增代理消息
            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserAddProxyMessage(suUserEntity.getParentId(),1, suUserEntity));
            SuUserEntity parentUser = suUserMapper.selectByPrimaryKey(suUserEntity.getParentId());
            if(parentUser.getParentId() != null)
                rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserAddProxyMessage(parentUser.getParentId(),2, suUserEntity));
        }
        return suUserEntity;
    }
    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.UserService.getUserInfo(..))")
    public void userLoginPointCut(){}
    /**
     * 生成
     * UserLoginMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("userLoginPointCut()")
    public Object userLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        SuUserEntity suUserEntity = (SuUserEntity)joinPoint.proceed();
        //首次登录消息
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserLoginMessage(suUserEntity.getId(),suUserEntity));
        return suUserEntity;
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.OrderService.onOrderCreate(..))")
    public void onOrderCreatePointCut(){}
    /**
     * 生成
     * OrderCreateMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("onOrderCreatePointCut()")
    public Object onOrderCreatePointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        SuOrderEntity suOrderEntity = (SuOrderEntity)joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderCreateMessage(suOrderEntity.getUserId(),suOrderEntity));
        return obj;
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.OrderService.updateOrderState(..))")
    public void updateOrderStatePointCut(){}
    /**
     * 生成
     * OrderStateChangeMessage
     * UserMaliceCreditBillMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("updateOrderStatePointCut()")
    public Object updateOrderStatePointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        SuOrderEntity newOrder = (SuOrderEntity)joinPoint.getArgs()[0];
        SuOrderEntity oldOrder = (SuOrderEntity)joinPoint.getArgs()[1];

        SuBillEntity example = new SuBillEntity();
        SuBillEntity suBillEntity = null;
        if(oldOrder.getUserId() != null){
            example.setUserId(oldOrder.getUserId());
            example.setAttach(oldOrder.getId());
            PageHelper.startPage(1,1).count(false);
            suBillEntity = suBillMapper.selectOne(example);
        }
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderStateChangeMessage(oldOrder.getUserId(),oldOrder,newOrder,suBillEntity));
        if(suBillEntity != null && suBillEntity.getState().equals(BillStateConstant.ALREADY) && newOrder.getState().equals(OrderStateConstant.FAIL_SETTLED))
            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserMaliceCreditBillMessage(suBillEntity.getUserId(),suBillEntity,oldOrder,newOrder));
        return obj;
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.UserBillService.payOutOrderBill(..))")
    public void payOutOrderBillPointCut(){}
    /**
     * 生成
     * OrderSettlementSucessMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("payOutOrderBillPointCut()")
    public Object payOutOrderBillPointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        SuOrderEntity sucessOrder = (SuOrderEntity)joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderSettlementSucessMessage(sucessOrder.getUserId(),sucessOrder));
        return obj;
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.UserBillService.invalidOrderBill(..))")
    public void invalidOrderBillPointCut(){}
    /**
     * 生成
     * OrderSettlementFailMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("invalidOrderBillPointCut()")
    public Object invalidOrderBillPointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        SuOrderEntity sucessOrder = (SuOrderEntity)joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderSettlementFailMessage(sucessOrder.getUserId(),sucessOrder,sucessOrder.getFailReason()));
        return obj;
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.UserBillService.addBill(..))")
    public void addBillPointCut(){}
    /**
     * 生成
     * UserBillCreateMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("addBillPointCut()")
    public Object addBillPointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        SuBillEntity suBillEntity = (SuBillEntity)joinPoint.getArgs()[0];
        SuOrderEntity suOrderEntity = null;
        if(Arrays.asList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.BUY_SHARE).contains(suBillEntity.getType()))
            suOrderEntity = suOrderMapper.selectByPrimaryKey(suBillEntity.getAttach());
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserBillCreateMessage(suBillEntity.getUserId(),suBillEntity,suOrderEntity));
        return obj;
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.user.service.UserBillService.updateBillState(..))")
    public void updateBillStatePointCut(){}
    /**
     * 生成
     * UserBillStateChangeMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("updateBillStatePointCut()")
    public Object updateBillStatePointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        SuBillEntity suBillEntity = (SuBillEntity)joinPoint.getArgs()[0];
        SuBillEntity copyEntity = new SuBillEntity();
        BeanUtil.copyProperties(suBillEntity,copyEntity);
        Object obj = joinPoint.proceed();
        Integer newState = (Integer)joinPoint.getArgs()[1];
        String failReason = (String) joinPoint.getArgs()[2];
        SuUserEntity user = suUserMapper.selectByPrimaryKey(suBillEntity.getUserId());
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserBillStateChangeMessage(suBillEntity.getUserId(),copyEntity,newState,failReason,user));
        return obj;
    }
}

