package com.xm.cpsmall.aspect;

import com.xm.cpsmall.comm.mq.message.config.WindMqConfig;
import com.xm.cpsmall.module.wind.serialize.entity.SwLoginRecordEntity;
import com.xm.cpsmall.utils.IpUtil;
import com.xm.cpsmall.utils.RequestHeaderConstant;
import com.xm.cpsmall.utils.response.Msg;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class WindLoginRecordAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("execution(public * com.xm.cpsmall.module.user.controller.LoginController.login(..))")
    private void pointCut(){};

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        SwLoginRecordEntity entity = new SwLoginRecordEntity();
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String appType = request.getHeader(RequestHeaderConstant.APP_TYPE);
        entity.setAppType(appType == null ? null : Integer.valueOf(appType));
        entity.setIp(IpUtil.getIp(request));
        entity.setUa(request.getHeader("User-Agent"));
        entity.setCreateTime(new Date());
        Object res = joinPoint.proceed();
        Map<String,Map<String, Object>> userInfo = ((Map<String,Map<String, Object>>)res);
        if(userInfo == null || userInfo.get("userInfo") == null)
            return res;
        Object userId = userInfo.get("userInfo").get("id");
        entity.setUserId(userId == null ? null : Integer.valueOf(userId.toString()));
        rabbitTemplate.convertAndSend(WindMqConfig.EXCHANGE,WindMqConfig.KEY_LOGIN,entity);
        return res;
    }
}
