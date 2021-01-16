package com.xm.cpsmall.module.user.serialize.dto;

import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.Data;

@Data
public class BillOrderDto extends SuBillEntity {
    private SuOrderEntity suOrderEntity;
    private SuUserEntity suUserEntity;
}
