package com.xm.cpsmall.module.activite.message.handler;

import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserAddProxyMessage;
import com.xm.cpsmall.module.activite.constant.ActiveConstant;
import com.xm.cpsmall.module.activite.mapper.SaActiveMapper;
import com.xm.cpsmall.module.activite.mapper.SaBillMapper;
import com.xm.cpsmall.module.activite.serialize.entity.SaActiveEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import com.xm.cpsmall.module.activite.service.ActiviteBillService;
import com.xm.cpsmall.module.user.mapper.SuSummaryMapper;
import com.xm.cpsmall.utils.lock.LockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Component
public class ShareActiveMessageHandler implements MessageHandler {

    @Autowired
    private ActiviteBillService activiteBillService;
    @Autowired
    private SaActiveMapper saActiveMapper;
    @Autowired
    private SaBillMapper saBillMapper;
    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private SuSummaryMapper suSummaryMapper;

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
            Lock lock = redisLockRegistry.obtain(this.getClass().getSimpleName() + ":" + userAddProxyMessage.getUserId() + ":" + userAddProxyMessage.getProxyUser().getId().toString());
            LockUtil.lock(lock, () -> {
                //消息重复消费校验
                SaBillEntity record = new SaBillEntity();
                record.setUserId(userAddProxyMessage.getUserId());
                record.setAttach(userAddProxyMessage.getProxyUser().getId().toString());
                Integer count = saBillMapper.selectCount(record);
                if (count > 0)
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
            });
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
