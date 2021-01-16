package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import lombok.Data;

import java.util.List;

@Data
public class CalcProfitForm {
    private Integer userId;
    private List<SmProductEntity> smProductEntities;
}
