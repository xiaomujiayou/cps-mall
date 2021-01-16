package com.xm.cpsmall.module.user.serialize.vo;

import com.xm.cpsmall.module.mall.serialize.vo.SmProductVo;
import lombok.Data;

@Data
public class SuProductHistoryVo extends SmProductVo {
    private Integer itemId;
    private Integer shareUserId;
    private Integer productType;
}
