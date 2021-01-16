package com.xm.cpsmall.module.lottery.serialize.ex;

import com.xm.cpsmall.module.lottery.serialize.entity.SlPropEntity;
import com.xm.cpsmall.module.lottery.serialize.entity.SlPropSpecEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.Data;

@Data
public class SlPropSpecEx extends SlPropSpecEntity {
    private SuUserEntity suUserEntity;
    private String clientIp;
    private SlPropEntity slPropEntity;
}
