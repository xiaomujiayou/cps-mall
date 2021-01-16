package com.xm.cpsmall.comm.api.controller;

import cn.hutool.core.util.StrUtil;
import com.xm.cpsmall.comm.api.client.MyMogujieClient;
import com.xm.cpsmall.comm.api.config.MgjApiConfig;
import com.xm.cpsmall.utils.response.Msg;
import com.xm.cpsmall.utils.response.MsgEnum;
import com.xm.cpsmall.utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ApiController {

    @Autowired
    private MgjApiConfig mgjApiConfig;

    @Autowired
    private MyMogujieClient myMogujieClient;


    /**
     * 蘑菇街获取api token
     * @return
     */
    @GetMapping("/mgj/login")
    public Msg getLoginUrl(){
        String loginUrl = "https://oauth.mogujie.com/authorize?response_type=code&app_key=%s&redirect_uri=%s&state=";
        return R.sucess(String.format(loginUrl,mgjApiConfig.getAppKey(),mgjApiConfig.getRedirectUri()));
    }
    /**
     * 蘑菇街获取api token
     * @return
     */
    @GetMapping("/mgj/token")
    public Msg getToken(String code){
        if(StrUtil.isBlank(code))
            return R.error(MsgEnum.PARAM_VALID_ERROR,"code获取失败");
        myMogujieClient.refreshToken(1,code,null);
        return R.sucess();
    }
}
