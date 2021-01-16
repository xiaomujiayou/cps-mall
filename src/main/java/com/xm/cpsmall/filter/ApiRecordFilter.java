//package com.xm.cpsmall.filter;
//
//import cn.hutool.core.io.IoUtil;
//import com.alibaba.fastjson.JSON;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.xm.cloud_gateway.util.IpUtil;
//import com.xm.comment.aspect.ExecTimeAspect;
//import com.xm.comment_mq.message.config.WindMqConfig;
//import com.xm.comment_serialize.module.gateway.constant.RequestHeaderConstant;
//import com.xm.comment_serialize.module.wind.entity.SwApiRecordEntity;
//import com.xm.cpsmall.aspect.ExecTimeAspect;
//import com.xm.cpsmall.comm.mq.message.config.WindMqConfig;
//import com.xm.cpsmall.module.wind.serialize.entity.SwApiRecordEntity;
//import com.xm.cpsmall.utils.IpUtil;
//import com.xm.cpsmall.utils.RequestHeaderConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;
//
///**
// * 风控系统
// * 记录用户的api请求
// */
//@Slf4j
//@Component
//public class ApiRecordFilter implements Filter {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        SwApiRecordEntity entity = new SwApiRecordEntity();
//        //设置userId
//        String userId = getUserId();
//        entity.setUserId(userId != null ? Integer.valueOf(userId) : null);
//        //设置appType
//        if(request.getRequestURL().toString().contains("/manage/")){
//            entity.setAppType(9);
//        }else {
//            String appType = getHeader(RequestHeaderConstant.APP_TYPE);
//            entity.setAppType(appType != null ? Integer.valueOf(appType) : null);
//        }
//        //设置IP
//        entity.setIp(IpUtil.getIp(request));
//        //设置URI
//        entity.setUrl(request.getRequestURI());
//        //设置method
//        entity.setMethod(request.getMethod());
//        //设置参数
//        if("GET".equals(request.getMethod())){
//            Map<String,Object> params = new HashMap<>();
//            Enumeration<String> enumeration = request.getParameterNames();
//            while (enumeration.hasMoreElements()){
//                String paramName = enumeration.nextElement();
//                params.put(paramName,request.getParameter(paramName));
//            }
//            entity.setParam(params.isEmpty() ? null : JSON.toJSONString(params));
//        }else if("POST".equals(request.getMethod())){
//            String requestBody = null;
//            try {
//                requestBody = IoUtil.read(request.getInputStream(),"UTF-8");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            entity.setParam(requestBody);
//        }else {
//            filterChain.doFilter(servletRequest,servletResponse);
//            return;
//        }
//        filterChain.doFilter(servletRequest,servletResponse);
//        Boolean should = servletResponse.getContentType().contains("application/json");
//        //设置执行时间
//        String[] execTime = {null};
//        entity.setTime(execTime[0] != null ? Integer.valueOf(execTime[0]) : null);
//        //设置结果
////        if(should){
////            entity.setResult(IoUtil.w(servletResponse.getOutputStream(),""));
////        }
//        entity.setUa(request.getHeader("User-Agent"));
//        entity.setCreateTime(new Date());
//        rabbitTemplate.convertAndSend(WindMqConfig.EXCHANGE,WindMqConfig.KEY_API,entity);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
