package com.xm.cpsmall.module.wind.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import com.xm.cpsmall.comm.mq.message.impl.UserBindCreditBillMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserCreditPayBillCreateMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserUnBindCreditBillMessage;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.wind.constant.CreditConfigEnmu;
import com.xm.cpsmall.module.wind.mapper.*;
import com.xm.cpsmall.module.wind.serialize.entity.*;
import com.xm.cpsmall.module.wind.service.CreditBillService;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Service("creditBillService")
public class CreditBillServiceImpl implements CreditBillService {

    @Autowired
    private SwCreditRecordMapper swCreditRecordMapper;
    @Autowired
    private SwCreditConfMapper swCreditConfMapper;
    @Autowired
    private SwCreditBillConfMapper swCreditBillConfMapper;
    @Autowired
    private SwCreditBillBindRecordMapper swCreditBillBindRecordMapper;
    @Autowired
    private SwCreditBillPayRecordMapper swCreditBillPayRecordMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Lazy
    @Autowired
    private CreditBillService creditBillService;
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Override
    public SwCreditRecordEntity getUserCredit(Integer userId) {
        SwCreditRecordEntity recordEntity = new SwCreditRecordEntity();
        recordEntity.setUserId(userId);
        OrderByHelper.orderBy("create_stamp+0 desc");
        PageHelper.startPage(1,1).count(false);
        recordEntity = swCreditRecordMapper.selectOne(recordEntity);
        //信用不存在则创建
        if(recordEntity == null || recordEntity.getId() == null)
            recordEntity = creditBillService.createUserCredit(userId);
        return recordEntity;
    }

    public SwCreditConfEntity getConfig(CreditConfigEnmu creditConfigEnmu){
        SwCreditConfEntity confEntity = new SwCreditConfEntity();
        confEntity.setName(creditConfigEnmu.getName());
        confEntity = swCreditConfMapper.selectOne(confEntity);
        if (confEntity == null || confEntity.getId() == null)
            throw new GlobleException(MsgEnum.CONF_NOTFOUND_ERROR);
        return confEntity;
    }

    @Override
    public SwCreditRecordEntity createUserCredit(Integer userId) {
        Lock lock = null;
        try {
            lock = redisLockRegistry.obtain("createUserCredit:"+userId);
            lock.lock();
            System.out.println("createUserCredit:"+userId);
            SwCreditRecordEntity recordEntity = new SwCreditRecordEntity();
            recordEntity.setUserId(userId);
            OrderByHelper.orderBy("create_stamp+0 desc");
            PageHelper.startPage(1,1).count(false);
            recordEntity = swCreditRecordMapper.selectOne(recordEntity);
            if(recordEntity == null || recordEntity.getId() == null) {
                SwCreditConfEntity confEntity = getConfig(CreditConfigEnmu.DEFAULT_CREDIT_SCORES);
                SwCreditRecordEntity entity = new SwCreditRecordEntity();
                entity.setUserId(userId);
                entity.setChangeScores(Integer.valueOf(confEntity.getVal()));
                entity.setChangeReason("新用户赠送");
                entity.setScores(entity.getChangeScores());
                Date current = new Date();
                entity.setCreateStamp(current.getTime()+"");
                entity.setCreateTime(current);
                swCreditRecordMapper.insertUseGeneratedKeys(entity);
                return entity;
            }
            return recordEntity;
        }finally {
            if(lock != null)
                lock.unlock();
        }
    }
    public static void main(String[] args){
        Integer a = Long.valueOf(System.currentTimeMillis()).intValue();
        System.out.println(a);
    }


