package com.xm.cpsmall.module.user.mapper.custom;

import com.xm.cpsmall.module.user.serialize.dto.BillOrderDto;
import com.xm.cpsmall.module.user.serialize.dto.OrderBillDto;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.form.BillProfitSearchForm;
import com.xm.cpsmall.utils.MyMapper;

import java.util.List;

public interface SuBillMapperEx extends MyMapper<SuBillEntity> {

    /**
     * 获取订单相关
     * @param userId
     * @param type
     * @param platformType
     * @param state
     * @return
     */
    List<OrderBillDto> getOrderBill(Integer userId, Integer type, Integer platformType, Integer state);

    /**
     * 获取账单相关
     * @param userId
     * @return
     */
    List<BillOrderDto> getBillInfo(Integer userId, List<String> billIds);

    /**
     * 获取相关账单金额
     * @param billProfitSearchForm
     * @return
     */
    Integer getBillProfit(BillProfitSearchForm billProfitSearchForm);

}
