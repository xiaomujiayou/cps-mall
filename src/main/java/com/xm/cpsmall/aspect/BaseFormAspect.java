package com.xm.cpsmall.aspect;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.cpsmall.annotation.AppType;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.annotation.Pid;
import com.xm.cpsmall.annotation.PlatformType;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.user.serialize.entity.SuPidEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.service.PidService;
import com.xm.cpsmall.utils.RequestHeaderConstant;
import com.xm.cpsmall.utils.form.BaseForm;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
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
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class BaseFormAspect {

    @Autowired
    private PidService pidService;

    @Pointcut("execution(public * com.xm.*..controller.*.*(..,(com.xm.cpsmall.utils.form.BaseForm+),..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        Integer index = null;
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        for (int i = 0; i < parameters.length; i++) {
            if(!(joinPoint.getArgs()[i] instanceof BaseForm))
                continue;
            index = i;
            BaseForm baseForm = (BaseForm)joinPoint.getArgs()[index];
            if(baseForm == null)
                baseForm = (BaseForm)parameters[i].getType().newInstance();

            //装配真实IP
            String ip = request.getHeader(RequestHeaderConstant.IP);
            baseForm.setIp(ip);

            //装配userId
            if(baseForm.getUserId() == null){
                Annotation annotation = parameters[i].getAnnotation(LoginUser.class);
//                HttpSession session = request.getSession();
//                Object user = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
//                SimplePrincipalCollection principal = (SimplePrincipalCollection)user;
//                SuUserEntity suUserEntity = principal.oneByType(SuUserEntity.class);
                Object userObj = request.getSession().getAttribute(RequestHeaderConstant.USER_INFO);
                if(annotation != null && ((LoginUser)annotation).necessary() && userObj == null){
                    throw new GlobleException(MsgEnum.SYSTEM_INVALID_USER_ERROR);
                }else {
                    SuUserEntity suUserEntity = (SuUserEntity)userObj;
                    baseForm.setUserId(suUserEntity != null ? suUserEntity.getId() : null);
                    baseForm.setOpenId(suUserEntity != null ? suUserEntity.getOpenId() : null);
                }
            }

            //装配platformType
            if(baseForm.getPlatformType() == null){
                Annotation annotation = parameters[i].getAnnotation(PlatformType.class);
                String platformType = request.getHeader(RequestHeaderConstant.PLATFORM_TYPE);
                if(annotation != null && ((PlatformType)annotation).necessary() && StrUtil.isBlank(platformType)){
                    throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"platformType 不存在");
                }else {
                    baseForm.setPlatformType(platformType != null ? Integer.valueOf(platformType) : null);
                }
            }

            //装配appType
            if(baseForm.getAppType() == null){
                Annotation annotation = parameters[i].getAnnotation(AppType.class);
                String appType = request.getHeader(RequestHeaderConstant.APP_TYPE);
                if(annotation != null && ((AppType)annotation).necessary() && StrUtil.isBlank(appType)){
                    throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"appType 不存在");
                }else {
                    baseForm.setAppType(appType != null ? Integer.valueOf(appType) : null);
                }
            }

            //装配pid
            if(baseForm.getPid() == null){
                Annotation annotation = parameters[i].getAnnotation(Pid.class);
                Object userObj = request.getSession().getAttribute(RequestHeaderConstant.USER_INFO);
                if(annotation != null && ((Pid)annotation).necessary() && userObj == null){
                    throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"pid 获取失败 userId不存在");
                }else if(annotation != null && ((Pid)annotation).necessary() && baseForm.getUserId() != null){
                    SuPidEntity suPidEntity = pidService.getPid(userObj == null ? null : ((SuUserEntity)userObj).getPid());
                    String pid = null;
                    switch (Integer.valueOf(baseForm.getPlatformType())){
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
                        case PlatformTypeConstant.TB: {
                            pid = suPidEntity.getTb();
                            break;
                        }
                        case PlatformTypeConstant.WPH:{
                            pid = suPidEntity.getWph();
                            break;
                        }
                    }
                    baseForm.setPid(pid);
                }
            }

            joinPoint.getArgs()[index] = baseForm;
            break;
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
