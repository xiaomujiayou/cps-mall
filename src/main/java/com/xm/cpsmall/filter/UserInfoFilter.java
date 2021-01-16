//package com.xm.cpsmall.filter;
//
//import cn.hutool.core.codec.Base64;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.xm.cloud_gateway.config.ShiroConfig;
//import com.xm.comment_serialize.module.gateway.constant.RequestHeaderConstant;
//import com.xm.comment_serialize.module.user.entity.SuUserEntity;
//import com.xm.cpsmall.config.ShiroConfig;
//import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.session.UnknownSessionException;
//import org.apache.shiro.session.mgt.SessionKey;
//import org.apache.shiro.subject.SimplePrincipalCollection;
//import org.apache.shiro.subject.support.DefaultSubjectContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.io.Serializable;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
//
///**
// * 用户信息过滤器(前台)
// * 如果用户已登录并且存在，则将用户信息设置到request header中，以便其他服务通过@LoginUser注解获取
// * 向service服务表明该请求来源于zuul，以便ExceptionAspect正常工作
// */
//@Slf4j
//@Component
//public class UserInfoFilter implements Filter {
//
//    @Autowired
//    private ShiroConfig shiroConfig;
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
//        //放过后台接口
//        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
//        return !request.getRequestURL().toString().contains("/manage/");
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        //向service表明该请求来自zuul，以便触发ExceptionAspect工作
//        requestContext.addZuulRequestHeader("from", "zuul");
//        HttpServletRequest request = requestContext.getRequest();
//        String token = request.getHeader(shiroConfig.getSessionIdName());
//
//        if(StrUtil.isBlank(token))
//            return null;
//
//        Session session = null;
//        try {
//            session = SecurityUtils.getSecurityManager().getSession(new MySessionKey(token));
//            if((Boolean) session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY)){
//                Object user = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
//                SimplePrincipalCollection principal = (SimplePrincipalCollection)user;
//                SuUserEntity suUserEntity = principal.oneByType(SuUserEntity.class);
//                if(user != null){
//                    requestContext.addZuulRequestHeader(RequestHeaderConstant.USER_INFO, Base64.encode(JSON.toJSONString(suUserEntity)));
//                    requestContext.addZuulRequestHeader(RequestHeaderConstant.USER_ID, suUserEntity.getId().toString());
//                }
//            }
//
//        }catch (UnknownSessionException e){
//            log.debug("无效 token：{}",token);
//        }
//        return null;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    public static class MySessionKey implements SessionKey {
//
//        private String token;
//
//        public MySessionKey(String token) {
//            this.token = token;
//        }
//
//        @Override
//        public Serializable getSessionId() {
//            return token;
//        }
//    }
//}
