//package com.xm.cpsmall.aspect;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletResponse;
//
//
///**
// *  记录服务响应时间
// *  ApiRecordFilter
// *
// */
//@Component
//@Aspect
//public class ExecTimeAspect {
//
//    public static final String EXEC_TIME_HEADER = "exec-time";
//
//    @Pointcut("execution(public * com.xm.*..controller..*.*(..))")
//    public void pointCut(){}
//
//    @Around("pointCut()")
//    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
//        HttpServletResponse response =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        long start = System.currentTimeMillis();
//        Object res = joinPoint.proceed();
//        response.setHeader(EXEC_TIME_HEADER,(System.currentTimeMillis() - start) + "");
//        return res;
//    }
//}
