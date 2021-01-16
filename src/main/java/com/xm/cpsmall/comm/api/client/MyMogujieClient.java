package com.xm.cpsmall.comm.api.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mogujie.openapi.MogujieClient;
import com.mogujie.openapi.exceptions.ApiException;
import com.mogujie.openapi.request.MgjRequest;
import com.mogujie.openapi.response.MgjResponse;
import com.xm.cpsmall.comm.api.config.MgjApiConfig;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MyMogujieClient extends MogujieClient {

    private MgjApiConfig mgjApiConfig;

    private StringRedisTemplate stringRedisTemplate;

    public MyMogujieClient(MgjApiConfig mgjApiConfig, StringRedisTemplate stringRedisTemplate) {
        super(mgjApiConfig.getAppKey(), mgjApiConfig.getAppSecret(), mgjApiConfig.getUrl());
        this.mgjApiConfig = mgjApiConfig;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public <R extends MgjResponse> R execute(MgjRequest<R> request) throws ApiException {
        String token = stringRedisTemplate.opsForValue().get(MgjApiConfig.MGJ_API_TOKEN_REDIS_KEY);
        if(StrUtil.isBlank(token)){
            token = refreshToken();
        }
        MgjResponse response = super.execute(request,token);
        if(response.getStatus().getCode().equals("0000000"))
            return (R)response;
        if(response.getStatus().getCode().equals("0000001")){
            refreshToken();
            return this.execute(request);
        }
        return (R)response;
    }
    public String refreshToken(){
        //刷新token
        String refreshToken = stringRedisTemplate.opsForValue().get(MgjApiConfig.MGJ_API_REFRESH_TOKEN_REDIS_KEY);
        if(StrUtil.isBlank(refreshToken))
            throw new GlobleException(MsgEnum.SYSTEM_TOKEN_ERROR,String.format("蘑菇街refreshToken获取失败"));
        return refreshToken(2,null,refreshToken);
    }

    /**
     * 获取token
     * @param type  1：生成token 2：刷新token
     * @param refreshToken
     * @return
     */
    public String refreshToken(Integer type ,String code, String refreshToken){
        String grantType = type == 1 ? "authorization_code":"refresh_token";
        String apiUrl = "https://oauth.mogujie.com/token";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("app_key",mgjApiConfig.getAppKey());
        paramMap.put("app_secret",mgjApiConfig.getAppSecret());
        paramMap.put("grant_type",grantType);
        if(StrUtil.isNotBlank(refreshToken))
            paramMap.put("refresh_token",refreshToken);
        if(StrUtil.isNotBlank(code))
            paramMap.put("code",code);
        paramMap.put("state","");
        HttpResponse response =  HttpUtil.createGet(apiUrl).form(paramMap).execute();
        JSONObject jsonRes = JSON.parseObject(response.body());
        log.debug("蘑菇街api 刷新 token:[{}]",jsonRes.toJSONString());
        if(!jsonRes.getString("statusCode").equals("0000000"))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,String.format("蘑菇街token获取失败:[%s]",jsonRes.toJSONString()));
        stringRedisTemplate.opsForValue().set(MgjApiConfig.MGJ_API_TOKEN_REDIS_KEY,jsonRes.getString("access_token"));
        stringRedisTemplate.opsForValue().set(MgjApiConfig.MGJ_API_REFRESH_TOKEN_REDIS_KEY,jsonRes.getString("refresh_token"));
        return jsonRes.getString("access_token");
    }
}