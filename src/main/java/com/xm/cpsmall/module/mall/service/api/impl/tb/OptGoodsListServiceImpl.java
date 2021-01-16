package com.xm.cpsmall.module.mall.service.api.impl.tb;

import com.xm.cpsmall.comm.api.config.TbApiConfig;
import com.xm.cpsmall.component.TbSdkComponent;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsListForm;
import com.xm.cpsmall.module.mall.serialize.form.ThemeGoodsListForm;
import com.xm.cpsmall.module.mall.service.api.OptGoodsListService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tbOptGoodsListService")
public class OptGoodsListServiceImpl implements OptGoodsListService {

    @Autowired
    private TbApiConfig tbApiConfig;
    @Autowired
    private TbSdkComponent tbSdkComponent;


    @Override
    public PageBean<SmProductEntityEx> theme(ThemeGoodsListForm themeGoodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> one(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        4093L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> two(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        4094L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> three(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        4041L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> four(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        3786L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> five(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        13366L,
                        null));
    }
}
