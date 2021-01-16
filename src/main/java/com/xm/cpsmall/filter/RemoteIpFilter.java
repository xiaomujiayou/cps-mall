//package com.xm.cpsmall.filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.xm.cloud_gateway.util.IpUtil;
//import com.xm.comment_serialize.module.gateway.constant.RequestHeaderConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
//
///**
// * 用户信息过滤器
// * 如果用户已登录并且存在，则将用户信息设置到request header中，以便其他服务通过@LoginUser注解获取
// * 向service服务表明该请求来源于zuul，以便ExceptionAspect正常工作
// */
//@Slf4j
//@Component
//public class RemoteIpFilter extends ZuulFilter {
//
//
//    @Override
//    public String filterType() {
//        return PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return PRE_DECORATION_FILTER_ORDER -1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        String remoteAddr = IpUtil.getIp(request);
//        ctx.getZuulRequestHeaders().put(RequestHeaderConstant.IP, remoteAddr);
//        return null;
//    }
//
//
//}
