package com.xm.cpsmall.module.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.xm.cpsmall.comm.mq.message.config.BillMqConfig;
import com.xm.cpsmall.module.lottery.serialize.ex.SlPropSpecEx;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxEntPayOrderInEntity;
import com.xm.cpsmall.module.user.constant.BillStateConstant;
import com.xm.cpsmall.module.user.constant.BillTypeConstant;
import com.xm.cpsmall.module.user.constant.OrderStateConstant;
import com.xm.cpsmall.module.user.mapper.SuBillMapper;
import com.xm.cpsmall.module.user.mapper.SuOrderMapper;
import com.xm.cpsmall.module.user.mapper.SuUserMapper;
import com.xm.cpsmall.module.user.mapper.custom.SuBillMapperEx;
import com.xm.cpsmall.module.user.serialize.bo.OrderCustomParameters;
import com.xm.cpsmall.module.user.serialize.bo.SuBillToPayBo;
import com.xm.cpsmall.module.user.serialize.dto.BillOrderDto;
import com.xm.cpsmall.module.user.serialize.entity.SuBillEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.serialize.vo.BillVo;
import com.xm.cpsmall.module.user.service.UserBillService;
import com.xm.cpsmall.utils.GoodsPriceUtil;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.product.GenNumUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.*;
import java.util.stream.Collectors;

@Service("userBillService")
public class UserBillServiceImpl implements UserBillService {

    @Autowired
    private SuBillMapper suBillMapper;
    @Autowired
    private SuBillMapperEx suBillMapperEx;
    @Autowired
    private MallConfigService mallConfigService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SuUserMapper suUserMapper;
    @Autowired
    private SuOrderMapper suOrderMapper;
    @Lazy
    @Autowired
    private UserBillService userBillService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createByOrder(SuOrderEntity order) {
        //订单相关账单存在则返回
        List<SuBillEntity> userRelatedBills = getOrderRelatedBill(order);
        if(userRelatedBills != null && userRelatedBills.size() > 0)
            return;
        OrderCustomParameters params = JSON.parseObject(order.getCustomParameters(), OrderCustomParameters.class);
        Integer shareUserId = params.getShareUserId();
        if(shareUserId == null){
            //生成正常下单账单
            userBillService.createNormalOrderBill(order);
        }else {
            //生成分享订单账单
            userBillService.createShareOrderBill(shareUserId,order);
        }
    }

    /**
     * 创建分享订单所属账单
     * 获取系统购买者分享订单费率 -> 计算购买者收益账单 -> 获取系统分享者订单费率 -> 计算分享者收益账单
     * @param shareUserId
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createShareOrderBill(Integer shareUserId,SuOrderEntity order){
        //属于分享订单
        //生成分享者账单
        Integer shareUserRate = Integer.valueOf(mallConfigService.getConfig(
                shareUserId,
                ConfigEnmu.PRODUCT_SHARE_USER_RATE,
                ConfigTypeConstant.SYS_CONFIG).getVal());
        SuBillEntity shareUserBill = createOrderBill(shareUserId,order, BillTypeConstant.SHARE_PROFIT,shareUserRate,order.getUserId());
        orderBillCreate(shareUserBill);
        //生成购买者订单
        Integer buyUserRate = Integer.valueOf(mallConfigService.getConfig(
                order.getUserId(),
                ConfigEnmu.PRODUCT_SHARE_BUY_RATE,
                ConfigTypeConstant.SYS_CONFIG).getVal());
        SuBillEntity buyUserBill = createOrderBill(order.getUserId(),order,BillTypeConstant.BUY_SHARE,buyUserRate,null);
        orderBillCreate(buyUserBill);
    }

    /**
     * 创建正常购买流程账单
     * 获取系统自购费率 -> 计算购买用户收益账单 -> 获取系统代理费率 -> 计算上级代理收益账单
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNormalOrderBill(SuOrderEntity order){
        //属于正常购买订单
        //生成购买者账单
        Integer buyUserRate = Integer.valueOf(mallConfigService.getConfig(
                order.getUserId(),
                ConfigEnmu.PRODUCT_BUY_RATE,
                ConfigTypeConstant.SYS_CONFIG).getVal());
        SuBillEntity buyUserBill = createOrderBill(order.getUserId(),order,BillTypeConstant.BUY_NORMAL,buyUserRate,null);
        orderBillCreate(buyUserBill);
        //生成代理账单
        //获取代理层级
        Integer proxyLevel = Integer.valueOf(mallConfigService.getConfig(
                null,
                ConfigEnmu.PROXY_LEVEL,
                ConfigTypeConstant.SYS_CONFIG).getVal());
        List<Integer> proxyRate = Lists.newArrayList(mallConfigService.getConfig(
                null,
                ConfigEnmu.PRODUCT_PROXY_RATE,
                ConfigTypeConstant.SYS_CONFIG).getVal().split(","))
                .stream()
                .map(o->{return Integer.valueOf(o);})
                .collect(Collectors.toList());

        List<SuUserEntity> proxyUsers = getParentUser(order.getUserId(),proxyLevel);
        for (int i = 0; i < proxyLevel; i++) {
            if(proxyUsers == null || proxyUsers.size() <= 0 || i > proxyUsers.size() - 1  || proxyUsers.get(i) == null)
                break;
            SuBillEntity proxyBill = createOrderBill(proxyUsers.get(i).getId(),order,BillTypeConstant.PROXY_PROFIT,proxyRate.get(i),i==0?order.getUserId():proxyUsers.get(i-1).getId());
            orderBillCreate(proxyBill);
        }
    }

    /**
     * 获取上级代理
     * @param userId
     * @param level
     * @return
     */
    private List<SuUserEntity> getParentUser(Integer userId, Integer level){
        List<SuUserEntity> result = new ArrayList<>();
        Integer parentId = suUserMapper.selectByPrimaryKey(userId).getParentId();
        for (int i = 0; i < level; i++) {
            if(parentId == null)
                return result;
            SuUserEntity parentUser = suUserMapper.selectByPrimaryKey(parentId);
            result.add(parentUser);
            parentId = parentUser.getParentId();
        }
        return result;
    }

