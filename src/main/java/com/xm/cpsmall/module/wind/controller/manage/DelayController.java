package com.xm.cpsmall.module.wind.controller.manage;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.module.wind.mapper.SwApiRecordMapper;
import com.xm.cpsmall.module.wind.mapper.custom.SwApiRecordMapperEx;
import com.xm.cpsmall.module.wind.serialize.entity.SwApiRecordEntity;
import com.xm.cpsmall.module.wind.serialize.form.DelayForm;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@RestController
@RequestMapping("/wind-control/manage/delay")
public class DelayController {

    @Autowired
    private SwApiRecordMapper swApiRecordMapper;
    @Autowired
    private SwApiRecordMapperEx swApiRecordMapperEx;


    /**
     * 查询API记录
     */
    @GetMapping
    public PageBean<SwApiRecordEntity> getApi(DelayForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        OrderByHelper.orderBy("create_time desc");
        List<SwApiRecordEntity> list = swApiRecordMapper.selectByExample(formToExample(form));
        PageBean<SwApiRecordEntity> result = new PageBean<>(list);
        return result;
    }

    /**
     * API记录统计
     */
    @GetMapping("/count")
    public Integer getApiCount(DelayForm form) {
        return swApiRecordMapper.selectCountByExample(formToExample(form));
    }

    /**
     * API平均延时统计
     */
    @GetMapping("/average")
    public Integer getApiAverage(DelayForm form) {
        Integer result = swApiRecordMapperEx.getApiAverage(form);
        return result == null ? 0 : result;
    }

    private Example formToExample(DelayForm form) {
        Example example = new Example(SwApiRecordEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (form.getUserId() != null)
            criteria.andEqualTo("userId", form.getUserId());
        if (form.getAppType() != null)
            criteria.andEqualTo("appType", form.getAppType());
        if (StrUtil.isNotBlank(form.getUrl()))
            criteria.andEqualTo("url", form.getUrl());
        if (StrUtil.isNotBlank(form.getIpAddr()))
            criteria.andLike("ipAddr", "%" + form.getIpAddr() + "%");
        if (StrUtil.isNotBlank(form.getMethod()))
            criteria.andEqualTo("method", form.getMethod());
        if (form.getTimeMin() != null)
            criteria.andGreaterThanOrEqualTo("time", form.getTimeMin());
        if (form.getTimeMax() != null)
            criteria.andLessThanOrEqualTo("time", form.getTimeMax());
        if (ObjectUtil.isAllNotEmpty(form.getCreateStart(), form.getCreateEnd()))
            criteria.andBetween("createTime", form.getCreateStart(), form.getCreateEnd());
        return example;
    }
}
