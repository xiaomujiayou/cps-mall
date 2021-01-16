package com.xm.cpsmall.module.activite.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.xm.cpsmall.module.activite.mapper.SaActiveMapper;
import com.xm.cpsmall.module.activite.mapper.SaBillMapper;
import com.xm.cpsmall.module.activite.mapper.SaCashOutRecordMapper;
import com.xm.cpsmall.module.activite.mapper.custom.SaBillMapperEx;
import com.xm.cpsmall.module.activite.serialize.entity.SaActiveEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaCashOutRecordEntity;
import com.xm.cpsmall.module.activite.service.ActiveService;
import com.xm.cpsmall.module.mall.serialize.ex.ActiveInfo;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxEntPayOrderInEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("activeService")
public class ActiveServiceImpl implements ActiveService {

    @Autowired
    private SaBillMapperEx saBillMapperEx;
    @Autowired
    private SaBillMapper saBillMapper;
    @Autowired
    private SaActiveMapper activeMapper;
    @Autowired
    private SaCashOutRecordMapper saCashOutRecordMapper;

    @Override
    public List<SaActiveEntity> getList(Integer userId) {
        return null;
    }

    @Override
    public SaActiveEntity getDetail(Integer userId) {
        return null;
    }

    @Override
    public Integer getProfit(Integer userId, Integer activeId) {
        SaBillEntity saBillEntity = new SaBillEntity();
        saBillEntity.setUserId(userId);
        saBillEntity.setActiveId(activeId);
        return saBillMapperEx.totalProfit(saBillEntity);
    }

    @Override
    public List<SmProductEntityEx> goodsInfo(Integer userId, List<SmProductEntityEx> productEntityExes) {
        List<ActiveInfo> activeInfos = getActiveInfo(userId);
        if(activeInfos == null)
            return productEntityExes;
        for (SmProductEntityEx productEntityEx : productEntityExes) {
            productEntityEx.setActiveInfos(JSON.parseArray(JSON.toJSONString(activeInfos),ActiveInfo.class));
        }
        return productEntityExes;
    }

    @Override
    public SmProductEntityEx goodsInfo(Integer userId, SmProductEntityEx smProductEntityEx) {
        List<ActiveInfo> activeInfos = getActiveInfo(userId);
        smProductEntityEx.setActiveInfos(activeInfos);
        return smProductEntityEx;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onCashoutEntPayResult(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity) {
        //付款失败则暂不处理，等待人工放款
        if(spWxEntPayOrderInEntity.getState() != 1)
            return;
        Integer id = spWxEntPayOrderInEntity.getBillPayId();
        List<Integer> ids = CollUtil.newArrayList(spWxEntPayOrderInEntity.getBillIds().split(",")).stream().map(o->Integer.valueOf(o)).collect(Collectors.toList());
        SaBillEntity record = new SaBillEntity();
        record.setState(3);
        Example example = new Example(SaBillEntity.class);
        example.createCriteria().andIn("id",ids);
        saBillMapper.updateByExampleSelective(record,example);
        SaCashOutRecordEntity cashOutRecordEntity = new SaCashOutRecordEntity();
        cashOutRecordEntity.setId(id);
        cashOutRecordEntity.setState(2);
        saCashOutRecordMapper.updateByPrimaryKeySelective(cashOutRecordEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onCashoutAutoEntPayResult(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity) {
        //付款失败则暂不处理，等待人工放款
        if(spWxEntPayOrderInEntity.getState() != 1)
            return;
        Integer id = spWxEntPayOrderInEntity.getBillPayId();
        SaBillEntity billEntity = new SaBillEntity();
        billEntity.setId(id);
        billEntity.setState(3);
        saBillMapper.updateByPrimaryKeySelective(billEntity);
    }

    private List<ActiveInfo> getActiveInfo(Integer userId){
        SaActiveEntity criteria = new SaActiveEntity();
        criteria.setState(1);
        criteria.setType(1);
        List<SaActiveEntity> saActiveEntities = activeMapper.select(criteria);
        if (saActiveEntities == null || saActiveEntities.isEmpty())
            return null;
        List<ActiveInfo> activeInfos = new ArrayList<>();
        for (SaActiveEntity saActiveEntity : saActiveEntities) {
            //检测是否满足活动需求
            if (check(userId, saActiveEntity)) {
                ActiveInfo info = toActiveInfo(saActiveEntity);
                activeInfos.add(info);
            }
        }
        return activeInfos;
    }

    /**
     * 判断用户是否可以继续参加活动
     */
    private boolean check(Integer userId,SaActiveEntity saActiveEntity) {
        if (saActiveEntity.getDays() == null && saActiveEntity.getTimes() == null)
            return true;
        int count = countWithinRule(userId,saActiveEntity);
        return saActiveEntity.getTimes() > count;
    }

    /**
     * 获取用户活动东账单
     * @param userId
     * @param saActiveEntity
     * @return
     */
    private int countWithinRule(Integer userId,SaActiveEntity saActiveEntity) {
        Example example = new Example(SaBillEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("activeId",saActiveEntity.getId());
        if (saActiveEntity.getDays() != null) {
            Date start = DateUtil.beginOfDay(new Date()).offset(DateField.DAY_OF_YEAR, -(saActiveEntity.getDays() - 1));
            Date end = DateUtil.beginOfDay(DateUtil.tomorrow());
            criteria.andBetween("createTime",start,end);
        }
        return saBillMapper.selectCountByExample(example);
    }

    //活动转info
    private ActiveInfo toActiveInfo(SaActiveEntity saActiveEntity) {
        ActiveInfo info = new ActiveInfo();
        info.setActiveId(saActiveEntity.getId());
        info.setMoney(saActiveEntity.getMoney());
        info.setName(saActiveEntity.getName());
        info.setDes(saActiveEntity.getDes());
        return info;
    }
}
