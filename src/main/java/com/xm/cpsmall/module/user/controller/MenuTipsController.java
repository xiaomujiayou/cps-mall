package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.module.user.serialize.vo.MenuTipVo;
import com.xm.cpsmall.module.user.service.MenuTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api-user/menu/tips")
@RestController
public class MenuTipsController {

    @Autowired
    private MenuTipService menuTipService;

    /**
     * 数字+1
     */
    @PostMapping("/num")
    public void addNum(@LoginUser Integer userId, @RequestBody List<Integer> menuIds){
        menuTipService.addNum(userId,menuIds);
    }

    /**
     * 添加小红点
     */
    @PostMapping("/point")
    public void addRedPoint(@LoginUser Integer userId,@RequestBody List<Integer> menuIds){
        menuTipService.addRedPoint(userId,menuIds);
    }

    /**
     * 清除提示
     */
    @PostMapping("/del")
    public void del(@LoginUser Integer userId,@RequestBody List<Integer> menuIds){
        menuTipService.del(userId,menuIds);
    }

    /**
     * 查询提示信息
     */
    @GetMapping
    public List<MenuTipVo> get(@LoginUser Integer userId, @RequestParam("menuIds") List<Integer> menuIds){
        return menuTipService.get(userId,menuIds);
    }
}
