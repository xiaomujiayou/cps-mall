package com.xm.cpsmall.filter;


import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xm.cpsmall.utils.response.MsgEnum;
import com.xm.cpsmall.utils.response.R;
import com.xm.cpsmall.utils.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 请求结果处理
 * 1.包装返回结果
 */
@Slf4j
@WebFilter(
        filterName = "resFilter",
        urlPatterns = {"*"}
)
@Component
public class ResFilter implements Filter {

    public static final ThreadLocal<Boolean> EXCEPTION_FLAG = new ThreadLocal<>();

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ResponseWrapper responseWrapper = response instanceof ResponseWrapper ? (ResponseWrapper) response : new ResponseWrapper((HttpServletResponse) response);
        //初始化请求异常状态
        EXCEPTION_FLAG.set(false);
        chain.doFilter(request, response);
        //异常状态则直接返回
        //除接口之外的其他请求返回原格式
        if (EXCEPTION_FLAG.get() || (request.getContentType() == null || !request.getContentType().contains(APPLICATION_JSON_VALUE)) && (response.getContentType() == null || !response.getContentType().contains(APPLICATION_JSON_VALUE))){
            response.setContentLength(responseWrapper.getContent().length);
            IoUtil.write(responseWrapper.getResponse().getOutputStream(), false, responseWrapper.getContent());
            return;
        }

        //处理接口返回
        String result = null;
        if(response.getContentType() == null && request.getContentType() != null && request.getContentType().contains(APPLICATION_JSON_VALUE)) {
            //接口没返回值，则表示请求成功
            result = JSON.toJSONString(R.sucess());
        }else if(responseWrapper.getContentType().contains(APPLICATION_JSON_VALUE)) {
            String content = new String(responseWrapper.getContent());
            Object resObj = JSON.parse(content);
            //已包含状态码code msg 的消息则原格式返回
            if(resObj instanceof JSONObject && ((JSONObject) resObj).containsKey("code") && ((JSONObject) resObj).containsKey("msg")){
                result = ((JSONObject) resObj).toJSONString();
            }else {
                //接口有返回值则包装消息状态
                result = JSON.toJSONString(R.sucess(resObj));
            }
        }else
            result = JSON.toJSONString(R.error(MsgEnum.UNKNOWN_ERROR,"未知请求类型！"));
        response.setContentLength(result.getBytes().length);
        IoUtil.write(responseWrapper.getResponse().getOutputStream(), false, result.getBytes());
    }

    @Override
    public void destroy() {

    }
}
