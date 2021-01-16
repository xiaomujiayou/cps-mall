package com.xm.cpsmall.module.user.service;

import com.xm.cpsmall.module.user.serialize.entity.SuSearchEntity;
import com.xm.cpsmall.utils.mybatis.PageBean;

public interface SearchService {

    public PageBean<SuSearchEntity> get(Integer userId, Integer pageNum, Integer pageSize);

    public void add(Integer userId, String keyword);

    public void deleteAll(Integer userId);
}
