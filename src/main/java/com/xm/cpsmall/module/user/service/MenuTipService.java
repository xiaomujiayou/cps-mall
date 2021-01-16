package com.xm.cpsmall.module.user.service;

import com.xm.cpsmall.module.user.serialize.vo.MenuTipVo;

import java.util.List;

/**
 * 用户菜单提示（数字、小红点）
 * 数字显示优先级高于小红点
 */
public interface MenuTipService {

    /**
     * 数字+1
     */
    public void addNum(Integer userId, List<Integer> menuIds);

    /**
     * 添加小红点
     */
    public void addRedPoint(Integer userId, List<Integer> menuIds);

    /**
     * 清除提示
     */
    public void del(Integer userId, List<Integer> menuIds);

    /**
     * 查询提示信息
     */
    public List<MenuTipVo> get(Integer userId, List<Integer> menuIds);
}
