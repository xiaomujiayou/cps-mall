package com.xm.cpsmall.module.activite.controller;

import cn.hutool.core.date.DateUtil;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.activite.constant.ActiveConstant;
import com.xm.cpsmall.module.activite.mapper.SaActiveMapper;
import com.xm.cpsmall.module.activite.mapper.SaBillMapper;
import com.xm.cpsmall.module.activite.mapper.custom.SaBillMapperEx;
import com.xm.cpsmall.module.activite.serialize.bo.BillActiveBo;
import com.xm.cpsmall.module.activite.serialize.entity.SaActiveEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaBillEntity;
import com.xm.cpsmall.module.activite.serialize.entity.SaCashOutRecordEntity;
import com.xm.cpsmall.module.activite.serialize.form.ActiveBillListForm;
import com.xm.cpsmall.module.activite.serialize.form.ActiveProfitForm;
import com.xm.cpsmall.module.activite.serialize.vo.BillActiveVo;
import com.xm.cpsmall.module.activite.serialize.vo.CashoutVo;
import com.xm.cpsmall.module.activite.service.ActiveService;
import com.xm.cpsmall.module.activite.service.ActiviteBillService;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.utils.form.BaseForm;
import com.xm.cpsmall.utils.form.ListForm;
import com.xm.cpsmall.utils.lock.DoWorkWithResult;
import com.xm.cpsmall.utils.lock.LockUtil;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.number.LuckUtil;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-active/active")
public class ActiveController {

    @Autowired
    private ActiveService activeService;
    @Autowired
    private SaActiveMapper saActiveMapper;
    @Autowired
    private SaBillMapper saBillMapper;
    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private ActiviteBillService activiteBillService;
    @Autowired
    private SaBillMapperEx saBillMapperEx;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 获取单个商品活动信息
     * @param userId
     * @param productEntityEx
     * @return
     */
    @PostMapping("/goods")
    public SmProductEntityEx goodsActiveInfo(Integer userId, @RequestBody SmProductEntityEx productEntityEx){
        return activeService.goodsInfo(userId,productEntityEx);
    }

    /**
     * 批量获取商品活动信息
     * @param userId
     * @param productEntityExes
     * @return
     */
    @PostMapping("/goods/list")
    public List<SmProductEntityEx> goodsActiveInfo(Integer userId, @RequestBody List<SmProductEntityEx> productEntityExes){
        return activeService.goodsInfo(userId,productEntityExes);
    }

    /**
     * 获取用户活动收入
     * @param activeProfitForm
     * @param bindingResult
     * @return
     */
    @GetMapping("/profit")
    public Map<String,Object> getActiveProfit(@Valid @LoginUser ActiveProfitForm activeProfitForm, BindingResult bindingResult){
        Map<String,Object> result = new HashMap<>();
        Integer profit = activiteBillService.getUserActiveProfit(
                activeProfitForm.getUserId(),
                activeProfitForm.getActiveId(),
                activeProfitForm.getState());
        if(activeProfitForm.getActiveId() != null) {
            //添加活动状态信息
            SaActiveEntity saActiveEntity = saActiveMapper.selectByPrimaryKey(activeProfitForm.getActiveId());
            if(saActiveEntity == null) {
                result.put("state", 0);
            }else {
                result.put("state",saActiveEntity.getState());
            }
        }
        result.put("profit",profit == null ? 0 : profit);
        return result;
    }


    /**
     * 领取视频奖励红包
     */
    @GetMapping("/video/red")
    public Object getVideoRed(@LoginUser BaseForm baseForm){
        SaActiveEntity saActiveEntity = saActiveMapper.selectByPrimaryKey(ActiveConstant.VIDEO_ACTIVE_ID);
        if(saActiveEntity == null || saActiveEntity.getState() != 1)
            throw new GlobleException(MsgEnum.ACTIVE_NOTFOUND_ERROR);
        Lock lock = redisLockRegistry.obtain(this.getClass().getSimpleName()+":"+"videored"+baseForm.getUserId());
        return LockUtil.lock(lock,new DoWorkWithResult<Integer>(){
            @Override
            public Integer dowork() {
                SaBillEntity entity = new SaBillEntity();
                entity.setUserId(baseForm.getUserId());
                entity.setActiveId(ActiveConstant.VIDEO_ACTIVE_ID);
                int count = saBillMapper.selectCount(entity);
                if(count > 0)
                    throw new GlobleException(MsgEnum.ACTIVE_ALREADY_USE,"只能领取一次");
                //随机红包
                int money = LuckUtil.get(
                        new LuckUtil.Config(30,40,70),
                        new LuckUtil.Config(40,60,30),
                        new LuckUtil.Config(70,100,5),
                        new LuckUtil.Config(100,200,1)).random();
                SaBillEntity bill = activiteBillService.createBill(
                        baseForm.getUserId(),
                        ActiveConstant.VIDEO_ACTIVE_ID,
                        1,
                        money,
                        1,
                        null,
                        null,
                        null);
                //付款
                activiteBillService.cashOut(bill,"粉饰生活-看视频领红包活动");
                return money;
            }
        }).get();
    }

    /**
     * 提现
     */
    @GetMapping("/cashout")
    public void cashOut(@LoginUser BaseForm baseForm){
        activiteBillService.cashOut(baseForm.getUserId(),baseForm.getOpenId(),baseForm.getIp());
    }

    /**
     * 活动收益列表
     */
    @GetMapping("/bill/list")
    public PageBean<BillActiveVo> billList(@LoginUser ActiveBillListForm listForm){
        List<BillActiveBo> list = activiteBillService.getList(listForm.getUserId(),listForm.getState(),listForm.getPageNum(),listForm.getPageSize());
        List<BillActiveVo> listVo = list.stream().map(o->{
            BillActiveVo vo = new BillActiveVo();
            vo.setBillId(o.getId());
            vo.setActiveName(o.getSaActiveEntity().getName());
            vo.setMoney(o.getMoney());
            vo.setState(o.getState());
            vo.setAttachDes(o.getAttachDes());
            vo.setFailReason(o.getFailReason());
            vo.setCreateTime(DateUtil.format(o.getCreateTime(),"MM-dd hh:mm"));
            return vo;
        }).collect(Collectors.toList());
        PageBean pageBean = new PageBean(list);
        pageBean.setList(listVo);
        return pageBean;
    }

    @GetMapping("/cashout/list")
    public PageBean<CashoutVo> cashoutList(@LoginUser ListForm listForm){
        List<SaCashOutRecordEntity> list = activiteBillService.getCashoutList(
                listForm.getUserId(),
                null,
                listForm.getPageNum(),
                listForm.getPageSize());
        List<CashoutVo> listVo = list.stream().map(o->{
            CashoutVo vo = new CashoutVo();
            vo.setId(o.getId());
            vo.setMoney(o.getMoney());
            vo.setState(o.getState());
            vo.setCreateTime(DateUtil.format(o.getCreateTime(),"MM-dd hh:mm"));
            return vo;
        }).collect(Collectors.toList());
        PageBean pageBean = new PageBean(list);
        pageBean.setList(listVo);
        return pageBean;
    }
}
