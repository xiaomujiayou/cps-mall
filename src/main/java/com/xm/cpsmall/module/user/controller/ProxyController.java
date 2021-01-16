package com.xm.cpsmall.module.user.controller;

import cn.hutool.core.date.DateUtil;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.module.user.serialize.dto.ProxyProfitDto;
import com.xm.cpsmall.module.user.serialize.form.GetProxyProfitForm;
import com.xm.cpsmall.module.user.serialize.vo.ProxyInfoVo;
import com.xm.cpsmall.module.user.service.UserService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api-user/proxy")
public class ProxyController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户下级代理
     * @param userId
     * @return
     */
    @GetMapping("/profit")
    public PageBean<ProxyProfitDto> get(@LoginUser Integer userId, @Valid GetProxyProfitForm getProxyProfitForm, BindingResult bindingResult){
        PageBean<ProxyProfitDto> pageBean = userService.getProxyProfit(
                userId,
                getProxyProfitForm.getState() == null? null:getProxyProfitForm.getState() == 0?null:getProxyProfitForm.getState(),
                getProxyProfitForm.getOrderColumn(),
                getProxyProfitForm.getOrderBy(),
                getProxyProfitForm.getPageNum(),
                getProxyProfitForm.getPageSize());
        pageBean.getList().stream().forEach(o -> {
//            o.setProxyName(StrUtil.hide(o.getProxyName(),3,4));
            o.setCreateTime(DateUtil.format(DateUtil.parse(o.getCreateTime()),"MM-dd HH:mm"));
        });
        return pageBean;
    }

    /**
     *  获取用户代理详情
     */
    @GetMapping("/info")
    public ProxyInfoVo getInfo(@LoginUser Integer userId){
        return userService.getProxyInfo(userId);
    }
}
