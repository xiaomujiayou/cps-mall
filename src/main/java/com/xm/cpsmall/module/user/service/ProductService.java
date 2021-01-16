package com.xm.cpsmall.module.user.service;

import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.user.serialize.vo.SuProductHistoryVo;
import com.xm.cpsmall.utils.mybatis.PageBean;

public interface ProductService {

    /**
     * 获取用户商品信息
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param suProductType
     * @return
     */
    public PageBean<SuProductHistoryVo> getUserProduct(Integer userId, Integer pageNum, Integer pageSize, Integer suProductType);

    /**
     * 添加浏览历史记录
     * @param userId
     */
    public void addOrUpdateHistory(Integer userId, Integer shareUserId, String ip, Integer appType, SmProductEntityEx smProductEntityEx);

    /**
     * 添加一个新纪录
     * @param userId
     * @param shareUserId
     * @param smProductEntityEx
     */
    public void newOne(Integer userId, Integer shareUserId, String ip, Integer appType, SmProductEntityEx smProductEntityEx);

    /**
     * @param userId
     * @param id
     */
    public void delHistory(Integer userId, Integer id, Integer productType);

    /**
     * 是否收藏
     * @param userId
     * @param platformType
     * @param goodsId
     * @return
     */
    public boolean isCollect(Integer userId, Integer platformType, String goodsId);

    /**
     * 取消或搜藏一个商品
     * @param userId
     * @param platformType
     * @param goodsId
     */
    public void collect(Integer userId, Integer platformType, String goodsId, Integer shareUserId, Boolean isCollect);
}
