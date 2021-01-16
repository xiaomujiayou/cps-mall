package com.xm.cpsmall.module.mall.service.api.impl.mgj;

import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsListForm;
import com.xm.cpsmall.module.mall.serialize.form.ThemeGoodsListForm;
import com.xm.cpsmall.module.mall.service.api.OptGoodsListService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.stereotype.Service;

@Service("mgjOptGoodsListService")
public class OptGoodsListServiceImpl implements OptGoodsListService {


    @Override
    public PageBean<SmProductEntityEx> theme(ThemeGoodsListForm themeGoodsListForm) throws Exception {
        return null;
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
}
