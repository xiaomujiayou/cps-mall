package com.xm.cpsmall.module.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.user.mapper.SuProductMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuProductEntity;
import com.xm.cpsmall.module.user.serialize.vo.SuProductHistoryVo;
import com.xm.cpsmall.module.user.service.ProductService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private SuProductMapper suProductMapper;
    @Lazy
    @Autowired
    private ProductService productService;

    @Override
    public PageBean<SuProductHistoryVo> getUserProduct(Integer userId, Integer pageNum, Integer pageSize, Integer suProductType) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setUserId(userId);
        if(suProductType == 2){
            //历史数据
            suProductEntity.setDel(1);
            OrderByHelper.orderBy("update_time desc");

        }else if(suProductType == 1){
            //收藏数据
            suProductEntity.setIsCollect(1);
            OrderByHelper.orderBy("collect_time desc");
        }else {
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"无效类型");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<SuProductEntity> suProductEntities = suProductMapper.select(suProductEntity);
        PageBean pageBean = new PageBean(suProductEntities);
        List<SuProductHistoryVo> list = suProductEntities
                .stream()
                .map(o -> {
                    SmProductEntityEx smProductEntityEx = JSON.parseObject(o.getGoodsInfo(),SmProductEntityEx.class);
                    SuProductHistoryVo suProductHistoryVo = new SuProductHistoryVo();
                    BeanUtil.copyProperties(smProductEntityEx,suProductHistoryVo);
                    suProductHistoryVo.setProductType(suProductType);
                    suProductHistoryVo.setItemId(o.getId());
                    suProductHistoryVo.setShareUserId(o.getShareUserId());
                    return suProductHistoryVo;
                })
                .collect(Collectors.toList());
        pageBean.setList(list);
        return pageBean;
    }

    @Override
    public void addOrUpdateHistory(Integer userId, Integer shareUserId,String ip,Integer appType, SmProductEntityEx smProductEntityEx) {
        SuProductEntity example = new SuProductEntity();
        example.setUserId(userId);
        example.setGoodsId(smProductEntityEx.getGoodsId());
        example.setPlatformType(smProductEntityEx.getType());
        SuProductEntity record = suProductMapper.selectOne(example);
        if(record == null || record.getId() == null){
            try {
                productService.newOne(userId,shareUserId,ip,appType,smProductEntityEx);
            }catch (DuplicateKeyException e){
                log.warn("SuProductEntity 重复插入：{}",JSON.toJSONString(example));
                record = suProductMapper.selectOne(example);
                record.setDel(1);
                record.setUpdateTime(new Date());
                record.setGoodsInfo(JSON.toJSONString(smProductEntityEx));
                if(shareUserId != null)
                    record.setShareUserId(shareUserId);
                suProductMapper.updateByPrimaryKeySelective(record);
            }

        }else {
            record.setDel(1);
            record.setUpdateTime(new Date());
            record.setGoodsInfo(JSON.toJSONString(smProductEntityEx));
            if(shareUserId != null)
                record.setShareUserId(shareUserId);
            suProductMapper.updateByPrimaryKeySelective(record);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void newOne(Integer userId, Integer shareUserId,String ip,Integer appType, SmProductEntityEx smProductEntityEx) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setIp(ip);
        suProductEntity.setAppType(appType);
        suProductEntity.setUserId(userId);
        suProductEntity.setGoodsId(smProductEntityEx.getGoodsId());
        suProductEntity.setPlatformType(smProductEntityEx.getType());
        suProductEntity.setGoodsInfo(JSON.toJSONString(smProductEntityEx));
        suProductEntity.setShareUserId(shareUserId);
        suProductEntity.setIsCollect(0);
        suProductEntity.setDel(1);
        suProductEntity.setUpdateTime(new Date());
        suProductEntity.setCreateTime(suProductEntity.getUpdateTime());
        suProductMapper.insert(suProductEntity);
    }

    @Override
    public void delHistory(Integer userId, Integer id, Integer productType) {
        if(id == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"id不能为空");
        Example example = new Example(SuProductEntity.class);
        SuProductEntity suProductEntity = new SuProductEntity();
        if(productType == 1){
            suProductEntity.setId(id);
            suProductEntity.setUserId(userId);
            suProductEntity.setIsCollect(0);
            suProductMapper.updateByPrimaryKeySelective(suProductEntity);
            return;
        }else if(productType == 2){
            suProductEntity.setId(id);
            suProductEntity.setUserId(userId);
            suProductEntity.setDel(0);
            suProductMapper.updateByExampleSelective(suProductEntity,example);
        }
    }

    @Override
    public boolean isCollect(Integer userId, Integer platformType, String goodsId) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setUserId(userId);
        suProductEntity.setPlatformType(platformType);
        suProductEntity.setGoodsId(goodsId);
        suProductEntity = suProductMapper.selectOne(suProductEntity);
        if(suProductEntity == null || suProductEntity.getId() == null)
            return false;
        return suProductEntity.getIsCollect() > 0 ? true:false;
    }

    @Override
    public void collect(Integer userId, Integer platformType, String goodsId,Integer shareUserId, Boolean isCollect) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setUserId(userId);
        suProductEntity.setPlatformType(platformType);
        suProductEntity.setGoodsId(goodsId);
        suProductEntity = suProductMapper.selectOne(suProductEntity);
        if(suProductEntity == null || suProductEntity.getId() == null)
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS,"商品不存在");
        if(isCollect){
            suProductEntity.setIsCollect(1);
            suProductEntity.setCollectTime(new Date());
            suProductMapper.updateByPrimaryKeySelective(suProductEntity);
        }else {
            suProductEntity.setIsCollect(0);
            suProductMapper.updateByPrimaryKeySelective(suProductEntity);
        }
    }
}
