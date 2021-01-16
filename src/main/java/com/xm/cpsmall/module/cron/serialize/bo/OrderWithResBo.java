package com.xm.cpsmall.module.cron.serialize.bo;

import com.xm.cpsmall.module.cron.serialize.entity.ScOrderStateRecordEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderWithResBo {
    private SuOrderEntity suOrderEntity;
    private ScOrderStateRecordEntity scOrderStateRecordEntity;
}
