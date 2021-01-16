package com.xm.cpsmall.module.user.controller.manage;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.module.user.mapper.SuOrderMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.module.user.serialize.form.OrderSearchForm;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@RestController
@RequestMapping("/api-user/manage/order")
public class ManageOrderController {

    @Autowired
    private SuOrderMapper suOrderMapper;

    /**
     * 查询订单
     */
    @GetMapping
    public PageBean<SuOrderEntity> getOrder(OrderSearchForm orderSearchForm){
        PageHelper.startPage(orderSearchForm.getPageNum(),orderSearchForm.getPageSize());
        OrderByHelper.orderBy("create_time desc");
        List<SuOrderEntity> list = suOrderMapper.selectByExample(formToExample(orderSearchForm));
        return new PageBean<SuOrderEntity>(list);
    }

    /**
     * 订单计数
     */
    @GetMapping("/count")
    public Integer getOrderCount(OrderSearchForm orderSearchForm){
        return suOrderMapper.selectCountByExample(formToExample(orderSearchForm));
    }

    private Example formToExample(OrderSearchForm form) {
        Example example = new Example(SuOrderEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (form.getId() != null)
            criteria.andEqualTo("id", form.getId());
        if (form.getUserId() != null)
            criteria.andEqualTo("userId", form.getUserId());
        if (form.getShareUserId() != null)
            criteria.andEqualTo("shareUserId", form.getShareUserId());
        if (StrUtil.isNotBlank(form.getOrderSn()))
            criteria.andEqualTo("orderSn", form.getOrderSn());
        if (StrUtil.isNotBlank(form.getOrderSubSn()))
            criteria.andEqualTo("orderSubSn", form.getOrderSubSn());
        if (StrUtil.isNotBlank(form.getProductId()))
            criteria.andEqualTo("productId", form.getProductId());
        if (StrUtil.isNotBlank(form.getProductName()))
            criteria.andLike("productName", "%" + form.getProductName() + "%");
        if (form.getPlatformType() != null)
            criteria.andEqualTo("platformType", form.getPlatformType());
        if (form.getState() != null)
            criteria.andEqualTo("state", form.getState());
        if (form.getPId() != null)
            criteria.andEqualTo("pId", form.getPId());
        if(ObjectUtil.isAllNotEmpty(form.getPromotionAmountMin(),form.getPromotionAmountMax()))
            criteria.andBetween("promotionAmount",form.getPromotionAmountMin(),form.getPromotionAmountMax());
        if (form.getFormType() != null)
            criteria.andEqualTo("formType", form.getFormType());
        if (form.getFromApp() != null)
            criteria.andEqualTo("fromApp", form.getFromApp());
        if(ObjectUtil.isAllNotEmpty(form.getCreateStart(),form.getCreateEnd()))
            criteria.andBetween("createTime",form.getCreateStart(),form.getCreateEnd());
        return example;
    }

}
