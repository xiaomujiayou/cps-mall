package com.xm.cpsmall.module.activite.serialize.bo;

import com.xm.cpsmall.module.activite.serialize.entity.SaActiveEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import lombok.Data;

@Data
public class BillActiveBo extends SaBillEntity {
    private SaActiveEntity saActiveEntity;
}
