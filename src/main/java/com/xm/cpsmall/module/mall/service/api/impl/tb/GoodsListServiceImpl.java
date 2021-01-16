package com.xm.cpsmall.module.mall.service.api.impl.tb;

import cn.hutool.core.bean.BeanUtil;
import com.xm.cpsmall.comm.api.config.TbApiConfig;
import com.xm.cpsmall.component.TbSdkComponent;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.serialize.bo.ProductCriteriaBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmOptEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.*;
import com.xm.cpsmall.module.mall.service.api.OptionService;
import com.xm.cpsmall.module.mall.service.api.impl.abs.GoodsListServiceAbs;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("tbGoodsListService")
public class GoodsListServiceImpl extends GoodsListServiceAbs {
    @Autowired
    private TbSdkComponent tbSdkComponent;
    @Autowired
    private OptionService optionService;
    @Autowired
    private TbApiConfig tbApiConfig;

    @Override
    public PageBean<SmProductEntityEx> index(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        28026L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> recommend(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        31362L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> my(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        6708L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> keyword(KeywordGoodsListForm keywordGoodsListForm) throws Exception {
        ProductCriteriaBo criteriaBo = new ProductCriteriaBo();
        criteriaBo.setUserId(keywordGoodsListForm.getUserId());
        criteriaBo.setHasCoupon(true);
        criteriaBo.setPageNum(keywordGoodsListForm.getPageNum());
        criteriaBo.setPageSize(keywordGoodsListForm.getPageSize());
        criteriaBo.setKeyword(keywordGoodsListForm.getKeywords());
        criteriaBo.setOrderBy(keywordGoodsListForm.getSort());
        criteriaBo.setMaxPrice(keywordGoodsListForm.getMaxPrice());
        criteriaBo.setMinPrice(keywordGoodsListForm.getMinPrice());
        criteriaBo.setHasCoupon(keywordGoodsListForm.getHasCoupon());
        return tbSdkComponent.convertSmProductEntityEx(
                keywordGoodsListForm.getUserId(),
                tbSdkComponent.getProductByCriteria(criteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> option(OptionGoodsListForm optionGoodsListForm) throws Exception {
        if(optionGoodsListForm.getOptionId() == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"goodsId 不能为空");


        OptionForm optionForm = new OptionForm();
        BeanUtil.copyProperties(optionGoodsListForm,optionForm);
        optionForm.setTargetOptId(optionGoodsListForm.getOptionId());
        List<SmOptEntity> optEntities = optionService.allParentList(optionForm);
        if(optEntities == null)
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR,"无效optId:"+optionGoodsListForm.getOptionId());
        String keyWords = String.join(" ",optEntities.stream().map(SmOptEntity::getName).collect(Collectors.toList()));
        KeywordGoodsListForm keywordGoodsListForm = new KeywordGoodsListForm();
        BeanUtil.copyProperties(optionGoodsListForm,keywordGoodsListForm);
        keywordGoodsListForm.setKeywords(keyWords);
        ProductCriteriaBo criteriaBo = new ProductCriteriaBo();
        criteriaBo.setUserId(optionGoodsListForm.getUserId());
        criteriaBo.setHasCoupon(true);
        criteriaBo.setPageNum(keywordGoodsListForm.getPageNum());
        criteriaBo.setPageSize(keywordGoodsListForm.getPageSize());
        criteriaBo.setKeyword(keywordGoodsListForm.getKeywords());

        return tbSdkComponent.convertSmProductEntityEx(
                keywordGoodsListForm.getUserId(),
                tbSdkComponent.getProductByCriteria(criteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> similar(SimilarGoodsListForm similarGoodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                similarGoodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        similarGoodsListForm,
                        tbApiConfig.getAdzoneId(),
                        13256L,
                        Long.valueOf(similarGoodsListForm.getGoodsId())));
    }

    @Override
    public PageBean<SmProductEntityEx> mall(MallGoodsListForm mallGoodsListForm) throws Exception {
        return null;
    }
}
