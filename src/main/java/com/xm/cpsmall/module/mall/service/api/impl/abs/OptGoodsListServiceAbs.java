package com.xm.cpsmall.module.mall.service.api.impl.abs;

import cn.hutool.core.bean.BeanUtil;
import com.xm.cpsmall.component.PddSdkComponent;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsListForm;
import com.xm.cpsmall.module.mall.serialize.form.KeywordGoodsListForm;
import com.xm.cpsmall.module.mall.serialize.form.ThemeGoodsListForm;
import com.xm.cpsmall.module.mall.service.api.OptGoodsListService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class OptGoodsListServiceAbs implements OptGoodsListService {

    @Autowired
    private PddSdkComponent pddSdkComponent;

    @Override
    public PageBean<SmProductEntityEx> theme(ThemeGoodsListForm themeGoodsListForm) throws Exception {
        KeywordGoodsListForm keywordGoodsListForm = new KeywordGoodsListForm();
        BeanUtil.copyProperties(themeGoodsListForm,keywordGoodsListForm);
        return pddSdkComponent.keyworkSearch(themeGoodsListForm.getUserId(),themeGoodsListForm.getPid(),keywordGoodsListForm);
    }

    @Override
    public PageBean<SmProductEntityEx> one(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> two(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> three(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> four(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> five(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> six(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> seven(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> eight(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> nine(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> ten(GoodsListForm goodsListForm) throws Exception {
        return null;
    }
}

