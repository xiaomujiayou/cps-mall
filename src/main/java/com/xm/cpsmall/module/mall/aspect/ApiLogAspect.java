package com.xm.cpsmall.module.mall.aspect;

import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopBaseHttpRequest;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import com.xm.cpsmall.exception.ApiCallException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 开放平台api接口日志
*/
@Aspect
@Component
@Slf4j
public class ApiLogAspect {
    @Pointcut("execution(public * com.pdd.pop.sdk.http.PopHttpClient.*(*))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        Map<String,String> param = null;
        for (Object arg : joinPoint.getArgs()) {
            if(arg instanceof  PopBaseHttpRequest){
                PopBaseHttpRequest request = (PopBaseHttpRequest)arg;
                param = request.getParamsMap();
                break;
            }
        }
        String type = param.get("type");
        PopBaseHttpResponse res = (PopBaseHttpResponse)joinPoint.proceed();
        if(res.getErrorResponse() != null)
            throw new ApiCallException("pdd",type,res.getErrorResponse().getErrorCode().toString(),res.getErrorResponse().getErrorMsg(),param,res);
        param.remove("version");
        param.remove("type");
        param.remove("data_type");
        log.debug("pdd api:[{}] param:[{}] res:[{}]",type,JSON.toJSONString(param),JSON.toJSONString(res));
        return res;
    }
}

