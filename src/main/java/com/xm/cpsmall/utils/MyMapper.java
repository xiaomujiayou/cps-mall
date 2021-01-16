package com.xm.cpsmall.utils;

import com.xm.cpsmall.utils.mapper.SelectIdsMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 自己的MyMapper
 */
public interface MyMapper<T> extends Mapper<T>, IdsMapper<T>, MySqlMapper<T>, SelectIdsMapper<T> {

}
