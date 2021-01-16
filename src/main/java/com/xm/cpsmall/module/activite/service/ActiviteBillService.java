package com.xm.cpsmall.module.activite.service;

import com.xm.cpsmall.module.activite.serialize.bo.BillActiveBo;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaCashOutRecordEntity;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

public interface ActiviteBillService {

    /**
     * 获取账单列表
     * @return
     */
    public List<BillActiveBo> getList(Integer userId, Integer state, Integer pageNum, Integer pageSize);

    public List<SaCashOutRecordEntity> getCashoutList(Integer userId, Integer state, Integer pageNum, Integer pageSize);

    /**
     * 获取用户活动收入
     * @param userId
     * @param activeId
     * @return
     */
    Integer getUserActiveProfit(Integer userId, Integer activeId, Integer state);

    /**
     * 创建账单
     */
    SaBillEntity createBill(Integer userId, Integer activeId, Integer type, Integer money, Integer state, String attach, String attachDes, String failReason);

    /**
     * 提现申请
     */
    void cashOut(Integer userId, String openId, String ip);

    /**
     * 支付一个账单
     */
    void cashOut(SaBillEntity saBillEntity, String desc);
}
