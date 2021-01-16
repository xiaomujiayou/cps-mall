package com.xm.cpsmall.module.wind.mapper.custom;

import com.xm.cpsmall.module.wind.serialize.entity.SwApiRecordEntity;
import com.xm.cpsmall.module.wind.serialize.form.DelayForm;
import com.xm.cpsmall.utils.MyMapper;

public interface SwApiRecordMapperEx extends MyMapper<SwApiRecordEntity> {

    /**
     * 获取api平均延时
     * @param delayForm
     * @return
     */
    Integer getApiAverage(DelayForm delayForm);
}
