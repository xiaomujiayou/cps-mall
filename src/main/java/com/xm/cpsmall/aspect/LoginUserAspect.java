package com.xm.cpsmall.aspect;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.user.mapper.SuUserMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.utils.RequestHeaderConstant;
import com.xm.cpsmall.utils.form.BaseForm;
import com.xm.cpsmall.utils.response.MsgEnum;
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
 * 获取登录的用户信息
 */

@Component
@Aspect
public class LoginUserAspect {

    @Autowired
    private SuUserMapper suUserMapper;

    @Pointcut("execution(public * com.xm.*..controller.*.*(..,@com.xm.cpsmall.annotation.LoginUser (*),..))")
    public void pointCut(){}


    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        boolean annotationFlag = false;
        boolean isObjInfo = false;
        Integer index = null;
        LoginUser annotation = null;
        for (int i = 0; i < parameters.length; i++) {
            annotation = parameters[i].getAnnotation(LoginUser.class);
            if(annotation != null && !(joinPoint.getArgs()[i] instanceof BaseForm)) {
                annotationFlag = true;
                index = i;
                if(!parameters[i].getType().equals(Integer.class))
                    isObjInfo = true;
                break;
            }
        }

        if(!annotationFlag || (joinPoint.getArgs()[index] != null )) {
            return joinPoint.proceed();
        }

        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object userObj = request.getSession().getAttribute(RequestHeaderConstant.USER_INFO);
        if(annotation != null && ((LoginUser)annotation).necessary() && userObj == null){
            throw new GlobleException(MsgEnum.SYSTEM_INVALID_USER_ERROR);
        }

        if(isObjInfo){
            if(annotation.latest()){
                SuUserEntity suUserEntity = suUserMapper.selectByPrimaryKey(((SuUserEntity)userObj).getId());
                joinPoint.getArgs()[index] = suUserEntity;
            }else {
                SuUserEntity suUserEntity = (SuUserEntity)userObj;
                joinPoint.getArgs()[index] = suUserEntity;
            }
        }else if(!isObjInfo){
            joinPoint.getArgs()[index] = Integer.valueOf(((SuUserEntity)userObj).getId());
        }else {
            return joinPoint.proceed();
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

}
