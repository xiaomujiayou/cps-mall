package com.xm.cpsmall.comm.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 前台登录token
 */
public class WeChatToken implements AuthenticationToken {

    private String openId;

    public WeChatToken(String openId) {
        this.openId = openId;
    }

    @Override
    public Object getPrincipal() {
        return openId;
    }

    @Override
    public Object getCredentials() {
        return "";
    }


}
