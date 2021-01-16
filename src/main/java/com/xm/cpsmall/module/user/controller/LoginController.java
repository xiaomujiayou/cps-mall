package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.comm.shiro.token.ManageToken;
import com.xm.cpsmall.comm.shiro.token.WeChatToken;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.serialize.form.AdminLoginForm;
import com.xm.cpsmall.module.user.serialize.form.GetUserInfoForm;
import com.xm.cpsmall.module.user.serialize.form.WechatLoginForm;
import com.xm.cpsmall.module.user.serialize.vo.SuAdminVo;
import com.xm.cpsmall.module.user.service.UserService;
import com.xm.cpsmall.utils.IpUtil;
import com.xm.cpsmall.utils.RequestHeaderConstant;
import com.xm.cpsmall.utils.response.MsgEnum;
import com.xm.cpsmall.utils.response.R;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserController userController;

    /**
     * 前台登录
     * @param wechatLoginForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/login")
    public Object login(@Valid @RequestBody WechatLoginForm wechatLoginForm, BindingResult bindingResult, HttpServletRequest request){
        GetUserInfoForm form = new GetUserInfoForm();
        form.setCode(wechatLoginForm.getCode());
        form.setShareUserId(wechatLoginForm.getShareUserId());
        form.setFrom(wechatLoginForm.getFrom());
        form.setIp(IpUtil.getIp(request));
        SuUserEntity msg = null;
        try {
            msg = userController.info(form);
        }catch (Exception e){
            e.printStackTrace();
            return R.error(MsgEnum.UNKNOWN_ERROR);
        }
        if(!SecurityUtils.getSubject().isAuthenticated()){
            WeChatToken token = new WeChatToken(msg.getOpenId());
            SecurityUtils.getSubject().login(token);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("token", SecurityUtils.getSubject().getSession().getId());
        SuUserEntity userEntity = (SuUserEntity) SecurityUtils.getSubject().getPrincipal();
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("nickname",userEntity.getNickname());
        userInfo.put("userSn",userEntity.getUserSn());
        userInfo.put("headImg",userEntity.getHeadImg());
        userInfo.put("id",userEntity.getId());
        userInfo.put("sex",userEntity.getSex());
        userInfo.put("state",userEntity.getState());
        result.put("userInfo",userInfo);
        request.getSession().setAttribute(RequestHeaderConstant.USER_INFO,userEntity);
        return result;
    }

    /**
     * 后台登陆
     */
    @PostMapping("/manage/logout")
    public void manageLogout(){
        if(SecurityUtils.getSubject().isAuthenticated()){
            SecurityUtils.getSubject().logout();
        }
    }

    /**
     * 后台登录
     */
    @PostMapping("/manage/login")
    public Object manageLogin(@Valid @RequestBody AdminLoginForm adminLoginForm, BindingResult bindingResult){
        if(!SecurityUtils.getSubject().isAuthenticated()){
            ManageToken token = new ManageToken(adminLoginForm.getUserName(),adminLoginForm.getPassword(),true);
            try {
                SecurityUtils.getSubject().login(token);
            }catch (Exception e){
                return R.error(MsgEnum.SYSTEM_LOGIN_ERROR);
            }
        }
        ManageToken token = (ManageToken) SecurityUtils.getSubject().getPrincipal();
        SuAdminVo suAdminVo = token.getSuAdminVo();
        suAdminVo.setToken(SecurityUtils.getSubject().getSession().getId());
        return suAdminVo;
    }
}
