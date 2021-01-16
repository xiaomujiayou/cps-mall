package com.xm.cpsmall.module.user.serialize.form;

import com.xm.cpsmall.module.lottery.serialize.ex.SlPropSpecEx;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.Data;

@Data
public class WxPayForm {
    private SlPropSpecEx slPropSpecEx;
    private SuUserEntity suUserEntity;
}
