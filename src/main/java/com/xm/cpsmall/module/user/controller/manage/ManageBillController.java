package com.xm.cpsmall.module.user.controller.manage;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.module.user.mapper.SuBillMapper;
import com.xm.cpsmall.module.user.mapper.custom.SuBillMapperEx;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.form.BillProfitSearchForm;
import com.xm.cpsmall.module.user.serialize.form.BillSearchForm;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@RestController
@RequestMapping("/api-user/manage/bill")
public class ManageBillController {

    @Autowired
    private SuBillMapper suBillMapper;
    @Autowired
    private SuBillMapperEx suBillMapperEx;

    /**
     * 查询账单
     */
    @GetMapping
    public PageBean<SuBillEntity> getBill(BillSearchForm billSearchForm) {
        PageHelper.startPage(billSearchForm.getPageNum(), billSearchForm.getPageSize());
        OrderByHelper.orderBy("create_time desc");
        List<SuBillEntity> list = suBillMapper.selectByExample(formToExample(billSearchForm));
        PageBean<SuBillEntity> result = new PageBean<>(list);
        return result;
    }

    /**
     * 账单统计
     */
    @GetMapping("/count")
    public Integer getBillCount(BillSearchForm billSearchForm) {
        return suBillMapper.selectCountByExample(formToExample(billSearchForm));
    }

    /**
     * 账单金额统计
     */
    @GetMapping("/profit")
    public Integer getBillProfit(BillProfitSearchForm form) {
        Integer result = suBillMapperEx.getBillProfit(form);
        return result == null ? 0 : result;
    }

    private Example formToExample(BillSearchForm form) {
        Example example = new Example(SuBillEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (form.getId() != null)
            criteria.andEqualTo("id", form.getId());
        if (form.getUserId() != null)
            criteria.andEqualTo("userId", form.getUserId());
        if (form.getFromUserId() != null)
            criteria.andEqualTo("fromUserId", form.getFromUserId());
        if (StrUtil.isNotBlank(form.getBillSn()))
            criteria.andEqualTo("billSn", form.getBillSn());
        if (form.getType() != null)
            criteria.andEqualTo("type", form.getType());
        if (form.getAttach() != null)
            criteria.andEqualTo("attach", form.getAttach());
        if (form.getState() != null)
            criteria.andEqualTo("state", form.getState());
        if (form.getCreditState() != null)
            criteria.andEqualTo("creditState", form.getCreditState());
        if (ObjectUtil.isAllNotEmpty(form.getCreateStart(), form.getCreateEnd()))
            criteria.andBetween("createTime", form.getCreateStart(), form.getCreateEnd());
        if (ObjectUtil.isAllNotEmpty(form.getPayTimeStart(), form.getPayTimeEnd()))
            criteria.andBetween("payTime", form.getPayTimeStart(), form.getPayTimeEnd());
        return example;
    }
}
