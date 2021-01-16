package com.xm.cpsmall.comm.shiro.token;

import com.xm.cpsmall.module.user.serialize.vo.SuAdminVo;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 后台登录token
 */
public class ManageToken extends UsernamePasswordToken {
    public ManageToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    private SuAdminVo suAdminVo;

    public SuAdminVo getSuAdminVo() {
        return suAdminVo;
    }

    public void setSuAdminVo(SuAdminVo suAdminVo) {
        this.suAdminVo = suAdminVo;
    }
}
