package com.xm.cpsmall.module.cron.aspect;

import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import com.xm.cpsmall.comm.mq.message.impl.UserPaymentSucessMessage;
import com.xm.cpsmall.module.cron.serialize.entity.ScBillPayEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxEntPayOrderInEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成用户动作mq消息
 */
@Aspect
@Component
@Slf4j
public class CronUserActionMessageAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("execution(public * com.xm.cpsmall.module.cron.service.BillPayService.onEntPaySucess(..))")
    public void userPaymentSucessMessagePointCut(){}

    /**
     * 生成
     * UserPaymentSucessMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("userPaymentSucessMessagePointCut()")
    public Object goodsDetailMessagePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        SmProductEntityEx smProductEntityEx = (SmProductEntityEx)joinPoint.proceed();
        ScBillPayEntity scBillPayEntity = (ScBillPayEntity) joinPoint.getArgs()[0];
        SpWxEntPayOrderInEntity spWxEntPayOrderInEntity = (SpWxEntPayOrderInEntity) joinPoint.getArgs()[1];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserPaymentSucessMessage(scBillPayEntity.getUserId(),scBillPayEntity,spWxEntPayOrderInEntity));
        return smProductEntityEx;
    }



}

