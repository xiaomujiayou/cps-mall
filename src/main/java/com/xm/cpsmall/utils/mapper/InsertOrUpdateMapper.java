package com.xm.cpsmall.utils.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface InsertOrUpdateMapper<T> {

    /**
     * 保存或更新一个实体
     * @param t
     * @param condition 实体存在条件(成对出现)
     * @return
     */
    @InsertProvider(type = InsertOrUpdateProvider.class, method = "dynamicSQL")
    int insertOrUpdate(T t, Object... condition);
}
