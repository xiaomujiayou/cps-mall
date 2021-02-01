package com.xm.cpsmall.component;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taobao.api.ApiException;
import com.vip.adp.api.open.service.*;
import com.vip.osp.sdk.context.ClientInvocationContext;
import com.vip.osp.sdk.exception.OspException;
import com.xm.cpsmall.comm.api.config.WphApiConfig;
import com.xm.cpsmall.module.mall.constant.AppTypeConstant;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.serialize.bo.ProductCriteriaBo;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsListForm;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.utils.MD5;
import com.xm.cpsmall.utils.mybatis.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WphSdkComponent {

    @Autowired
    private ProfitService profitService;
    @Autowired
    private ClientInvocationContext clientInvocationContext;
    @Autowired
    private WphApiConfig wphApiConfig;

    /**
     * 查询商品接口
     * @param criteria
     * @return
     */
    public PageBean<SmProductEntity> getProductByCriteria(ProductCriteriaBo criteria) throws Exception{
        UnionGoodsServiceHelper.UnionGoodsServiceClient client = new UnionGoodsServiceHelper.UnionGoodsServiceClient();
        client.setClientInvocationContext(clientInvocationContext);
        QueryGoodsRequest request = new QueryGoodsRequest();
        request.setKeyword(criteria.getKeyword());
        Object orderBy = criteria.getOrderBy(PlatformTypeConstant.WPH);
        if(orderBy != null){
            String[] orderByInfo = orderBy.toString().split(" ");
            request.setFieldName(orderByInfo[0]);
            request.setOrder(Integer.valueOf(orderByInfo[1]));
        }
        request.setPage(criteria.getPageNum());
        request.setPageSize(criteria.getPageSize());
        request.setRequestId(UUID.randomUUID().toString());
        request.setPriceStart(criteria.getMinPrice() != null ? (criteria.getMinPrice()/100 + "") : null);
        request.setPriceEnd(criteria.getMaxPrice() != null ? (criteria.getMaxPrice()/100 + "") : null);
        request.setQueryReputation(true);
        request.setQueryStock(true);
        request.setQueryActivity(true);
        request.setCommonParams(createCommonParams(criteria));
        GoodsInfoResponse response = client.query(request);
        return convertPageBean(response,criteria.getPageNum(),criteria.getPageSize());
    }

    private PageBean<SmProductEntity> convertPageBean(GoodsInfoResponse response,Integer pageNum,Integer pageSize) {
        PageBean<SmProductEntity> pageBean = null;
        if(response.getGoodsInfoList() == null || response.getGoodsInfoList().isEmpty()){
            pageBean = new PageBean<>(new ArrayList<>());
        }else {
            pageBean = new PageBean<SmProductEntity>(response.getGoodsInfoList().stream().map(o -> convertGoodsList(o)).filter(o -> o != null).collect(Collectors.toList()));
        }
        pageBean.setTotal(response.getTotal());
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }


    private CommonParams createCommonParams(ProductCriteriaBo criteria) {
        CommonParams params = new CommonParams();
        params.setPlat(criteria.getAppType() == AppTypeConstant.WE_APP ? 3 : criteria.getAppType() == AppTypeConstant.APP ? 2 : criteria.getAppType() == AppTypeConstant.WEB_MO ? 1 : null);
        if(criteria.getUserId() != null){
            params.setDeviceType("IMEI");
            params.setDeviceValue(MD5.md5(criteria.getUserId().toString(),""));
        }
        params.setIp(criteria.getIp());
        return params;
    }


    /**
     * 特定物料商品搜索
     * @return
     * @throws ApiException
     */
    public PageBean<SmProductEntity> optSearch(GoodsListForm goodsListForm, Integer channelType) throws ApiException, OspException {
        UnionGoodsServiceHelper.UnionGoodsServiceClient client = new UnionGoodsServiceHelper.UnionGoodsServiceClient();
        client.setClientInvocationContext(clientInvocationContext);
        GoodsInfoRequest request = new GoodsInfoRequest();
        request.setChannelType(channelType);
        request.setPage(goodsListForm.getPageNum());
        request.setPageSize(goodsListForm.getPageSize());
        request.setRequestId(UUID.randomUUID().toString());
        request.setFieldName("COMMISSION");
        request.setOrder(1);
        request.setQueryReputation(true);
        request.setQueryStock(true);
        request.setQueryActivity(true);
        request.setCommonParams(createCommonParams(goodsListForm));
        GoodsInfoResponse response = client.goodsList(request);
        return convertPageBean(response,goodsListForm.getPageNum(),goodsListForm.getPageSize());
    }

    private CommonParams createCommonParams(GoodsListForm goodsListForm) {
        CommonParams params = new CommonParams();
        params.setPlat(goodsListForm.getAppType() == AppTypeConstant.WE_APP ? 3 : goodsListForm.getAppType() == AppTypeConstant.APP ? 2 : goodsListForm.getAppType() == AppTypeConstant.WEB_MO ? 1 : null);
        if(goodsListForm.getUserId() != null){
            params.setDeviceType("IMEI");
            params.setDeviceValue(MD5.md5(goodsListForm.getUserId().toString(),""));
        }
        params.setIp(goodsListForm.getIp());
        return params;
    }


    private SmProductEntity convertGoodsList(GoodsInfo goodsInfo) {
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.WPH);
        smProductEntity.setGoodsId(goodsInfo.getGoodsId());
        smProductEntity.setGoodsThumbnailUrl(goodsInfo.getGoodsThumbUrl());
        smProductEntity.setName(goodsInfo.getGoodsName());
        smProductEntity.setOriginalPrice(goodsInfo.getVipPrice() != null ? Double.valueOf(Double.valueOf(goodsInfo.getVipPrice()) * 100).intValue() : null);
        smProductEntity.setMallName(goodsInfo.getStoreInfo().getStoreName());
        smProductEntity.setMallId(goodsInfo.getStoreInfo().getStoreId());
        smProductEntity.setBrandLogoUrl(goodsInfo.getBrandLogoFull());
        smProductEntity.setBrandName(goodsInfo.getBrandName());
        smProductEntity.setCommentsRate(goodsInfo.getCommentsInfo().getGoodCommentsShare() != null ? goodsInfo.getCommentsInfo().getGoodCommentsShare().split("\\.")[0] : null);
        smProductEntity.setDiscount(new Double(NumberUtil.mul(Double.valueOf(goodsInfo.getDiscount()),new Double(10))).toString());
        smProductEntity.setPromotionRate(Double.valueOf(Double.valueOf(goodsInfo.getCommissionRate()) * 100d).intValue());
        smProductEntity.setHasCoupon(0);
        smProductEntity.setCouponPrice(0);
        smProductEntity.setCashPrice(Double.valueOf(Double.valueOf(goodsInfo.getCommission()) * 100d).intValue());
        smProductEntity.setOptId(goodsInfo.getCategoryId().toString());
        smProductEntity.setGoodsGalleryUrls(goodsInfo.getGoodsDetailPictures() != null ? goodsInfo.getGoodsDetailPictures().stream().collect(Collectors.joining(",")) : null);
        if(goodsInfo.getJoinedActivities() != null)
            smProductEntity.setServiceTags(goodsInfo.getJoinedActivities().stream().map(ActivityInfo::getActName).collect(Collectors.joining(",")));
//        if(smProductEntity.getName().contains("口罩") || smProductEntity.getName().contains("医") || smProductEntity.getName().contains("药")){
//            return null;
//        }
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }


    public PageBean<SmProductEntityEx> convertSmProductEntityEx(Integer userId, PageBean<SmProductEntity> pageBean){
        List<SmProductEntityEx> list = profitService.calcProfit(pageBean.getList(),userId);
        PageBean<SmProductEntityEx> productEntityExPageBean = new PageBean<>();
        productEntityExPageBean.setList(list);
        productEntityExPageBean.setPageNum(pageBean.getPageNum());
        productEntityExPageBean.setPageSize(pageBean.getPageSize());
        productEntityExPageBean.setTotal(pageBean.getTotal());
        return productEntityExPageBean;
    }

    /**
     * 获取商品详情
     * @param goodsIds
     * @return
     */
    public List<SmProductEntity> details(Integer userId,List<String> goodsIds) throws Exception {
        UnionGoodsServiceHelper.UnionGoodsServiceClient client = new UnionGoodsServiceHelper.UnionGoodsServiceClient();
        client.setClientInvocationContext(clientInvocationContext);
        List<GoodsInfo> goodsList = client.getByGoodsIds(goodsIds,UUID.randomUUID().toString());
        return goodsList.stream().map(o -> convertGoodsList(o)).filter(o -> o != null).collect(Collectors.toList());
    }

    /**
     * 获取单个商品详情
     * 内容更加丰富
     * @param goodsId
     * @return
     * @throws Exception
     */
    public SmProductEntity detail(String goodsId) throws Exception {
        UnionGoodsServiceHelper.UnionGoodsServiceClient client = new UnionGoodsServiceHelper.UnionGoodsServiceClient();
        client.setClientInvocationContext(clientInvocationContext);
        List<GoodsInfo> goodsList = client.getByGoodsIds(Arrays.asList(goodsId),UUID.randomUUID().toString());
        return convertGoodsList(goodsList.get(0));
    }


    /**
     * 获取商品简略信息
     * @param goodsId
     * @return
     * @throws Exception
     */
//    public SmProductSimpleVo basicDetail(String goodsId) throws Exception {
//        return detail(goodsId);
//    }

    /**
     * 获取商品购买信息
     * @param pId
     * @param goodsId
     * @return
     * @throws Exception
     */
    public ShareLinkBo getShareLink(String pId, String goodsId) throws Exception {
        ShareLinkBo shareLinkBo = new ShareLinkBo();
        UnionUrlServiceHelper.UnionUrlServiceClient client = new UnionUrlServiceHelper.UnionUrlServiceClient();
        client.setClientInvocationContext(clientInvocationContext);
        UrlGenResponse response = client.genByGoodsId(Arrays.asList(goodsId),pId,UUID.randomUUID().toString());
        UrlInfo urlInfo = response.getUrlInfoList().get(0);
        shareLinkBo.setWeAppId(wphApiConfig.getWeAppId());
        shareLinkBo.setWePagePath(urlInfo.getVipWxUrl());
        shareLinkBo.setShotUrl(urlInfo.getUrl());
        shareLinkBo.setLongUrl(urlInfo.getLongUrl());
        return shareLinkBo;
    }
}
