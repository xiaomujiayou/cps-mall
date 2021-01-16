package com.xm.cpsmall.module.mall.service.api.impl.wph;

import cn.hutool.core.util.StrUtil;
import com.xm.cpsmall.component.WphSdkComponent;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.serialize.bo.ProductCriteriaBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.*;
import com.xm.cpsmall.module.mall.service.api.impl.abs.GoodsListServiceAbs;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("wphGoodsListService")
public class GoodsListServiceImpl extends GoodsListServiceAbs {

    @Autowired
    private WphSdkComponent wphSdkComponent;

    @Override
    public PageBean<SmProductEntityEx> index(GoodsListForm goodsListForm) throws Exception {
        //出单爆款
        return wphSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                wphSdkComponent.optSearch(
                        goodsListForm,
                        0));
    }

    @Override
    public PageBean<SmProductEntityEx> recommend(GoodsListForm goodsListForm) throws Exception {
        //每日精选
        return wphSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                wphSdkComponent.optSearch(
                        goodsListForm,
                        1));
    }

    @Override
    public PageBean<SmProductEntityEx> my(GoodsListForm goodsListForm) throws Exception {
        //超高佣
//        return wphSdkComponent.convertSmProductEntityEx(
//                goodsListForm.getUserId(),
//                wphSdkComponent.optSearch(
//                        goodsListForm,
//                        3));
        ProductCriteriaBo productCriteriaBo = convertCriteriaBo(goodsListForm);
        productCriteriaBo.setOptionId(null);
        productCriteriaBo.setKeyword("百货");
        return wphSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                wphSdkComponent.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> keyword(KeywordGoodsListForm keywordGoodsListForm) throws Exception {
        if(keywordGoodsListForm.getKeywords() == null || keywordGoodsListForm.getKeywords().trim().equals(""))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"keywords 不能为空");
        ProductCriteriaBo productCriteriaBo = convertCriteriaBo(keywordGoodsListForm);
        return wphSdkComponent.convertSmProductEntityEx(
                keywordGoodsListForm.getUserId(),
                wphSdkComponent.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> option(OptionGoodsListForm optionGoodsListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = convertCriteriaBo(optionGoodsListForm);
        productCriteriaBo.setOptionId(null);
        productCriteriaBo.setKeyword(convertOptionToName(optionGoodsListForm));
        return wphSdkComponent.convertSmProductEntityEx(
                optionGoodsListForm.getUserId(),
                wphSdkComponent.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> similar(SimilarGoodsListForm similarGoodsListForm) throws Exception {
        if(StrUtil.isBlank(similarGoodsListForm.getGoodsId()))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"goodsId 不能为空");
        SmProductEntity smProductEntity = wphSdkComponent.detail(similarGoodsListForm.getGoodsId());
        ProductCriteriaBo productCriteriaBo = convertCriteriaBo(similarGoodsListForm);
        productCriteriaBo.setOrderBy(3);
        productCriteriaBo.setKeyword(smProductEntity.getName());
        PageBean<SmProductEntity> smProductEntityPageBean  = wphSdkComponent.getProductByCriteria(productCriteriaBo);
        if(smProductEntityPageBean != null && !smProductEntityPageBean.getList().isEmpty()){
            smProductEntityPageBean.setList(
                    smProductEntityPageBean.getList()
                            .stream()
                            .filter(o -> !similarGoodsListForm.getGoodsId().equals(o.getGoodsId()))
                            .collect(Collectors.toList()));
        }
        return wphSdkComponent.convertSmProductEntityEx(
                similarGoodsListForm.getUserId(),
                smProductEntityPageBean
                );
    }

    @Override
    public PageBean<SmProductEntityEx> mall(MallGoodsListForm mallGoodsListForm) throws Exception {
        return null;
    }
}
