package com.xm.cpsmall.aspect;

import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * form校验
 */

@Component
@Aspect
public class ParamValidAspect {

    @Pointcut("execution(public * com.xm.*..controller..*.*(..,org.springframework.validation.BindingResult,..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        boolean annotationFlag = false;
        for (Parameter parameter:parameters){
            if(parameter.getAnnotation(Valid.class) != null) {
                annotationFlag = true;
                break;
            }
        }
        if(!annotationFlag) {
            return joinPoint.proceed();
        }
        for (Object arg:joinPoint.getArgs()){
            if(arg instanceof BindingResult){
                BindingResult bindingResult = (BindingResult)arg;
                if(!bindingResult.hasErrors()) {
                    return joinPoint.proceed();
                }else {
                    String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
                    throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,errorMsg);
                }
            }
        }
        return joinPoint.proceed();
    }
}
