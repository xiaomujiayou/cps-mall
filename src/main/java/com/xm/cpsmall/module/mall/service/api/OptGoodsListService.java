package com.xm.cpsmall.module.mall.service.api;

import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsListForm;
import com.xm.cpsmall.module.mall.serialize.form.ThemeGoodsListForm;
import com.xm.cpsmall.utils.mybatis.PageBean;

/**
 * 类目商品列表
 */
public interface OptGoodsListService {

    /**
     * 主题活动列表
     * @param themeGoodsListForm
     * @return
     * @throws Exception
     */
    public PageBean<SmProductEntityEx> theme(ThemeGoodsListForm themeGoodsListForm) throws Exception;

    /**
     * 首页滑动列表第一个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> one(GoodsListForm goodsListForm) throws Exception{return null;};

    /**
     * 首页滑动列表第二个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> two(GoodsListForm goodsListForm) throws Exception{return null;};

    /**
     * 首页滑动列表第三个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> three(GoodsListForm goodsListForm) throws Exception{return null;};

    /**
     * 首页滑动列表第四个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> four(GoodsListForm goodsListForm) throws Exception{return null;};

    /**
     * 首页滑动列表第五个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> five(GoodsListForm goodsListForm) throws Exception{return null;};
    /**
     * 首页滑动列表第6个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> six(GoodsListForm goodsListForm) throws Exception{return null;};
    /**
     * 首页滑动列表第7个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> seven(GoodsListForm goodsListForm) throws Exception{return null;};
    /**
     * 首页滑动列表第8个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> eight(GoodsListForm goodsListForm) throws Exception{return null;};
    /**
     * 首页滑动列表第9个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> nine(GoodsListForm goodsListForm) throws Exception{return null;};
    /**
     * 首页滑动列表第10个
     * @param goodsListForm
     * @return
     * @throws Exception
     */
    default PageBean<SmProductEntityEx> ten(GoodsListForm goodsListForm) throws Exception{return null;};
}
