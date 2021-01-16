package com.xm.cpsmall.module.mall.service.api;

import com.xm.cpsmall.module.mall.serialize.entity.SmOptEntity;
import com.xm.cpsmall.module.mall.serialize.ex.OptEx;
import com.xm.cpsmall.module.mall.serialize.form.OptionForm;

import java.util.List;

/**
 * 商品类目服务
 */
public interface OptionService {

    /**
     * 获取所有类目
     * @return
     */
    List<OptEx> list(OptionForm optionForm);

    /**
     * 获取子类目
     * @param optionForm
     * @return
     */
    List<SmOptEntity> childList(OptionForm optionForm);

    /**
     * 获取所有父类目
     * @param optionForm
     * @return
     */
    List<SmOptEntity> allParentList(OptionForm optionForm);

    /**
     * 校验类目是否合法
     * @param optId
     * @return
     */
    public boolean check(String optId);
}
