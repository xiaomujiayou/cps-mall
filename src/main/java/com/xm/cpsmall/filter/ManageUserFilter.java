//package com.xm.cpsmall.filter;
//
//import cn.hutool.core.codec.Base64;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.xm.cloud_gateway.config.ShiroConfig;
//import com.xm.cloud_gateway.shiro.token.ManageToken;
//import com.xm.comment_serialize.module.gateway.constant.RequestHeaderConstant;
//import com.xm.comment_serialize.module.user.vo.SuAdminVo;
//import com.xm.comment_utils.exception.GlobleException;
//import com.xm.comment_utils.response.MsgEnum;
//import com.xm.cpsmall.config.ShiroConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.session.UnknownSessionException;
//import org.apache.shiro.subject.SimplePrincipalCollection;
//import org.apache.shiro.subject.support.DefaultSubjectContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
//
///**
// * 后台接口鉴权
// */
//@Component
//@Slf4j
//public class ManageUserFilter extends ZuulFilter {
//
//    @Autowired
//    private ShiroConfig shiroConfig;
//
//    @Override
//    public boolean shouldFilter() {
//        //只拦截后台接口
//        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
//        return request.getRequestURL().toString().contains("/manage/");
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = requestContext.getRequest();
//        String token = request.getHeader(shiroConfig.getSessionIdName());
//        if(StrUtil.isBlank(token)){
//            authError(requestContext);
//            return null;
//        }
//        Session session = null;
//        try {
//            session = SecurityUtils.getSecurityManager().getSession(new UserInfoFilter.MySessionKey(token));
//            if((Boolean) session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY)){
//                Object user = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
//                SimplePrincipalCollection principal = (SimplePrincipalCollection)user;
//                ManageToken manageToken = (ManageToken)principal.getPrimaryPrincipal();
//                SuAdminVo suAdminVo = manageToken.getSuAdminVo();
//                if(suAdminVo != null){
//                    requestContext.addZuulRequestHeader(RequestHeaderConstant.MANAGE_USER_INFO, Base64.encode(JSON.toJSONString(suAdminVo)));
//                    requestContext.addZuulRequestHeader(RequestHeaderConstant.MANAGE_USER_ID, suAdminVo.getId().toString());
//                    return null;
//                }else{
//                    authError(requestContext);
//                    return null;
//                }
//            }
//        }catch (UnknownSessionException e){
//            log.debug("无效 token：{}",token);
//        }
//        authError(requestContext);
//        return null;
//    }
//
//    private void authError(RequestContext context){
//        throw new GlobleException(MsgEnum.SYSTEM_AUTH_ERROR);
////        context.addZuulResponseHeader("Content-Type","application/json");
////        context.getResponse().setCharacterEncoding("UTF-8");
////        context.setResponseBody(JSON.toJSONString(R.error(MsgEnum.SYSTEM_AUTH_ERROR)));
//    }
//}
