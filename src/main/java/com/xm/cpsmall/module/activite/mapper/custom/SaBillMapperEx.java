package com.xm.cpsmall.module.activite.mapper.custom;

import com.xm.cpsmall.module.activite.serialize.bo.BillActiveBo;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import com.xm.cpsmall.utils.MyMapper;

import java.util.List;

public interface SaBillMapperEx extends MyMapper<SaBillEntity> {

    /**
     * 获取总收益
     * @return
     */
    public Integer totalProfit(SaBillEntity saBillEntity);

    /**
     * 获取活动收益列表
     * @param saBillEntity
     * @return
     */
    public List<BillActiveBo> getList(SaBillEntity saBillEntity);
}
