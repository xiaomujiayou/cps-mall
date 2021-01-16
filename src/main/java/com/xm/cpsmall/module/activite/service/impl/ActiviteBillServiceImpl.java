package com.xm.cpsmall.module.activite.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.comm.mq.message.config.PayMqConfig;
import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import com.xm.cpsmall.comm.mq.message.impl.UserActiveBillCreateMessage;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.activite.mapper.SaBillMapper;
import com.xm.cpsmall.module.activite.mapper.SaCashOutRecordMapper;
import com.xm.cpsmall.module.activite.mapper.custom.SaBillMapperEx;
import com.xm.cpsmall.module.activite.serialize.bo.BillActiveBo;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaCashOutRecordEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaConfigEntity;
import com.xm.cpsmall.module.activite.service.ActiviteBillService;
import com.xm.cpsmall.module.activite.service.ActiviteConfigService;
import com.xm.cpsmall.module.activite.service.manage.CashoutService;
import com.xm.cpsmall.module.pay.serialize.message.ActiveAutoEntPayMessage;
import com.xm.cpsmall.module.user.controller.UserController;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.utils.lock.LockUtil;
import com.xm.cpsmall.utils.product.GenNumUtil;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Service("activiteBillService")
public class ActiviteBillServiceImpl implements ActiviteBillService {

    @Autowired
    private SaBillMapper saBillMapper;
    @Autowired
    private SaBillMapperEx saBillMapperEx;
    @Autowired
    private SaCashOutRecordMapper saCashOutRecordMapper;
    @Autowired
    private ActiviteConfigService activiteConfigService;
    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private CashoutService cashoutService;
    @Autowired
    private UserController userController;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<BillActiveBo> getList(Integer userId, Integer state, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SaBillEntity example = new SaBillEntity();
        example.setUserId(userId);
        example.setState(state);
        return saBillMapperEx.getList(example);
    }

    @Override
    public List<SaCashOutRecordEntity> getCashoutList(Integer userId, Integer state, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderByHelper.orderBy("create_time desc");
        SaCashOutRecordEntity example = new SaCashOutRecordEntity();
        example.setUserId(userId);
        return saCashOutRecordMapper.select(example);
    }

    @Override
    public Integer getUserActiveProfit(Integer userId, Integer activeId,Integer state) {
        SaBillEntity example = new SaBillEntity();
        example.setActiveId(activeId);
        example.setUserId(userId);
        example.setState(state);
        return saBillMapperEx.totalProfit(example);
    }

    @Override
    public SaBillEntity createBill(Integer userId, Integer activeId, Integer type, Integer money,Integer state, String attach,String attachDes, String failReason) {
        SaBillEntity saBillEntity = new SaBillEntity();
        saBillEntity.setUserId(userId);
        SuUserEntity suUserEntity = userController.infoDetail(userId);
        saBillEntity.setOpenId(suUserEntity.getOpenId());
        saBillEntity.setIp(suUserEntity.getCurrentIp());
        saBillEntity.setActiveId(activeId);
        saBillEntity.setType(type);
        saBillEntity.setMoney(money);
        saBillEntity.setAttach(attach);
        saBillEntity.setAttachDes(attachDes);
        saBillEntity.setState(state);
        saBillEntity.setFailReason(failReason);
        saBillEntity.setCreateTime(new Date());
        saBillMapper.insertSelective(saBillEntity);
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserActiveBillCreateMessage(userId,saBillEntity));
        return saBillEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cashOut(Integer userId,String openId,String ip) {
        Lock lock = redisLockRegistry.obtain(this.getClass().getSimpleName()+":"+userId);

        LockUtil.lock(lock,()->{
            SaCashOutRecordEntity example = new SaCashOutRecordEntity();
            example.setUserId(userId);
            PageHelper.startPage(1,1).count(false);
            OrderByHelper.orderBy("create_time desc");
            SaCashOutRecordEntity last = saCashOutRecordMapper.selectOne(example);
            if(last != null && last.getState() == 1)
                throw new GlobleException(MsgEnum.DATA_ALREADY_EXISTS,"审核中，请耐心等待");


            //每天提现一次
            Example emp = new Example(SaCashOutRecordEntity.class);
            Date start = DateUtil.beginOfDay(new Date());
            Date end = DateUtil.beginOfDay(DateUtil.tomorrow());
            emp.createCriteria()
                    .andEqualTo("userId",userId)
                    .andBetween("createTime",start,end);
            int count = saCashOutRecordMapper.selectCountByExample(emp);
            SaConfigEntity configEntity = activiteConfigService.getConfig("CASH_OUT_TIMES_ONE_DAY");
            int timesByDay = configEntity != null ? Integer.valueOf(configEntity.getVal()) : 0;
            if(count >= timesByDay)
                throw new GlobleException(MsgEnum.DATA_ALREADY_EXISTS,"每天只能提现一次");

            SaBillEntity saBillEntity = new SaBillEntity();
            saBillEntity.setUserId(userId);
            saBillEntity.setState(1);
            List<SaBillEntity> bills = saBillMapper.select(saBillEntity);
            if(bills == null || bills.isEmpty())
                throw new GlobleException(MsgEnum.NO_DATA_ERROR,"没有可以提现的金额");




            //添加提现记录
            Integer profit = bills.stream().mapToInt(SaBillEntity::getMoney).sum();
            if(profit < 30){
                throw new GlobleException(MsgEnum.NO_DATA_ERROR,"由于微信限制，不足0.3元无法提现");
            }
            String billIds = bills.stream().map(o -> o.getId().toString()).collect(Collectors.joining(","));
            List<Integer> ids = bills.stream().map(SaBillEntity::getId).collect(Collectors.toList());
            SaCashOutRecordEntity recordEntity = new SaCashOutRecordEntity();
            recordEntity.setUserId(userId);
            recordEntity.setOpenId(openId);
            recordEntity.setIp(ip);
            recordEntity.setCashSn(GenNumUtil.genActiveBillNum());
            recordEntity.setBillIds(billIds);
            recordEntity.setState(1);
            recordEntity.setMoney(profit);
            recordEntity.setCreateTime(new Date());
            saCashOutRecordMapper.insertSelective(recordEntity);
            //修改账单状态
            Example billEmp = new Example(SaBillEntity.class);
            billEmp.createCriteria().andIn("id",ids);
            SaBillEntity billEntity = new SaBillEntity();
            billEntity.setState(2);
            saBillMapper.updateByExampleSelective(billEntity,billEmp);

            //自动提现
            cashoutService.approval(Arrays.asList(recordEntity.getId()));
        });
    }

    @Override
    public void cashOut(SaBillEntity saBillEntity,String desc) {
        ActiveAutoEntPayMessage activeAutoEntPayMessage = new ActiveAutoEntPayMessage();
        activeAutoEntPayMessage.setUserId(saBillEntity.getUserId());
        activeAutoEntPayMessage.setIp(saBillEntity.getIp());
        activeAutoEntPayMessage.setDesc(desc);
        activeAutoEntPayMessage.setSaBillEntity(saBillEntity);
        rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE,PayMqConfig.KEY_WX_ENT_PAY_ACTIVE_AUTO,activeAutoEntPayMessage);
    }
}