    @Override
    public SwCreditBillConfEntity getConfByScores(SwCreditRecordEntity swCreditRecordEntity) {
        return swCreditBillConfMapper.selectAll()
                .stream()
                .filter(o -> swCreditRecordEntity.getScores() >= o.getScores())
                .max((o1, o2)-> o1.getScores().compareTo(o2.getScores())).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payBillByCredit(SuBillEntity billEntity, String goodsName) {
        Lock lock = null;
        try {
            lock = redisLockRegistry.obtain("payBillByCredit:"+billEntity.getUserId());
            lock.lock();
            SwCreditRecordEntity userCredit = getUserCredit(billEntity.getUserId());
            SwCreditBillConfEntity userCreditConf = getConfByScores(userCredit);
            SwCreditBillBindRecordEntity example = new SwCreditBillBindRecordEntity();
            example.setUserId(billEntity.getUserId());
            example.setState(1);
            List<SwCreditBillBindRecordEntity> bindRecordEntities = swCreditBillBindRecordMapper.select(example);
            SwCreditBillPayRecordEntity billPayRecord = creditCheckBill(billEntity,userCredit,userCreditConf,bindRecordEntities);
            billPayRecord.setUserId(billEntity.getUserId());
            billPayRecord.setBillSn(billEntity.getBillSn());
            billPayRecord.setBillMoney(billEntity.getMoney());
            billPayRecord.setCreateTime(new Date());
            if(billPayRecord.getCheckResult() == 1){
                //校验通过
                swCreditBillPayRecordMapper.insertUseGeneratedKeys(billPayRecord);
                creditBillService.creditBindBill(billEntity,goodsName,userCredit,userCreditConf,billPayRecord);
                return;
            }
            swCreditBillPayRecordMapper.insertSelective(billPayRecord);
        }finally {
            if(lock != null)
                lock.unlock();
        }
    }

    /**
     * 检测账单是否合乎信用规则
     * @param userCreditConf
     * @return
     */
    public SwCreditBillPayRecordEntity creditCheckBill(SuBillEntity suBillEntity,SwCreditRecordEntity userCredit,SwCreditBillConfEntity userCreditConf,List<SwCreditBillBindRecordEntity> bindRecordEntities){
        SwCreditBillPayRecordEntity billPayRecord = new SwCreditBillPayRecordEntity();
        //是否重复
        Boolean repeat = bindRecordEntities == null ? false : bindRecordEntities.stream().anyMatch(o -> o.getBillSn().equals(suBillEntity.getBillSn()));

        if(userCreditConf == null || userCreditConf.getId() == null) {
            billPayRecord.setCheckResult(0);
            billPayRecord.setFailReason(String.format("用户信用不足，当前信用：%s",userCredit.getScores()));
        }else if(repeat){
            billPayRecord.setCheckResult(0);
            billPayRecord.setFailReason(String.format("账单已存在，重复提交 当前信用：%s 可预支总数：%s 已使用：%s",userCredit.getScores(),userCreditConf.getAdvanceCount(),bindRecordEntities.size()));
        }else {
            return creditCheckMoney(suBillEntity.getMoney(),userCredit,userCreditConf,bindRecordEntities);
        }
        return billPayRecord;
    }

    @Override
    public SwCreditBillPayRecordEntity creditCheckMoney(Integer money, SwCreditRecordEntity userCredit, SwCreditBillConfEntity userCreditConf, List<SwCreditBillBindRecordEntity> bindRecordEntities) {
        SwCreditBillPayRecordEntity billPayRecord = new SwCreditBillPayRecordEntity();
        //绑定的总额度
        Integer totalMoney = bindRecordEntities == null ? 0 : bindRecordEntities.stream().mapToInt(SwCreditBillBindRecordEntity::getBillMoney).sum();
        //绑定的总数
        Integer totalCount = bindRecordEntities == null ? 0 : bindRecordEntities.size();

        if(userCreditConf == null || userCreditConf.getId() == null) {
            billPayRecord.setCheckResult(0);
            billPayRecord.setFailReason(String.format("用户信用不足，当前信用：%s",userCredit.getScores()));
        }else if(money > userCreditConf.getQuota()){
            billPayRecord.setCheckResult(0);
            billPayRecord.setFailReason(String.format("单笔限额超限，当前信用：%s 单笔限额：%s元 本次：%s元",userCredit.getScores(),userCreditConf.getQuota() / 100d,money / 100d));
        }else if(totalMoney + money > userCreditConf.getMaxQuota()){
            billPayRecord.setCheckResult(0);
            billPayRecord.setFailReason(String.format("预支额度超限 当前信用：%s 预支最大额度：%s元 已使用：%s元 当前：%s元",userCredit.getScores(),userCreditConf.getMaxQuota() / 100d,totalMoney / 100d,money / 100d));
        }else if(totalCount >= userCreditConf.getAdvanceCount()){
            billPayRecord.setCheckResult(0);
            billPayRecord.setFailReason(String.format("用户可预支次数不足 当前信用：%s 可预支总数：%s 已使用：%s",userCredit.getScores(),userCreditConf.getAdvanceCount(),bindRecordEntities.size()));
        }else {
            billPayRecord.setCheckResult(1);
        }
        return billPayRecord;
    }


    @Override
    public void creditBindBill(SuBillEntity suBillEntity, String goodsName,SwCreditRecordEntity swCreditRecordEntity,SwCreditBillConfEntity swCreditBillConfEntity,SwCreditBillPayRecordEntity swCreditBillPayRecordEntity) {
        SwCreditBillBindRecordEntity swCreditBillBindRecordEntity = new SwCreditBillBindRecordEntity();
        swCreditBillBindRecordEntity.setUserId(suBillEntity.getUserId());
        swCreditBillBindRecordEntity.setBillSn(suBillEntity.getBillSn());
        swCreditBillBindRecordEntity.setGoodsName(goodsName);
        swCreditBillBindRecordEntity.setBillMoney(suBillEntity.getMoney());
        swCreditBillBindRecordEntity.setState(1);
        swCreditBillBindRecordEntity.setBindScores(swCreditRecordEntity.getScores());
        swCreditBillBindRecordEntity.setCreateTime(new Date());
        swCreditBillBindRecordMapper.insertUseGeneratedKeys(swCreditBillBindRecordEntity);
        //发送UserAction
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserBindCreditBillMessage(suBillEntity.getUserId(),swCreditBillBindRecordEntity,suBillEntity,swCreditRecordEntity,swCreditBillConfEntity,swCreditBillPayRecordEntity));
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserCreditPayBillCreateMessage(suBillEntity.getUserId(),suBillEntity,swCreditRecordEntity,swCreditBillConfEntity,swCreditBillPayRecordEntity));
    }

    @Override
    public void creditUnBindBill(SuBillEntity suBillEntity, SwCreditRecordEntity swCreditRecordEntity,Integer unBindType, String unBindReason) {
        SwCreditBillBindRecordEntity example = new SwCreditBillBindRecordEntity();
        example.setBillSn(suBillEntity.getBillSn());
        example.setUserId(suBillEntity.getUserId());
        SwCreditBillBindRecordEntity recordEntity = swCreditBillBindRecordMapper.selectOne(example);
        if(recordEntity == null || recordEntity.getId() == null || recordEntity.getState() != 1)
            return;
        recordEntity.setState(0);
        recordEntity.setUnBindReason(unBindReason);
        recordEntity.setUnBindScores(swCreditRecordEntity.getScores());
        swCreditBillBindRecordMapper.updateByPrimaryKeySelective(recordEntity);
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserUnBindCreditBillMessage(suBillEntity.getUserId(),suBillEntity,swCreditRecordEntity,unBindType,unBindReason));
    }

    @Override
    public List<SmProductEntityEx> productCheck(List<SmProductEntityEx> smProductEntityExes) {
        if(CollUtil.isEmpty(smProductEntityExes))
            return smProductEntityExes;
        Integer userId = smProductEntityExes.get(0).getUserId();
        SwCreditRecordEntity userCredit = getUserCredit(userId);
        SwCreditBillConfEntity userCreditConf = getConfByScores(userCredit);
        SwCreditBillBindRecordEntity example = new SwCreditBillBindRecordEntity();
        example.setUserId(userId);
        example.setState(1);
        List<SwCreditBillBindRecordEntity> bindRecordEntities = swCreditBillBindRecordMapper.select(example);
        for (SmProductEntityEx smProductEntityEx : smProductEntityExes) {
            SwCreditBillPayRecordEntity creditBillPayRecordEntity = creditCheckMoney(smProductEntityEx.getBuyPrice(),userCredit,userCreditConf,bindRecordEntities);
            smProductEntityEx.setCreditPay(creditBillPayRecordEntity.getCheckResult() == 1);
        }
        return smProductEntityExes;
    }
}
