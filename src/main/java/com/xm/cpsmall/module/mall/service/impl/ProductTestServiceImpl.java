package com.xm.cpsmall.module.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.xm.cpsmall.comm.mq.message.config.OrderMqConfig;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GetProductSaleInfoForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailForm;
import com.xm.cpsmall.module.mall.service.ProductTestService;
import com.xm.cpsmall.module.mall.service.api.GoodsService;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;
import com.xm.cpsmall.module.user.serialize.bo.OrderCustomParameters;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.utils.GoodsPriceUtil;
import com.xm.cpsmall.utils.product.GenNumUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("productTestService")
public class ProductTestServiceImpl implements ProductTestService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GoodsService goodsService;

    @Override
    public ShareLinkBo saleInfo(Integer userId, String pid, GetProductSaleInfoForm productSaleInfoForm) throws Exception {
        ShareLinkBo shareLinkBo = createShareLinkBo();
        GoodsDetailForm goodsDetailForm = new GoodsDetailForm();
        goodsDetailForm.setUserId(userId);
        goodsDetailForm.setPid(pid);
        goodsDetailForm.setPlatformType(productSaleInfoForm.getPlatformType());
        goodsDetailForm.setGoodsId(productSaleInfoForm.getGoodsId());
        goodsDetailForm.setShareUserId(productSaleInfoForm.getShareUserId());
        goodsDetailForm.setAppType(productSaleInfoForm.getAppType());
        SmProductEntityEx smProductEntityEx = goodsService.detail(goodsDetailForm);
        SuOrderEntity suOrderEntity = createSuOrderEntity(smProductEntityEx,pid,userId,productSaleInfoForm);
        rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
//        new Thread(()->{
//            try {
//                //确认收货
////                Thread.sleep(1000 * 30);
////                suOrderEntity.setState(OrderStateConstant.CONFIRM_RECEIPT);
////                suOrderEntity.setOrderModifyAt(new Date());
////                rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
////                //已结算
////                Thread.sleep(1000 * 60);
////                suOrderEntity.setOrderModifyAt(new Date());
////                suOrderEntity.setState(OrderStateConstant.ALREADY_SETTLED);
////                rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
//                //已退单
////                Thread.sleep(1000 * 90);
////                suOrderEntity.setOrderModifyAt(new Date());
////                suOrderEntity.setState(OrderStateConstant.FAIL_SETTLED);
////                suOrderEntity.setFailReason("订单已取消");
////                rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
        return shareLinkBo;
    }

    private SuOrderEntity createSuOrderEntity(SmProductEntityEx item,String pid,Integer userId,GetProductSaleInfoForm productSaleInfoForm){
        SuOrderEntity orderEntity = new SuOrderEntity();
        orderEntity.setOrderSn(GenNumUtil.genOrderNum());
        orderEntity.setOrderSubSn(orderEntity.getOrderSn() + "-" + item.getGoodsId());
        orderEntity.setProductId(item.getGoodsId().toString());
        orderEntity.setProductName(item.getName());
        orderEntity.setImgUrl(item.getGoodsThumbnailUrl());
        orderEntity.setPlatformType(PlatformTypeConstant.PDD);
        orderEntity.setState(OrderStateConstant.PAY);
        orderEntity.setPId(pid);
        orderEntity.setOriginalPrice(item.getOriginalPrice());
        orderEntity.setQuantity(1);
        orderEntity.setAmount(item.getOriginalPrice() - (item.getCouponPrice() == null ? 0 : item.getCouponPrice()));
        orderEntity.setPromotionRate(item.getPromotionRate());
        orderEntity.setPromotionAmount(GoodsPriceUtil.type(orderEntity.getPlatformType()).calcUserBuyProfit(orderEntity.getAmount().doubleValue(), orderEntity.getPromotionRate().doubleValue()).intValue());
        orderEntity.setType(0);
        OrderCustomParameters parameters = new OrderCustomParameters();
        parameters.setUserId(userId);
        parameters.setShareUserId(item.getShareUserId());
        parameters.setPid(pid);
        parameters.setFromApp(1);
        orderEntity.setCustomParameters(JSON.toJSONString(parameters));
        orderEntity.setOrderModifyAt(new Date());
        return orderEntity;
    }
    private ShareLinkBo createShareLinkBo(){
        ShareLinkBo shareLinkBo = new ShareLinkBo();
        shareLinkBo.setWePagePath("test");
        return shareLinkBo;
    }
}
