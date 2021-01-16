package com.xm.cpsmall.module.user.service.impl;

import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.user.mapper.SuSummaryMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuSummaryEntity;
import com.xm.cpsmall.module.user.service.SummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service("summaryService")
public class SummaryServiceImpl implements SummaryService {

    @Lazy
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private SuSummaryMapper suSummaryMapper;

    @Override
    public SuSummaryEntity getUserSummary(Integer userId) {
        SuSummaryEntity record = new SuSummaryEntity();
        record.setUserId(userId);
        record = suSummaryMapper.selectOne(record);
        if(record == null || record.getId() == null)
            record = summaryService.createNewSummary(userId);
        return record;
    }

    @Override
    public void updateSummary(SuSummaryEntity suSummaryEntity) {
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryMapper.updateByPrimaryKeySelective(suSummaryEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SuSummaryEntity createNewSummary(Integer userId) {
        SuSummaryEntity suSummaryEntity = new SuSummaryEntity();
        suSummaryEntity.setUserId(userId);
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryEntity.setCreateTime(suSummaryEntity.getUpdateTime());
        try {
            suSummaryMapper.insertUseGeneratedKeys(suSummaryEntity);
        }catch (DuplicateKeyException e){
            log.warn("SuSummaryEntity 数据插入重复 userId：{}",userId);
            SuSummaryEntity record = new SuSummaryEntity();
            record.setUserId(userId);
            return suSummaryMapper.selectOne(record);
        }
        return suSummaryEntity;
    }
}
