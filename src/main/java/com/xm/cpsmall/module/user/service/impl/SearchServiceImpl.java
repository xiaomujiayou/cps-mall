package com.xm.cpsmall.module.user.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.module.user.mapper.SuSearchMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuSearchEntity;
import com.xm.cpsmall.module.user.service.SearchService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SuSearchMapper suSearchMapper;


    @Override
    public PageBean<SuSearchEntity> get(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("create_time desc");
        SuSearchEntity entity = new SuSearchEntity();
        entity.setUserId(userId);
        entity.setDel(1);
        Page page = (Page)suSearchMapper.select(entity);
        return new PageBean<>(page);
    }

    @Override
    public void add(Integer userId, String keyword) {
        SuSearchEntity criteria = new SuSearchEntity();
        criteria.setUserId(userId);
        criteria.setKeyword(keyword);
        PageHelper.startPage(1,1,false);
        SuSearchEntity suSearchEntity = suSearchMapper.selectOne(criteria);
        if(suSearchEntity != null){
            suSearchEntity.setDel(1);
            suSearchEntity.setCreateTime(new Date());
            suSearchMapper.updateByPrimaryKeySelective(suSearchEntity);
        }else {
            SuSearchEntity entity = new SuSearchEntity();
            entity.setUserId(userId);
            entity.setKeyword(keyword);
            entity.setDel(1);
            entity.setCreateTime(new Date());
            suSearchMapper.insertSelective(entity);
        }
    }

    @Override
    public void deleteAll(Integer userId) {
        SuSearchEntity entity = new SuSearchEntity();
        entity.setDel(0);
        Example example = new Example(SuSearchEntity.class);
        example.createCriteria().andEqualTo("userId",userId);
        suSearchMapper.updateByExampleSelective(entity,example);
    }
}
