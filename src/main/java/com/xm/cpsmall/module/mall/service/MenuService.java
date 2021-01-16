package com.xm.cpsmall.module.mall.service;

import com.xm.cpsmall.module.mall.serialize.entity.SmMenuEntity;
import com.xm.cpsmall.utils.form.BaseForm;

import java.util.List;

public interface MenuService {

    /**
     * 获取底部导航菜单
     * @param baseForm
     * @return
     */
    public List<SmMenuEntity> mainTabs(BaseForm baseForm);

    /**
     * 获取设置菜单
     * @param baseForm
     * @return
     */
    public List<SmMenuEntity> setting(BaseForm baseForm);
}
