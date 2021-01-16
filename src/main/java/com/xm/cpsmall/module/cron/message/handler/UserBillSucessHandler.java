package com.xm.cpsmall.module.cron.message.handler;

import cn.hutool.core.collection.CollUtil;
import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserBillStateChangeMessage;
import com.xm.cpsmall.module.cron.mapper.ScWaitPayBillMapper;
import com.xm.cpsmall.module.cron.serialize.entity.ScWaitPayBillEntity;
import com.xm.cpsmall.module.user.constant.BillStateConstant;
import com.xm.cpsmall.module.user.constant.BillTypeConstant;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 监听达到发放标准的账单
 * 只接受购买商品所产生的交易账单
 */
@Component
public class UserBillSucessHandler implements MessageHandler {

    @Autowired
    private ScWaitPayBillMapper scWaitPayBillMapper;

    @Override
    public List<Class> getType() {
        return CollUtil.newArrayList(
                UserBillStateChangeMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if(!(message instanceof UserBillStateChangeMessage))
            return;
        UserBillStateChangeMessage userBillStateChangeMessage = (UserBillStateChangeMessage)message;
        SuBillEntity bill = userBillStateChangeMessage.getOldBill();
        if(CollUtil.newArrayList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.PROXY_PROFIT,BillTypeConstant.BUY_SHARE,BillTypeConstant.SHARE_PROFIT).contains(bill.getType()) && bill.getState() != BillStateConstant.WAIT || userBillStateChangeMessage.getNewState() != BillStateConstant.READY)
            return;
        ScWaitPayBillEntity scWaitPayBillEntity = new ScWaitPayBillEntity();
        scWaitPayBillEntity.setBillId(userBillStateChangeMessage.getOldBill().getId());
        if(scWaitPayBillMapper.selectCount(scWaitPayBillEntity) > 0)
            return;
        scWaitPayBillEntity.setMoney(userBillStateChangeMessage.getOldBill().getMoney());
        scWaitPayBillEntity.setUserId(userBillStateChangeMessage.getUserId());
        scWaitPayBillEntity.setOpenId(userBillStateChangeMessage.getSuUserEntity().getOpenId());
        scWaitPayBillEntity.setState(1);
        scWaitPayBillEntity.setCreateTime(new Date());
        scWaitPayBillMapper.insertSelective(scWaitPayBillEntity);
    }

    @Override
    public void onError(Exception e) {

    }
}
