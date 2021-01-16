package com.xm.cpsmall.module.wind.service;

import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.constant.CreditConfigEnmu;
import com.xm.cpsmall.module.wind.serialize.entity.*;

import java.util.List;

/**
 * 信用支付服务
 */
public interface CreditBillService {

    /**
     * 获取用户信用
     * @param userId
     * @return
     */
    public SwCreditRecordEntity getUserCredit(Integer userId);

    /**
     * 获取全局信用配置信息
     * @param creditConfigEnmu
     * @return
     */
    public SwCreditConfEntity getConfig(CreditConfigEnmu creditConfigEnmu);

    /**
     * 创建用户信用记录
     * @param userId
     * @return
     */
    public SwCreditRecordEntity createUserCredit(Integer userId);

    /**
     * 按分数获取配置信息
     * @return
     * @throws Exception
     */
    public SwCreditBillConfEntity getConfByScores(SwCreditRecordEntity swCreditRecordEntity);

    /**
     * 信用支付一个订单
     * @throws Exception
     */
    public void payBillByCredit(SuBillEntity billEntity, String goodsName);

    /**
     * 检测账单是否合乎信用规则
     */
    public SwCreditBillPayRecordEntity creditCheckBill(SuBillEntity suBillEntity, SwCreditRecordEntity userCredit, SwCreditBillConfEntity userCreditConf, List<SwCreditBillBindRecordEntity> bindRecordEntities);

    /**
     * 检测金额是否合乎信用规则
     */
    public SwCreditBillPayRecordEntity creditCheckMoney(Integer money, SwCreditRecordEntity userCredit, SwCreditBillConfEntity userCreditConf, List<SwCreditBillBindRecordEntity> bindRecordEntities);
        /**
         * 信用绑定一个账单
         */
    public void creditBindBill(SuBillEntity suBillEntity, String goodsName, SwCreditRecordEntity swCreditRecordEntity, SwCreditBillConfEntity swCreditBillConfEntity, SwCreditBillPayRecordEntity swCreditBillPayRecordEntity);

    /**
     * 解绑信用账单
     * @param unBindType (解绑类型：1订单完成解绑)
     */
    public void creditUnBindBill(SuBillEntity suBillEntity, SwCreditRecordEntity swCreditRecordEntity, Integer unBindType, String unBindReason);

    /**
     * 商品校验
     * @param smProductEntityExes
     * @return
     */
    public List<SmProductEntityEx> productCheck(List<SmProductEntityEx> smProductEntityExes);
}
