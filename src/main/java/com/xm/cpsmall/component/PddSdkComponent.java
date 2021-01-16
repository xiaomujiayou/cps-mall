package com.xm.cpsmall.component;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.*;
import com.pdd.pop.sdk.http.api.pop.response.*;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.constant.*;
import com.xm.cpsmall.module.mall.serialize.bo.*;
import com.xm.cpsmall.module.mall.serialize.entity.SmConfigEntity;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.MallGoodsListForm;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductSimpleVo;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.utils.EnumUtils;
import com.xm.cpsmall.utils.GoodsPriceUtil;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PddSdkComponent {
    @Autowired
    private PopHttpClient popHttpClient;
    @Autowired
    private MallConfigService mallConfigService;
    @Autowired
    private ProfitService profitService;

    /**
     * 查询商品接口
     *
     * @param criteria
     * @return
     */
    public PageBean<SmProductEntity> getProductByCriteria(ProductCriteriaBo criteria) throws Exception {
        //获取排序规则
        SmConfigEntity sortConfig = mallConfigService.getConfig(criteria.getUserId(), ConfigEnmu.PDD_PRODUCT_BEST_LIST_SORT, ConfigTypeConstant.PROXY_CONFIG);
        //获取店铺类型规则
        SmConfigEntity shopTypeConfig = mallConfigService.getConfig(criteria.getUserId(), ConfigEnmu.PDD_PRODUCT_BEST_LIST_SHOP_TYPE, ConfigTypeConstant.PROXY_CONFIG);
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
        request.setPid(criteria.getPid());
        Map<String, Object> params = new HashMap<>();
        params.put("uid", criteria.getUserId());
        if (!isRecord(criteria.getPid(), JSON.toJSONString(params))) {
            //尚未备案
            throw new GlobleException(MsgEnum.PDD_AUTH_ERROR);
        }

        request.setCustomParameters(JSON.toJSONString(params));
        request.setSortType(sortConfig.getVal() == null ? null : Integer.valueOf(sortConfig.getVal()));
        request.setMerchantType(shopTypeConfig.getVal() == null ? null : Integer.valueOf(shopTypeConfig.getVal()));
        request.setPage(criteria.getPageNum());
        request.setPageSize(criteria.getPageSize());
        request.setOptId(criteria.getOptionId() != null ? Long.valueOf(criteria.getOptionId()) : null);
        request.setKeyword(criteria.getKeyword());
        request.setGoodsIdList(criteria.getGoodsIdList() != null ? criteria.getGoodsIdList().stream().map(Long::valueOf).collect(Collectors.toList()) : null);
        request.setSortType(criteria.getOrderBy(PlatformTypeConstant.PDD) == null ? null : Integer.valueOf(criteria.getOrderBy(PlatformTypeConstant.PDD).toString()));
        request.setActivityTags(criteria.getActivityTags());
        request.setWithCoupon(criteria.getHasCoupon());
        //筛选器
        List<PddDdkGoodsSearchRequest.RangeListItem> rangeList = new ArrayList<>();
        //价格区间
        if (criteria.getMaxPrice() != null && criteria.getMinPrice() != null) {
//            Map<String,Object> range = new HashMap<>();
//            range.put("range_id",0);
//            range.put("range_from",criteria.getMinPrice());
//            range.put("range_to",criteria.getMaxPrice().equals(0)?10000000:criteria.getMaxPrice());
//            rangeList.add(range);
            PddDdkGoodsSearchRequest.RangeListItem rangeListItem = new PddDdkGoodsSearchRequest.RangeListItem();
            rangeListItem.setRangeId(0);
            rangeListItem.setRangeFrom(criteria.getMinPrice().longValue());
            rangeListItem.setRangeTo(criteria.getMaxPrice().equals(0) ? 10000000l : criteria.getMaxPrice().longValue());
            rangeList.add(rangeListItem);
        }

        if (!rangeList.isEmpty()) {
            request.setRangeList(rangeList);
        }
        PddDdkGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
        List<SmProductEntity> smProductEntityList = goodsList.stream().map(o -> convertGoodsList(o)).collect(Collectors.toList());
        return packageToPageBean(
                smProductEntityList,
                response.getGoodsSearchResponse().getTotalCount().intValue(),
                criteria.getPageNum(),
                criteria.getPageSize());

    }

    /**
     * 获取店铺商品
     */
    public PageBean<SmProductEntity> mallGoodsList(MallGoodsListForm mallGoodsListForm) throws Exception {
        PddDdkMallGoodsListGetRequest request = new PddDdkMallGoodsListGetRequest();
        request.setMallId(Long.valueOf(mallGoodsListForm.getMallId()));
        request.setPageNumber(mallGoodsListForm.getPageNum());
        request.setPageSize(mallGoodsListForm.getPageSize());
        PddDdkMallGoodsListGetResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkMallGoodsListGetResponse.GoodsInfoListResponseGoodsListItem> list = response.getGoodsInfoListResponse().getGoodsList();
        List<SmProductEntity> smProductEntityList = list.stream().map(o -> convertGoodsList(o)).collect(Collectors.toList());
        return packageToPageBean(
                smProductEntityList,
                response.getGoodsInfoListResponse().getTotal().intValue(),
                mallGoodsListForm.getPageNum(),
                mallGoodsListForm.getPageSize());
    }

    /**
     * 获取商品详情
     *
     * @param goodsIds
     * @return
     */
    public List<SmProductEntity> details(List<String> goodsIds) throws Exception {
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
        request.setGoodsIdList(goodsIds.stream().map(o -> Long.valueOf(o)).collect(Collectors.toList()));
        PddDdkGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
        List<SmProductEntity> smProductEntityList = goodsList.stream().map(o -> {
            return convertGoodsList(o);
        }).collect(Collectors.toList());
        return smProductEntityList;
    }

    /**
     * 获取单个商品详情
     * 内容更加丰富
     *
     * @param goodsId
     * @return
     * @throws Exception
     */
    public SmProductEntity detail(String goodsId, String pid) throws Exception {
        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
        request.setGoodsIdList(Arrays.asList(Long.valueOf(goodsId)));
        request.setPid(pid);
        PddDdkGoodsDetailResponse response = popHttpClient.syncInvoke(request);
        if (response.getGoodsDetailResponse().getGoodsDetails() == null || response.getGoodsDetailResponse().getGoodsDetails().size() <= 0)
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS, "找不到拼多多商品：" + goodsId);
        return convertDetail(response.getGoodsDetailResponse().getGoodsDetails().get(0));
    }

    /**
     * 获取商品简略信息
     *
     * @param goodsId
     * @return
     * @throws Exception
     */
    public SmProductSimpleVo basicDetail(Long goodsId) throws Exception {
        PddDdkGoodsBasicInfoGetRequest request = new PddDdkGoodsBasicInfoGetRequest();
        request.setGoodsIdList(Arrays.asList(goodsId));
        PddDdkGoodsBasicInfoGetResponse response = popHttpClient.syncInvoke(request);
        SmProductSimpleVo smProductSimpleVo = new SmProductSimpleVo();
        smProductSimpleVo.setGoodsId(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getGoodsId().toString());
        smProductSimpleVo.setGoodsThumbnailUrl(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getGoodsPic());
        smProductSimpleVo.setName(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getGoodsName());
        smProductSimpleVo.setOriginalPrice(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getMinGroupPrice().intValue());
        smProductSimpleVo.setType(PlatformTypeConstant.PDD);
        return smProductSimpleVo;
    }

    /**
     * 获取商品购买信息
     *
     * @param customParams
     * @param pId
     * @param goodsId
     * @return
     * @throws Exception
     */
    public ShareLinkBo getShareLink(String customParams, String pId, String goodsId, String couponId) throws Exception {
        PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
        request.setPId(pId);
        request.setGoodsIdList(Arrays.asList(Long.valueOf(goodsId)));
        request.setCustomParameters(customParams);
        request.setGenerateShortUrl(true);
        request.setGenerateWeApp(true);
        PddDdkGoodsPromotionUrlGenerateResponse response = popHttpClient.syncInvoke(request);
        ShareLinkBo shareLinkBo = new ShareLinkBo();
        PddDdkGoodsPromotionUrlGenerateResponse.GoodsPromotionUrlGenerateResponseGoodsPromotionUrlListItem item = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0);
        shareLinkBo.setWeIconUrl(item.getWeAppInfo().getWeAppIconUrl());
        shareLinkBo.setWeBannerUrl(item.getWeAppInfo().getBannerUrl());
        shareLinkBo.setWeDesc(item.getWeAppInfo().getDesc());
        shareLinkBo.setWeSourceDisplayName(item.getWeAppInfo().getSourceDisplayName());
        shareLinkBo.setWePagePath(item.getWeAppInfo().getPagePath());
        shareLinkBo.setWeUserName(item.getWeAppInfo().getUserName());
        shareLinkBo.setWeTitle(item.getWeAppInfo().getTitle());
        shareLinkBo.setWeAppId(item.getWeAppInfo().getAppId());
        shareLinkBo.setShotUrl(item.getShortUrl());
        shareLinkBo.setLongUrl(item.getUrl());
        return shareLinkBo;
    }

    /**
     * 生成推广位id
     */
    public String generatePid(Integer userId) throws Exception {
        PddDdkGoodsPidGenerateRequest request = new PddDdkGoodsPidGenerateRequest();
        request.setNumber(1L);
        request.setPIdNameList(Arrays.asList(userId.toString()));
        PddDdkGoodsPidGenerateResponse response = popHttpClient.syncInvoke(request);
        log.info("pdd 剩余推广位：[{}]", response.getPIdGenerateResponse().getRemainPidCount());
        return response.getPIdGenerateResponse().getPIdList().get(0).getPId();
    }


    /**
     * 获取系统时间
     *
     * @return
     */
    public Date getTime() throws Exception {
        PddTimeGetRequest request = new PddTimeGetRequest();
        PddTimeGetResponse response = popHttpClient.syncInvoke(request);
        return DateUtil.parse(response.getTimeGetResponse().getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 多多客获取爆款排行商品接口
     *
     * @param type     :商品类型(1:热销榜,2:收益榜)
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Cacheable(value = "pdd.getTopGoodsList", key = "#type + '_' + #pageNum + '_' + #pageSize ")
    public PageBean<SmProductEntity> getTopGoodsList(Integer type, String pid, Integer pageNum, Integer pageSize) throws Exception {
        PddDdkTopGoodsListQueryRequest request = new PddDdkTopGoodsListQueryRequest();
        request.setOffset(PageUtil.getStart(pageNum, pageSize));
        request.setLimit(pageSize);
        request.setSortType(type);
        request.setPId(pid);
        PddDdkTopGoodsListQueryResponse response = popHttpClient.syncInvoke(request);
        List<SmProductEntity> list = response.getTopGoodsListGetResponse().getList().stream().map(o -> {
            return convertGoodsList(o);
        }).collect(Collectors.toList());
        return packageToPageBean(
                list,
                response.getTopGoodsListGetResponse().getTotal().intValue(),
                pageNum,
                pageSize);
    }

    /**
     * 获取主题推广类型
     *
     * @return
     */
    @Cacheable(value = "pdd.getThemeList")
    public List<PddThemeBo> getThemeList() throws Exception {
        PddDdkThemeListGetRequest request = new PddDdkThemeListGetRequest();
        PddDdkThemeListGetResponse response = popHttpClient.syncInvoke(request);
        List<PddThemeBo> list = response.getThemeListGetResponse().getThemeList().stream().map(o -> {
            PddThemeBo pddThemeBo = new PddThemeBo();
            pddThemeBo.setId(o.getId().intValue());
            pddThemeBo.setName(o.getName());
            pddThemeBo.setImageUrl(o.getImageUrl());
            pddThemeBo.setGoodsNum(o.getGoodsNum().intValue());
            return pddThemeBo;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取主题商品列表
     *
     * @param themeId
     * @return
     */
    @Cacheable(value = "pdd.getThemeGoodsList", key = "#themeId")
    public PageBean<SmProductEntity> getThemeGoodsList(Integer themeId, String pid) throws Exception {
        PddDdkThemeGoodsSearchRequest request = new PddDdkThemeGoodsSearchRequest();
        request.setThemeId(themeId.longValue());
        PddDdkThemeGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<SmProductEntity> list = response.getThemeListGetResponse().getGoodsList().stream().map(o -> {
            return convertGoodsList(o);
        }).collect(Collectors.toList());
        return packageToPageBean(
                list,
                response.getThemeListGetResponse().getTotal().intValue(),
                1,
                response.getThemeListGetResponse().getTotal().intValue());
    }

    /**
     * 获取运营频道商品
     *
     * @param channelType
     * @return
     * @throws Exception
     */
    @Cacheable(value = "pdd.getRecommendGoodsList", key = "#channelType + '_' + #pageNum + '_' + #pageSize")
    public PageBean<SmProductEntity> getRecommendGoodsList(String pid, Integer channelType, Integer pageNum, Integer pageSize) throws Exception {
        PddDdkGoodsRecommendGetRequest request = new PddDdkGoodsRecommendGetRequest();
        request.setChannelType(channelType);
        request.setOffset(PageUtil.getStart(pageNum, pageSize));
        request.setLimit(pageSize);
        request.setPid(pid);
        PddDdkGoodsRecommendGetResponse response = popHttpClient.syncInvoke(request);
        List<SmProductEntity> list = response.getGoodsBasicDetailResponse().getList().stream().map(o -> {
            return convertGoodsList(o);
        }).collect(Collectors.toList());
        return packageToPageBean(
                list,
                response.getGoodsBasicDetailResponse().getTotal().intValue(),
                pageNum,
                pageSize);
    }

    /**
     * 获取拼多多授权信息
     *
     * @param pid
     * @param userId
     * @return
     */
    public PddAuthBo getAuthInfo(String pid, Integer userId) throws Exception {
        PddDdkRpPromUrlGenerateRequest request = new PddDdkRpPromUrlGenerateRequest();
        request.setChannelType(10);
        request.setPIdList(CollUtil.newArrayList(pid));
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        request.setCustomParameters(JSON.toJSONString(params));
        request.setGenerateWeApp(true);
        PddDdkRpPromUrlGenerateResponse response = popHttpClient.syncInvoke(request);
        PddDdkRpPromUrlGenerateResponse.RpPromotionUrlGenerateResponseUrlListItem item = response.getRpPromotionUrlGenerateResponse().getUrlList().get(0);
        PddAuthBo pddAuthBo = new PddAuthBo();
        pddAuthBo.setMobileUrl(item.getMobileUrl());
        pddAuthBo.setUrl(item.getUrl());
        PddAuthBo.WeAppInfoBean weAppInfoBean = new PddAuthBo.WeAppInfoBean();
        weAppInfoBean.setAppId(item.getWeAppInfo().getAppId());
        weAppInfoBean.setDesc(item.getWeAppInfo().getDesc());
        weAppInfoBean.setPagePath(item.getWeAppInfo().getPagePath());
        weAppInfoBean.setSourceDisplayName(item.getWeAppInfo().getSourceDisplayName());
        weAppInfoBean.setTitle(item.getWeAppInfo().getTitle());
        weAppInfoBean.setUserName(item.getWeAppInfo().getUserName());
        weAppInfoBean.setWeAppIconUrl(item.getWeAppInfo().getWeAppIconUrl());
        pddAuthBo.setWeAppInfo(weAppInfoBean);
        return pddAuthBo;
    }

    public PageBean<SmProductEntityEx> convertSmProductEntityEx(Integer userId, PageBean<SmProductEntity> pageBean) {
        List<SmProductEntityEx> list = profitService.calcProfit(pageBean.getList(), userId);
        PageBean<SmProductEntityEx> productEntityExPageBean = new PageBean<>();
        productEntityExPageBean.setList(list);
        productEntityExPageBean.setPageNum(pageBean.getPageNum());
        productEntityExPageBean.setPageSize(pageBean.getPageSize());
        productEntityExPageBean.setTotal(pageBean.getTotal());
        return productEntityExPageBean;
    }

    private PageBean<SmProductEntity> packageToPageBean(List<SmProductEntity> list, Integer total, Integer pageNum, Integer pageSize) {
        list = CollUtil.removeNull(list);

        //苹果机不支持虚拟类目
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ua = request.getHeader("User-Agent");
        if(ua != null && ua.contains("iPhone")) {
            list = list.stream()
                    .filter(o -> !CollUtil.newHashSet("10141","23154","590","10696","23905").contains(o.getOptId()))
                    .filter(o -> !o.getName().contains("会员"))
                    .filter(o -> !o.getName().contains("充值"))
                    .filter(o -> !o.getName().contains("年卡"))
                    .filter(o -> !o.getName().contains("月卡"))
                    .filter(o -> !o.getName().contains("一天"))
                    .filter(o -> !o.getName().contains("代金券"))
                    .filter(o -> !o.getName().contains("优惠券"))
                    .filter(o -> !o.getName().contains("网盘"))
                    .filter(o -> !o.getName().contains("下载"))
                    .filter(o -> !o.getName().contains("激活"))
                    .filter(o -> !o.getName().contains("兑换"))
                    .filter(o -> !o.getName().contains("许可证"))
                    .filter(o -> !o.getName().contains("电子书"))
                    .filter(o -> !o.getName().toLowerCase().contains("vip"))
                    .filter(o -> !o.getName().toLowerCase().contains("txt"))
                    .filter(o -> !o.getName().toLowerCase().contains("office"))
                    .filter(o -> !o.getName().toLowerCase().contains("wps"))
                    .collect(Collectors.toList());
        }
        PageBean<SmProductEntity> pageBean = new PageBean<>(list);
        pageBean.setTotal(total);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    private SmProductEntity convertGoodsList(Object listItem) {
        PddGoodsListItem pddGoodsListItem = new PddGoodsListItem();
        BeanUtil.copyProperties(listItem, pddGoodsListItem);
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.PDD);
        smProductEntity.setGoodsId(pddGoodsListItem.getGoodsId().toString());
        smProductEntity.setGoodsThumbnailUrl(pddGoodsListItem.getGoodsThumbnailUrl());
        smProductEntity.setName(pddGoodsListItem.getGoodsName());
        smProductEntity.setOriginalPrice(pddGoodsListItem.getMinGroupPrice().intValue());
        smProductEntity.setCouponPrice(pddGoodsListItem.getCouponDiscount().intValue());
        smProductEntity.setMallName(pddGoodsListItem.getMallName());
        smProductEntity.setSalesTip(pddGoodsListItem.getSalesTip());
        smProductEntity.setMallCps(pddGoodsListItem.getMallCps());
        smProductEntity.setPromotionRate(pddGoodsListItem.getPromotionRate().intValue() * 10);
        smProductEntity.setHasCoupon(pddGoodsListItem.getCouponDiscount() > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.PDD).calcProfit(smProductEntity).intValue());
        smProductEntity.setOptId(pddGoodsListItem.getOptId().toString());
//        if(smProductEntity.getName().contains("口罩") || smProductEntity.getName().contains("医") || smProductEntity.getName().contains("药") || !optionService.checkOpt(smProductEntity.getOptId())){
//        if(smProductEntity.getName().contains("口罩") || smProductEntity.getName().contains("医") || smProductEntity.getName().contains("药")){
//            return null;
//        }
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }

    private SmProductEntity convertDetail(PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem detailsItem) {
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.PDD);
        smProductEntity.setGoodsId(detailsItem.getGoodsId().toString());
        smProductEntity.setGoodsThumbnailUrl(detailsItem.getGoodsImageUrl());
        smProductEntity.setGoodsGalleryUrls(String.join(",", detailsItem.getGoodsGalleryUrls()));
        smProductEntity.setName(detailsItem.getGoodsName());
        smProductEntity.setDes(detailsItem.getGoodsDesc());
        smProductEntity.setOriginalPrice(detailsItem.getMinGroupPrice().intValue());
        smProductEntity.setCouponPrice(detailsItem.getCouponDiscount().intValue());
        smProductEntity.setMallId(detailsItem.getMallId() + "");
        smProductEntity.setMallName(detailsItem.getMallName());
        smProductEntity.setDiscount(detailsItem.getMallCouponDiscountPct().toString());
        smProductEntity.setSalesTip(detailsItem.getSalesTip());
        smProductEntity.setMallCps(2);
        smProductEntity.setPromotionRate(detailsItem.getPromotionRate().intValue() * 10);
        smProductEntity.setHasCoupon(detailsItem.getCouponTotalQuantity() > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.PDD).calcProfit(smProductEntity).intValue());
        if (detailsItem.getServiceTags() != null)
            smProductEntity.setServiceTags(String.join(",", getServiceTags(detailsItem.getServiceTags())));
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }

    private List<String> getServiceTags(List<Integer> serviceTags) {
        if (serviceTags == null || serviceTags.size() <= 0)
            return null;
        List<String> tags = serviceTags.stream().map(o -> {
            try {
                PddServiceTagEnum pddServiceTagEnum = EnumUtils.getEnum(PddServiceTagEnum.class, "tagId", o);
                if (pddServiceTagEnum != null && pddServiceTagEnum.getShow()) {
                    return pddServiceTagEnum.getTagName();
                }
            } catch (Exception e) {
                log.error("{}", e);
            }
            return null;
        }).collect(Collectors.toList());
        CollUtil.removeNull(tags);
        return tags;
    }

    private String getActiviteType(Integer activiteType) {
        if (activiteType == null)
            return null;
        try {
            return EnumUtils.getEnum(PddActivityTypeEnum.class, "activityId", activiteType).getActivityName();
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    /**
     * 查询是否备案
     *
     * @param pid
     * @param customParamters
     * @return
     */
    private Boolean isRecord(String pid, String customParamters) throws Exception {
        PddDdkMemberAuthorityQueryRequest request = new PddDdkMemberAuthorityQueryRequest();
        request.setPid(pid);
        request.setCustomParameters(customParamters);
        PddDdkMemberAuthorityQueryResponse response = popHttpClient.syncInvoke(request);
        return response.getAuthorityQueryResponse().getBind() == 1;
    }

    public Boolean isRecord(String pid, Integer userId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        return isRecord(pid, JSON.toJSONString(params));
    }


}
