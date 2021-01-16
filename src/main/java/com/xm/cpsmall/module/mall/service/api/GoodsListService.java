package com.xm.cpsmall.module.mall.service.api;

import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.*;
import com.xm.cpsmall.utils.mybatis.PageBean;

/**
 * 商品列表
 */
public interface GoodsListService {

    /**
     * 首页列表
     */
    public PageBean<SmProductEntityEx> index(GoodsListForm goodsListForm) throws Exception;

    /**
     * 推荐列表
     */
    public PageBean<SmProductEntityEx> recommend(GoodsListForm goodsListForm) throws Exception;

    /**
     * 我的页面列表
     */
    public PageBean<SmProductEntityEx> my(GoodsListForm goodsListForm) throws Exception;

    /**
     * 关键字商品查询
     * @param keywordGoodsListForm
     * @return
     * @throws Exception
     */
    public PageBean<SmProductEntityEx> keyword(KeywordGoodsListForm keywordGoodsListForm) throws Exception;

    /**
     * 类目商品查询
     * @param optionGoodsListForm
     * @return
     * @throws Exception
     */
    public PageBean<SmProductEntityEx> option(OptionGoodsListForm optionGoodsListForm) throws Exception;

    /**
     * 类似商品查询
     * @param similarGoodsListForm
     * @return
     * @throws Exception
     */
    public PageBean<SmProductEntityEx> similar(SimilarGoodsListForm similarGoodsListForm) throws Exception;

    /**
     * 店铺商品查询
     * @param mallGoodsListForm
     * @return
     * @throws Exception
     */
    public PageBean<SmProductEntityEx> mall(MallGoodsListForm mallGoodsListForm) throws Exception;


}
