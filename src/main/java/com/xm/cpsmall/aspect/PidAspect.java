package com.xm.cpsmall.aspect;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.cpsmall.annotation.Pid;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.user.serialize.entity.SuPidEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.service.PidService;
import com.xm.cpsmall.utils.RequestHeaderConstant;
import com.xm.cpsmall.utils.form.BaseForm;
import com.xm.cpsmall.utils.response.MsgEnum;
import com.xm.cpsmall.utils.response.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 获取用户pid
 */
@Component
@Aspect
public class PidAspect {

    @Autowired
    private PidService pidService;

    @Pointcut("execution(public * com.xm.*..controller.*.*(..,@com.xm.cpsmall.annotation.Pid (*),..))")
    public void pointCut(){}


    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        boolean annotationFlag = false;
        Integer index = null;
        Pid annotation = null;

        for (int i = 0; i < parameters.length; i++) {
            annotation = parameters[i].getAnnotation(Pid.class);
            if(annotation != null && !(joinPoint.getArgs()[i] instanceof BaseForm)) {
                annotationFlag = true;
                index = i;
                break;
            }
        }
        if(!annotationFlag || joinPoint.getArgs()[index] != null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String platformType = request.getHeader(RequestHeaderConstant.PLATFORM_TYPE);
        String userInfo = request.getHeader(RequestHeaderConstant.USER_INFO);
        if(annotation.necessary() && StrUtil.hasBlank(userInfo,platformType))
            return R.error(MsgEnum.SYSTEM_INVALID_USER_ERROR);

        SuUserEntity suUserEntity = JSON.parseObject(Base64.decodeStr(userInfo),SuUserEntity.class);
        SuPidEntity suPidEntity = pidService.getPid(suUserEntity.getPid());

        String pid = null;
        switch (Integer.valueOf(platformType)){
            case PlatformTypeConstant.PDD:{
                pid = suPidEntity.getPdd();
                break;
            }
            case PlatformTypeConstant.JD:{
                pid = suPidEntity.getJd();
                break;
            }
            case PlatformTypeConstant.MGJ:{
                pid = suPidEntity.getMgj();
                break;
            }
            case PlatformTypeConstant.TB:{
                pid = suPidEntity.getTb();
                break;
            }
        }
        if(annotation.necessary() && pid == null)
            return R.error(MsgEnum.DATA_ALREADY_NOT_EXISTS,String.format("找不到用户：[%s] 所属平台：[%s]的pid!",suUserEntity.getId(),platformType));
        joinPoint.getArgs()[index] = pid;
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
