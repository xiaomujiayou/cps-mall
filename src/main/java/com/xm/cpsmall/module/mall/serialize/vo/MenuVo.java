package com.xm.cpsmall.module.mall.serialize.vo;

import com.xm.cpsmall.module.mall.serialize.entity.SmMenuEntity;
import lombok.Data;

@Data
public class MenuVo extends SmMenuEntity {
    private Boolean hot;
    private Integer num;
}
