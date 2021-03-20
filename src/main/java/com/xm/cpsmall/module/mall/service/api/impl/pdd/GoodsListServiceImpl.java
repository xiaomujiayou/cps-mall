package com.xm.cpsmall.module.mall.service.api.impl.pdd;

import cn.hutool.core.util.StrUtil;
import com.xm.cpsmall.component.PddSdkComponent;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.mapper.SmOptMapper;
import com.xm.cpsmall.module.mall.serialize.bo.ProductCriteriaBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.*;
import com.xm.cpsmall.module.mall.service.api.GoodsListService;
import com.xm.cpsmall.module.user.serialize.form.AddSearchForm;
import com.xm.cpsmall.module.user.service.SearchService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("pddGoodsListService")
public class GoodsListServiceImpl implements GoodsListService {
    @Autowired
    private PddSdkComponent pddSdkComponent;
    @Autowired
    private SearchService searchService;
    @Autowired
    private SmOptMapper smOptMapper;

    @Override
    public PageBean<SmProductEntityEx> index(GoodsListForm goodsListForm) throws Exception {
        return pddSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                pddSdkComponent.getRecommendGoodsList(
                        goodsListForm.getPid(),
                        0,
                        goodsListForm.getPageNum(),
                        goodsListForm.getPageSize()));
    }

    @Override
    public PageBean<SmProductEntityEx> recommend(GoodsListForm goodsListForm) throws Exception {
        //热销榜
        return pddSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                pddSdkComponent.getTopGoodsList(
                        2,
                        goodsListForm.getPid(),
                        goodsListForm.getPageNum(),
                        goodsListForm.getPageSize()));
    }

    @Override
    public PageBean<SmProductEntityEx> my(GoodsListForm goodsListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setPid(goodsListForm.getPid());
        productCriteriaBo.setUserId(goodsListForm.getUserId());
        productCriteriaBo.setPageNum(goodsListForm.getPageNum());
        productCriteriaBo.setPageSize(goodsListForm.getPageSize());
        return pddSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                pddSdkComponent.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> keyword(KeywordGoodsListForm keywordGoodsListForm) throws Exception {
        if(keywordGoodsListForm.getKeywords() == null || keywordGoodsListForm.getKeywords().trim().equals(""))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"keywords 不能为空");
        //添加搜索历史
        if(keywordGoodsListForm.getPageNum() == 1 && keywordGoodsListForm.getUserId() != null){
            //只在搜索第一页添加
            AddSearchForm addSearchForm = new AddSearchForm();
            addSearchForm.setKeyWords(keywordGoodsListForm.getKeywords());
            searchService.add(keywordGoodsListForm.getUserId(),addSearchForm.getKeyWords());
        }
        return pddSdkComponent.keyworkSearch(keywordGoodsListForm.getUserId(),keywordGoodsListForm.getPid(),keywordGoodsListForm);
    }

    @Override
    public PageBean<SmProductEntityEx> option(OptionGoodsListForm optionGoodsListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setPid(optionGoodsListForm.getPid());
        productCriteriaBo.setUserId(optionGoodsListForm.getUserId());
        productCriteriaBo.setPageNum(optionGoodsListForm.getPageNum());
        productCriteriaBo.setPageSize(optionGoodsListForm.getPageSize());
        productCriteriaBo.setOptionId(
                        smOptMapper.selectByPrimaryKey(
                                optionGoodsListForm.getOptionId()).
                                getPddOptId());
        return pddSdkComponent.convertSmProductEntityEx(
                optionGoodsListForm.getUserId(),
                pddSdkComponent.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> similar(SimilarGoodsListForm similarGoodsListForm) throws Exception {
        if(StrUtil.isBlank(similarGoodsListForm.getGoodsId()))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"goodsId 不能为空");
        SmProductEntity smProductEntity = pddSdkComponent.detail(
                similarGoodsListForm.getGoodsId(),
                similarGoodsListForm.getPid());
        KeywordGoodsListForm keywordGoodsListForm = new KeywordGoodsListForm();
        keywordGoodsListForm.setKeywords(smProductEntity.getName());
        keywordGoodsListForm.setSort(3);
        keywordGoodsListForm.setPageNum(similarGoodsListForm.getPageNum());
        keywordGoodsListForm.setPageSize(similarGoodsListForm.getPageSize());
        PageBean<SmProductEntityEx> productEntityExPageBean = pddSdkComponent.keyworkSearch(
                similarGoodsListForm.getUserId(),
                similarGoodsListForm.getPid(),
                keywordGoodsListForm);
        productEntityExPageBean.setList(productEntityExPageBean.getList().stream().filter(o-> !smProductEntity.getGoodsId().equals(o.getGoodsId())).collect(Collectors.toList()));
        return productEntityExPageBean;
    }

    @Override
    public PageBean<SmProductEntityEx> mall(MallGoodsListForm mallGoodsListForm) throws Exception {
        return pddSdkComponent.convertSmProductEntityEx(
                mallGoodsListForm.getUserId(),
                pddSdkComponent.mallGoodsList(mallGoodsListForm));
    }


}
