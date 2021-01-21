package com.xm.cpsmall.comm.shiro.realm;

import com.xm.cpsmall.comm.shiro.token.ManageToken;
import com.xm.cpsmall.module.user.controller.manage.ManageController;
import com.xm.cpsmall.module.user.serialize.form.AdminLoginForm;
import com.xm.cpsmall.module.user.serialize.vo.SuAdminVo;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 后台管理接口
 */
@Component
public class ManageRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private ManageController manageController;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        ManageToken manageToken = (ManageToken)token;
        AdminLoginForm adminLoginForm = new AdminLoginForm();
        adminLoginForm.setUserName(manageToken.getUsername());
        adminLoginForm.setPassword(String.valueOf(manageToken.getPassword()));
        SuAdminVo suAdminVo = manageController.info(adminLoginForm);
        manageToken.setSuAdminVo(suAdminVo);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(manageToken,suAdminVo.getId() != null?manageToken.getPassword():"",getName());
        return authenticationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ManageToken;
    }
}
