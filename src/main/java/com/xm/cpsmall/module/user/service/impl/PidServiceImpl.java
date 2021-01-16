package com.xm.cpsmall.module.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.user.mapper.SuPidMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuPidEntity;
import com.xm.cpsmall.module.user.service.PidService;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pidService")
public class PidServiceImpl implements PidService {

    @Autowired
    private SuPidMapper suPidMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SuPidEntity generatePid() {
        SuPidEntity record = new SuPidEntity();
        record.setState(1);
        PageHelper.startPage(1,1);
        SuPidEntity suPidEntity = suPidMapper.selectOne(record);
        if(suPidEntity == null)
            throw new GlobleException(MsgEnum.NO_DATA_ERROR,"pid 获取失败");
        record.setState(2);
        record.setId(suPidEntity.getId());
        suPidMapper.updateByPrimaryKeySelective(record);
        return suPidEntity;
    }

    public SuPidEntity getPid(Integer id){
        return suPidMapper.selectByPrimaryKey(id);
    }
}
