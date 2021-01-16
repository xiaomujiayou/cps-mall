package com.xm.cpsmall.module.cron.mapper.custom;

import com.xm.cpsmall.module.cron.serialize.entity.ScBillPayEntity;
import com.xm.cpsmall.module.cron.serialize.form.BillPayForm;
import com.xm.cpsmall.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ScBillPayMapperEx extends MyMapper<ScBillPayEntity> {

    Integer totalGenPayBill(Integer minMoney, @Param("timeline") Date timeline);

    List<ScBillPayEntity> genPayBill(Integer minMoney, Integer start, Integer end, Date timeline);

    /**
     * 查询账单支付金额
     */
    Integer getBillProfit(BillPayForm billPayForm);
}
