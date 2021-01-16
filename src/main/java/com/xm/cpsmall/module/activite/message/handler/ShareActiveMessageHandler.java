package com.xm.cpsmall.module.activite.message.handler;

import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserAddProxyMessage;
import com.xm.cpsmall.module.activite.constant.ActiveConstant;
import com.xm.cpsmall.module.activite.mapper.SaActiveMapper;
import com.xm.cpsmall.module.activite.serialize.entity.SaActiveEntity;
import com.xm.cpsmall.module.activite.service.ActiviteBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ShareActiveMessageHandler implements MessageHandler {

    @Autowired
    private ActiviteBillService activiteBillService;
    @Autowired
    private SaActiveMapper saActiveMapper;

    @Override
    public List<Class> getType() {
        return Arrays.asList(UserAddProxyMessage.class);
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if(message instanceof UserAddProxyMessage){
            UserAddProxyMessage userAddProxyMessage = (UserAddProxyMessage)message;
            if(userAddProxyMessage.getLevel() > 1)
                return;
            SaActiveEntity shareActive = saActiveMapper.selectByPrimaryKey(ActiveConstant.SHARE_ACTIVE_ID);
            if(shareActive == null || shareActive.getState() != 1)
                return;
            activiteBillService.createBill(
                    userAddProxyMessage.getUserId(),
                    ActiveConstant.SHARE_ACTIVE_ID,
                    1,
                    shareActive.getMoney(),
                    1,
                    userAddProxyMessage.getProxyUser().getId().toString(),
                    userAddProxyMessage.getProxyUser().getNickname(),
                    null);
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
