package com.xm.cpsmall.module.user.service;

import com.xm.cpsmall.module.user.serialize.entity.SuPidEntity;

public interface PidService {

    public SuPidEntity generatePid();

    public SuPidEntity getPid(Integer id);
}
