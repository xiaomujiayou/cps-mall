package com.xm.cpsmall.module.user.message.handler;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.impl.*;
import com.xm.cpsmall.module.user.constant.BillTypeConstant;
import com.xm.cpsmall.module.user.service.MenuTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 添加菜单提示
 * 修改系统菜单后需要同时修改此文件
 */
@Component
public class MenuTipsHandler implements MessageHandler {

    @Autowired
    private MenuTipService menuTipService;
    //页面“我的”id
    private static final Integer PARENT_MENU_MY = 4;

    @Override
    public List<Class> getType() {
        return Lists.newArrayList(
                OrderCreateMessage.class,
                OrderSettlementSucessMessage.class,
                UserBillCreateMessage.class,
                UserShareGoodsMessage.class,
                UserAddProxyMessage.class,
                UserPaymentSucessMessage.class,
                UserCreditChangeMessage.class,
                UserActiveBillCreateMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        //自购订单消息
        if(message instanceof OrderCreateMessage || message instanceof OrderSettlementSucessMessage){
            addMenuTipNum(message.getUserId(),5,PARENT_MENU_MY);
        }
        if(message instanceof OrderStateChangeMessage || message instanceof OrderCommissionSucessMessage){
            addMenuDot(message.getUserId(),5,PARENT_MENU_MY);
        }
        //分享订单消息
        if(message instanceof UserBillCreateMessage){
            UserBillCreateMessage userBillCreateMessage = (UserBillCreateMessage)message;
            if(userBillCreateMessage.getSuBillEntity().getType() == BillTypeConstant.SHARE_PROFIT){
                addMenuTipNum(message.getUserId(),6,PARENT_MENU_MY);
                addMenuTipNum(message.getUserId(),7,PARENT_MENU_MY);
            }
        }
        //分享记录
        if(message instanceof UserShareGoodsMessage){
            addMenuDot(message.getUserId(),7,PARENT_MENU_MY);
        }
        //我的账单
        if(message instanceof UserBillCreateMessage){
            UserBillCreateMessage userBillCreateMessage = (UserBillCreateMessage)message;
            if(CollUtil.newArrayList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.PROXY_PROFIT,BillTypeConstant.BUY_SHARE,BillTypeConstant.SHARE_PROFIT).contains(userBillCreateMessage.getSuBillEntity().getType())){
                addMenuTipNum(message.getUserId(),8,PARENT_MENU_MY);
            }
        }
        //锁定用户
        if(message instanceof UserAddProxyMessage){
            UserAddProxyMessage userAddProxyMessage = (UserAddProxyMessage)message;
            if(userAddProxyMessage.getLevel() == 1) {
                addMenuTipNum(message.getUserId(), 9, PARENT_MENU_MY);
            }else {
                addMenuDot(message.getUserId(), 9, PARENT_MENU_MY);
            }
        }
        //佣金发放
        if(message instanceof UserPaymentSucessMessage){
            addMenuTipNum(message.getUserId(), 12, PARENT_MENU_MY);
        }

        //信用变更
        if(message instanceof UserCreditChangeMessage){
            addMenuDot(message.getUserId(), 15, PARENT_MENU_MY);
        }

        //活动账单
        if(message instanceof UserActiveBillCreateMessage){
            addMenuTipNum(message.getUserId(), 16, PARENT_MENU_MY);
        }
    }

    private void addMenuTipNum(Integer userId,Integer menuId,Integer parentMenu){
        menuTipService.addNum(userId, Arrays.asList(menuId,parentMenu));
    }

    private void addMenuDot(Integer userId,Integer menuId,Integer parentMenu){
        menuTipService.addRedPoint(userId, Arrays.asList(menuId,parentMenu));
    }

    @Override
    public void onError(Exception e) {

    }
}
