package com.xm.cpsmall.module.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xm.cpsmall.module.activite.service.ActiveService;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.utils.GoodsPriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("profitService")
public class ProfitServiceImpl implements ProfitService {

    @Autowired
    private MallConfigService mallConfigService;
    @Autowired
    private ActiveService activeService;

    @Override
    public SmProductEntityEx calcProfit(SmProductEntity smProductEntity, Integer userId, Boolean isShare, Integer shareUserId) {

        //去掉自己分享
        if(userId != null && userId.equals(shareUserId)){
            isShare = false;
            shareUserId = null;
        }
        Integer configType = ConfigTypeConstant.PROXY_CONFIG;
        Integer buyRate = Integer.valueOf(mallConfigService.getConfig(isShare?shareUserId:userId, isShare?ConfigEnmu.PRODUCT_SHARE_BUY_RATE:ConfigEnmu.PRODUCT_BUY_RATE,isShare?ConfigTypeConstant.SELF_CONFIG: configType).getVal());
        Integer shareRate = Integer.valueOf(mallConfigService.getConfig(userId, ConfigEnmu.PRODUCT_SHARE_USER_RATE, configType).getVal());
        SmProductEntityEx result = calcProfit(userId,smProductEntity,buyRate,shareRate);
        //添加活动信息
        result = activeService.goodsInfo(userId,result);
        return result;
    }

    @Override
    public List<SmProductEntityEx> calcProfit(List<SmProductEntity> smProductEntitys, Integer userId) {
        Integer configType = ConfigTypeConstant.PROXY_CONFIG;
        Integer buyRate = Integer.valueOf(mallConfigService.getConfig(userId, ConfigEnmu.PRODUCT_BUY_RATE,configType).getVal());
        Integer shareRate = Integer.valueOf(mallConfigService.getConfig(userId, ConfigEnmu.PRODUCT_SHARE_USER_RATE, configType).getVal());
        List<SmProductEntityEx> list = smProductEntitys.stream().map(o->{
            return calcProfit(userId,o,buyRate,shareRate);
        }).collect(Collectors.toList());
        //添加活动信息
        list = activeService.goodsInfo(userId,list);
        return list;
    }

    private SmProductEntityEx calcProfit(Integer userId,SmProductEntity smProductEntity,Integer buyRate,Integer shareRate){
        SmProductEntityEx smProductEntityEx = new SmProductEntityEx();
        BeanUtil.copyProperties(smProductEntity,smProductEntityEx);
        smProductEntityEx.setUserId(userId);
        smProductEntityEx.setBuyRate(buyRate);
        smProductEntityEx.setShareRate(shareRate);
        smProductEntityEx.setBuyPrice(GoodsPriceUtil.type(smProductEntity.getType()).calcUserBuyProfit(smProductEntity,buyRate.doubleValue()).intValue());
        smProductEntityEx.setSharePrice(GoodsPriceUtil.type(smProductEntity.getType()).calcUserShareProfit(smProductEntity,shareRate.doubleValue()).intValue());
        return smProductEntityEx;
    }
}