    /**
     * 通过订单和费率生成账单
     * @param userId        :账单所属用户
     * @param order
     * @param rate
     * @return
     */
    private SuBillEntity createOrderBill(Integer userId,SuOrderEntity order,Integer billType,Integer rate,Integer formUserId){
        SuBillEntity bill = new SuBillEntity();
        bill.setUserId(userId);
        bill.setFromUserId(formUserId);
        bill.setMoney(GoodsPriceUtil.type(order.getPlatformType()).calcUserBuyProfit(order.getPromotionAmount().doubleValue(),rate.doubleValue()).intValue());
        bill.setType(billType);
        bill.setAttach(order.getId());
        bill.setPromotionRate(rate);
        bill.setIncome(1);
        bill.setState(BillTypeConstant.BUY_NORMAL);
        bill.setFailReason(order.getFailReason());
        return bill;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payOutOrderBill(SuOrderEntity order) {
        List<SuBillEntity> suBillEntities = getOrderRelatedBill(order);
        suBillEntities.stream()
                .filter(o-> o.getState().equals(BillStateConstant.WAIT))
                .forEach(o->{
                    userBillService.updateBillState(o,BillStateConstant.READY,null);
                    rabbitTemplate.convertAndSend(BillMqConfig.EXCHANGE,BillMqConfig.KEY,o);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void invalidOrderBill(SuOrderEntity order) {
        List<SuBillEntity> suBillEntities = getOrderRelatedBill(order);
        suBillEntities.stream()
                //已返现的账单不再修改状态
                .filter(o -> !o.getState().equals(BillStateConstant.ALREADY))
                .forEach(o->{
                    userBillService.updateBillState(o,BillStateConstant.FAIL,order.getFailReason());
        });
    }
    /**
     * 获取订单相关账单
     * @param suOrderEntity
     * @return
     */
    private List<SuBillEntity> getOrderRelatedBill(SuOrderEntity suOrderEntity){
        SuBillEntity example = new SuBillEntity();
        example.setAttach(suOrderEntity.getId());
        return suBillMapper.select(example);
    }

    @Override
    public PageBean<BillVo> getList(Integer userId, Integer state, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("create_time desc");
        List<SuBillEntity> suBillEntities = null;

        //自购订单包含分享自购
        if(type != null && type == 1){
            Example example = new Example(SuBillEntity.class);
            Example.Criteria criteria =  example.createCriteria()
                    .andEqualTo("userId",userId)
                    .andIn("type",Arrays.asList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.BUY_SHARE));
            if(state != null)
                criteria.andEqualTo("state",state);
            suBillEntities = suBillMapper.selectByExample(example);

        }else {
            SuBillEntity example = new SuBillEntity();
            example.setUserId(userId);
            example.setState(state);
            example.setType(type);
            suBillEntities = suBillMapper.select(example);
        }

        PageBean suBillPageBean = new PageBean(suBillEntities);
        Map<Integer,List<SuBillEntity>> groupMap = suBillEntities.stream().collect(Collectors.groupingBy(SuBillEntity::getType));
        Map<Integer,String> orderIdMap = new HashMap<>();
        Map<Integer,String> userIdMap = new HashMap<>();
        groupMap.entrySet().forEach(o-> {
            //查询订单相关信息
            if(Arrays.asList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.BUY_SHARE,BillTypeConstant.SHARE_PROFIT).contains(o.getKey())){
                o.getValue().forEach(j ->{
                    orderIdMap.put(j.getId(),j.getAttach().toString());
                });
            }else if(Arrays.asList(BillTypeConstant.PROXY_PROFIT).contains(o.getKey())){
                o.getValue().forEach(j ->{
                    userIdMap.put(j.getId(),j.getFromUserId().toString());
                });
            }
        });
        List<SuUserEntity> suUserEntities = !userIdMap.isEmpty() ? suUserMapper.selectByIds(String.join(",",String.join(",",userIdMap.values()))):new ArrayList<>();
        List<SuOrderEntity> suOrderEntities = !orderIdMap.isEmpty() ? suOrderMapper.selectByIds(String.join(",",String.join(",",orderIdMap.values()))):new ArrayList<>();
        List<BillVo> billVos = suBillEntities.stream().map(o->{
            SuOrderEntity suOrderEntity = null;
            SuUserEntity suUserEntity = null;
            suOrderEntity = orderIdMap.get(o.getId()) == null?null: suOrderEntities.stream().filter(j->{return j.getId().equals(Integer.valueOf(orderIdMap.get(o.getId())));}).findFirst().orElse(null);
            suUserEntity = userIdMap.get(o.getId()) == null?null:suUserEntities.stream().filter(j->{return j.getId().equals(Integer.valueOf(userIdMap.get(o.getId())));}).findFirst().orElse(null);
            if(ObjectUtil.isAllEmpty(suOrderEntity,suUserEntity))
                return null;
            return covertBillVo(o,suOrderEntity,suUserEntity);
        }).collect(Collectors.toList());
        CollUtil.removeNull(billVos);
        suBillPageBean.setList(billVos);
        return suBillPageBean;
    }

    @Override
    public List<BillOrderDto> getBillInfo(Integer userId, List<String> billIds) {
        OrderByHelper.orderBy("sb.create_time desc");
        return suBillMapperEx.getBillInfo(userId,billIds);
    }

    private void orderBillCreate(SuBillEntity suBillEntity){
        //过滤零元账单，仅限 订单相关账单
        if(CollUtil.newArrayList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.PROXY_PROFIT,BillTypeConstant.BUY_SHARE,BillTypeConstant.SHARE_PROFIT).contains(suBillEntity.getType()) && suBillEntity.getMoney() <= 0)
            return;
        userBillService.addBill(suBillEntity);
    }

    @Override
    public void addBill(SuBillEntity suBillEntity) {
        suBillMapper.insertUseGeneratedKeys(completeBillInfo(suBillEntity));
    }
    private SuBillEntity completeBillInfo(SuBillEntity suBillEntity){
        suBillEntity.setBillSn(GenNumUtil.genBillNum());
        suBillEntity.setCreateTime(new Date());
        suBillEntity.setUpdateTime(suBillEntity.getCreateTime());
        return suBillEntity;
    }

    @Override
    public void updateBillState(SuBillEntity suBillEntity, Integer newState,String failReason) {
        suBillEntity.setState(newState);
        suBillEntity.setFailReason(failReason);
        suBillEntity.setUpdateTime(new Date());
        suBillMapper.updateByPrimaryKeySelective(suBillEntity);
    }

    @Override
    public SuBillToPayBo createByProp(SlPropSpecEx slPropSpecEx) {
        SuBillEntity suBillEntity = new SuBillEntity();
        suBillEntity.setUserId(slPropSpecEx.getSuUserEntity().getId());
        suBillEntity.setMoney(slPropSpecEx.getPrice());
        suBillEntity.setType(BillTypeConstant.BUY_LOTTERY);
        suBillEntity.setAttach(slPropSpecEx.getId());
        suBillEntity.setState(6);
        suBillEntity.setIncome(2);
        suBillEntity.setDes(slPropSpecEx.getSlPropEntity().getName() + "-" + slPropSpecEx.getName());
        userBillService.addBill(suBillEntity);
        //订单支付超时
        rabbitTemplate.convertAndSend(BillMqConfig.EXCHANGE,BillMqConfig.KEY_PAY_OVERTIME,suBillEntity);
        SuBillToPayBo suBillToPayBo = new SuBillToPayBo();
        BeanUtil.copyProperties(suBillEntity,suBillToPayBo);
        suBillToPayBo.setClientIp(slPropSpecEx.getClientIp());
        suBillToPayBo.setOpenId(slPropSpecEx.getSuUserEntity().getOpenId());
        return suBillToPayBo;
    }

    @Override
    public void payOvertime(SuBillEntity suBillEntity) {
        SuBillEntity suBillEntity1 = suBillMapper.selectByPrimaryKey(suBillEntity.getId());
        if(suBillEntity1 == null || suBillEntity1.getState().equals(7))
            return;
        suBillMapper.deleteByPrimaryKey(suBillEntity.getId());
    }

    @Override
    public void paySucess(SuBillEntity suBillEntity) {
        SuBillEntity suBillEntity1 = suBillMapper.selectByPrimaryKey(suBillEntity.getId());
        if(suBillEntity1 == null)
            return;
        suBillEntity1.setState(7);
        suBillEntity1.setUpdateTime(new Date());
        suBillMapper.updateByPrimaryKeySelective(suBillEntity1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onEntPayResult(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity) {
        //付款失败则暂不处理，等待人工放款
        if(spWxEntPayOrderInEntity.getState() != 1)
            return;
        CollUtil.newArrayList(spWxEntPayOrderInEntity.getBillIds().split(",")).stream().mapToInt(Integer::valueOf).forEach(o->{
            userBillService.updateBillState(suBillMapper.selectByPrimaryKey(o),BillStateConstant.ALREADY,null);
        });


//        Example example = new Example(SuBillEntity.class);
//        example.createCriteria().andIn("id",CollUtil.newArrayList(spWxEntPayOrderInEntity.getBillIds().split(",")));
//        SuBillEntity record = new SuBillEntity();
//        record.setState(BillStateConstant.ALREADY);
//        suBillMapper.updateByExampleSelective(record,example);
    }

    @Override
    public void onCreditDelayArrived(SuBillEntity suBillEntity) {
        userBillService.updateBillState(suBillEntity,BillStateConstant.READY,null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBillStateByOrderStateChange(SuOrderEntity oldOrder, Integer newState,String failReason) {
        if(oldOrder.getState().equals(OrderStateConstant.CONFIRM_RECEIPT) && checkState(oldOrder,newState, OrderStateConstant.ALREADY_SETTLED)){
            //达到发放状态，发放佣金
            userBillService.payOutOrderBill(oldOrder);
        }else if(checkState(oldOrder,newState, OrderStateConstant.FAIL_SETTLED)){
            //达到失败状态，更新状态
            userBillService.invalidOrderBill(oldOrder);
        }
    }
    /**
     * 订单是否到达预期状态
     * @param oldOrder
     * @param orderState    :预期状态(OrderStateConstant)
     * @return
     */
    private boolean checkState(SuOrderEntity oldOrder,Integer newState,Integer... orderState){
        List<Integer> states = Lists.newArrayList(orderState);
        //老订单存在，则只在状态变更时发放收益
        if(states.contains(newState))
            return true;
        return false;
    }

    private BillVo covertBillVo(SuBillEntity suBillEntity,SuOrderEntity suOrderEntity,SuUserEntity suUserEntity){
        BillVo billVo = new BillVo();
        billVo.setMoney(suBillEntity.getMoney());
        billVo.setType(suBillEntity.getType());
        billVo.setState(suBillEntity.getState());
        billVo.setIncome(suBillEntity.getIncome());
        billVo.setTime(DateUtil.format(suBillEntity.getCreateTime(),"MM-dd HH:mm"));
        billVo.setFailReason(suBillEntity.getFailReason());
        billVo.setHeadImg(suUserEntity == null?null:suUserEntity.getHeadImg());
        billVo.setNickname(suUserEntity == null?null:suUserEntity.getNickname());
        billVo.setGoodsId(suOrderEntity == null?null:suOrderEntity.getProductId());
        billVo.setGoodsName(suOrderEntity == null?null:suOrderEntity.getProductName());
        billVo.setPlatformType(suOrderEntity == null?null:suOrderEntity.getPlatformType());
        return billVo;
    }

}
