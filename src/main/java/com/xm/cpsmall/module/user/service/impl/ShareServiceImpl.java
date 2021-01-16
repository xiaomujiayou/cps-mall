package com.xm.cpsmall.module.user.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.user.mapper.SuShareMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuOrderEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuShareEntity;
import com.xm.cpsmall.module.user.serialize.vo.ShareVo;
import com.xm.cpsmall.module.user.service.ShareService;
import com.xm.cpsmall.utils.GoodsPriceUtil;
import com.xm.cpsmall.utils.mybatis.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("shareService")
public class ShareServiceImpl implements ShareService {

    @Autowired
    private SuShareMapper suShareMapper;
    @Autowired
    private MallConfigService mallConfigService;
    @Lazy
    @Autowired
    private ShareService shareService;

    private void newShare(Integer userId, SmProductEntityEx smProductEntityEx) {
        SuShareEntity record = new SuShareEntity();
        record.setUserId(userId);
        record.setGoodsId(smProductEntityEx.getGoodsId());
        record.setPlatformType(smProductEntityEx.getType());
        record.setSell(0);
        record.setWatch(1);
        record.setDel(1);
        record.setGoodsInfo(JSON.toJSONString(smProductEntityEx));
        record.setCreateTime(new Date());
        record.setUpdateTime(record.getCreateTime());
        suShareMapper.insertSelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void show(Integer userId,SmProductEntityEx smProductEntityEx) {
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setGoodsId(smProductEntityEx.getGoodsId());
        example.setPlatformType(smProductEntityEx.getType());
        SuShareEntity record = suShareMapper.selectOne(example);
        if(record != null && record.getId() != null){
            example.setGoodsInfo(JSON.toJSONString(smProductEntityEx));
            example.setWatch(example.getWatch() + 1);
            example.setUpdateTime(new Date());
            suShareMapper.updateByPrimaryKeySelective(example);
        }else {
            try {
                newShare(userId,smProductEntityEx);
            }catch (DuplicateKeyException e){
                log.warn("SuShareEntity 重复插入：{}",JSON.toJSONString(example));
                example = suShareMapper.selectOne(example);
                example.setGoodsInfo(JSON.toJSONString(smProductEntityEx));
                example.setWatch(example.getWatch() + 1);
                example.setUpdateTime(new Date());
                suShareMapper.updateByPrimaryKeySelective(example);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void buy(SuOrderEntity suOrderEntity) {
        if(suOrderEntity.getShareUserId() == null)
            return;
        SuShareEntity example = new SuShareEntity();
        example = getShareExtity(suOrderEntity.getShareUserId(),suOrderEntity.getProductId(),suOrderEntity.getPlatformType());
        if(example != null && example.getId() != null){
            String userShareRate = mallConfigService.getConfig(suOrderEntity.getShareUserId(), ConfigEnmu.PRODUCT_SHARE_USER_RATE, ConfigTypeConstant.SELF_CONFIG).getVal();
            Integer willGetMoney = GoodsPriceUtil.type(suOrderEntity.getPlatformType()).calcUserShareProfit(Double.valueOf(suOrderEntity.getPromotionAmount()),Double.valueOf(userShareRate)).intValue();
            example.setWillMakeMoney(example.getWillMakeMoney() + willGetMoney);
            example.setSell(example.getSell() + 1);
            example.setUpdateTime(new Date());
            suShareMapper.updateByPrimaryKeySelective(example);
        }
    }

    @Override
    public void buyFail(SuOrderEntity suOrderEntity) {
        if(suOrderEntity.getShareUserId() == null)
            return;
        SuShareEntity example = new SuShareEntity();
        example.setUserId(suOrderEntity.getShareUserId());
        example.setGoodsId(suOrderEntity.getProductId());
        example.setPlatformType(suOrderEntity.getPlatformType());
        example = getShareExtity(suOrderEntity.getShareUserId(),suOrderEntity.getProductId(),suOrderEntity.getPlatformType());
        if(example != null && example.getId() != null){
            String userShareRate = mallConfigService.getConfig(suOrderEntity.getShareUserId(), ConfigEnmu.PRODUCT_SHARE_USER_RATE,ConfigTypeConstant.SELF_CONFIG).getVal();
            Integer willGetMoney = GoodsPriceUtil.type(suOrderEntity.getPlatformType()).calcUserShareProfit(Double.valueOf(suOrderEntity.getPromotionAmount()),Double.valueOf(userShareRate)).intValue();
            example.setWillMakeMoney(example.getWillMakeMoney() - willGetMoney);
            if(example.getWillMakeMoney() < 0)
                example.setWillMakeMoney(0);
            example.setSell(example.getSell() - 1);
            if(example.getSell() < 0)
                example.setSell(0);
            example.setUpdateTime(new Date());
            suShareMapper.updateByPrimaryKeySelective(example);
        }
    }

    private SuShareEntity getShareExtity(Integer userId,String goodsId,Integer platformType){
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setGoodsId(goodsId);
        example.setPlatformType(platformType);
        return suShareMapper.selectOne(example);
    }

    @Override
    public PageBean<ShareVo> getList(Integer userId, Integer orderType, Integer order, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        StringBuilder sb = new StringBuilder();
        if (order != 0) {
            switch (orderType){
                case 1:{
                    sb.append("watch ");
                    break;
                }
                case 2:{
                    sb.append("sell ");
                    break;
                }
                case 3:{
                    sb.append("will_make_money ");
                    break;
                }
                case 4:{
                    sb.append("create_time ");
                    break;
                }
            }
            if(order == 1) {
                sb.append("asc");
            }else {
                sb.append("desc");
            }
        }else {
            sb.append("update_time desc");
        }
        OrderByHelper.orderBy(sb.toString());
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setDel(1);
        List<SuShareEntity> suShareEntities = suShareMapper.select(example);

        PageBean pageBean = new PageBean(suShareEntities);
        List<ShareVo> vos = suShareEntities.stream().map(o ->{
            SmProductEntityEx smProductEntityEx = JSON.parseObject(o.getGoodsInfo(),SmProductEntityEx.class);
            ShareVo shareVo = convertVo(o,smProductEntityEx);
            return shareVo;
        }).collect(Collectors.toList());
        pageBean.setList(vos);
        return pageBean;

//        PageBean pageBean = new PageBean(suShareEntities);
//         String userBuyRate = mallFeignClient.getOneConfig(userId, ConfigEnmu.PRODUCT_BUY_RATE.getName(),ConfigTypeConstant.PROXY_CONFIG).getVal();
//        String userShareRate = mallFeignClient.getOneConfig(userId, ConfigEnmu.PRODUCT_SHARE_USER_RATE.getName(),ConfigTypeConstant.SELF_CONFIG).getVal();
//        List<SmProductEntity> smProductEntities = mallFeignClient.getProductDetails(suShareEntities.stream().map(o->{
//            ProductIndexBo productIndexBo = new ProductIndexBo();
//            productIndexBo.setGoodsId(o.getGoodsId());
//            productIndexBo.setPlatformType(o.getPlatformType());
//            return productIndexBo;
//        }).collect(Collectors.toList()));
//
//        List<ShareVo> shareVos = suShareEntities.stream().map(o ->{
//            SmProductEntity smProductEntity = smProductEntities.stream().filter(j->{return o.getGoodsId().equals(j.getGoodsId());}).findFirst().get();
//            if(smProductEntity == null)
//                return null;
//            ShareVo shareVo = convertVo(o,smProductEntity,Double.valueOf(userBuyRate),Double.valueOf(userShareRate));
//            return shareVo;
//        }).collect(Collectors.toList());
//        shareVos.remove(null);
//        pageBean.setList(shareVos);
//        return pageBean;
    }

    private ShareVo convertVo(SuShareEntity suShareEntity,SmProductEntityEx smProductEntity){
        ShareVo shareVo = new ShareVo();
        shareVo.setId(suShareEntity.getId());
        shareVo.setGoodsId(smProductEntity.getGoodsId());
        shareVo.setPlatformType(suShareEntity.getPlatformType());
        shareVo.setGoodsImg(smProductEntity.getGoodsThumbnailUrl());
        shareVo.setTitle(smProductEntity.getName());
        shareVo.setOriginalPrice(smProductEntity.getOriginalPrice());
        shareVo.setCoupon(smProductEntity.getCouponPrice());
        shareVo.setRed(smProductEntity.getBuyPrice());
        shareVo.setShareMoney(smProductEntity.getSharePrice());
        shareVo.setShow(suShareEntity.getWatch());
        shareVo.setSellOut(suShareEntity.getSell());
        shareVo.setWillMakeMoney(suShareEntity.getWillMakeMoney());
        shareVo.setSalesTip(smProductEntity.getSalesTip());
        shareVo.setCreateTime(DateUtil.format(suShareEntity.getCreateTime(),"MM-dd HH:mm"));
        return shareVo;
    }

    @Override
    public void del(Integer userId, Integer shareId) {
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setId(shareId);
        example.setDel(0);
        suShareMapper.updateByPrimaryKeySelective(example);
    }
}
