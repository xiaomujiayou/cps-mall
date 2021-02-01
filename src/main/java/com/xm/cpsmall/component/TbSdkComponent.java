package com.xm.cpsmall.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taobao.api.ApiException;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;
import com.xm.cpsmall.comm.api.client.MyTaobaoClient;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.serialize.bo.ProductCriteriaBo;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsListForm;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductSimpleVo;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.utils.GoodsPriceUtil;
import com.xm.cpsmall.utils.MD5;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TbSdkComponent {

    @Autowired
    private ProfitService profitService;
    @Autowired
    private MyTaobaoClient myTaobaoClient;

    /**
     * 查询商品接口
     * @param criteria
     * @return
     */
    public PageBean<SmProductEntity> getProductByCriteria(ProductCriteriaBo criteria) throws Exception{

        /**
         * 6707 :智能搜索
         */
        return baseSearch(criteria,110173550272L,6707L);

    }

    /**
     * 全网商品搜索
     * @param criteria
     * @param adzoneId
     * @param materialId
     * @return
     * @throws ApiException
     */
    private PageBean<SmProductEntity> baseSearch(ProductCriteriaBo criteria,Long adzoneId,Long materialId) throws ApiException {
        TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
        request.setMaterialId(materialId);
        request.setAdzoneId(adzoneId);
        request.setPageNo(criteria.getPageNum().longValue());
        request.setPageSize(criteria.getPageSize().longValue());
        request.setCat(criteria.getOptionId());
        request.setQ(criteria.getKeyword());
        request.setSort(criteria.getOrderBy(PlatformTypeConstant.TB) == null ? null : criteria.getOrderBy(PlatformTypeConstant.TB).toString());
        request.setStartPrice(criteria.getMinPrice() == null ? null : criteria.getMinPrice().longValue());
        request.setEndPrice(criteria.getMaxPrice() == null ? null : criteria.getMaxPrice().longValue());
        request.setHasCoupon(criteria.getHasCoupon());
        request.setNeedFreeShipment(criteria.getParcels());
        request.setIsTmall(criteria.getIsTmall());
        request.setItemloc(criteria.getLocation());
        request.setIp(criteria.getIp());
        request.setDeviceType("IMEI");
        request.setDeviceEncrypt("MD5");
        request.setDeviceValue(MD5.md5(criteria.getUserId().toString(),""));
        TbkDgMaterialOptionalResponse response = myTaobaoClient.execute(request);
        List<SmProductEntity> smProductEntities = response.getResultList().stream().map(o -> convertGoodsList(o)).collect(Collectors.toList());
        return packageToPageBean(
                smProductEntities,
                response.getTotalResults().intValue(),
                criteria.getPageNum(),
                criteria.getPageSize());
    }


    /**
     * 特定物料商品搜索
     * @param adzoneId
     * @param materialId
     * @return
     * @throws ApiException
     */
    public PageBean<SmProductEntity> optSearch(GoodsListForm goodsListForm, Long adzoneId, Long materialId, Long itemId) throws ApiException {
        TbkDgOptimusMaterialRequest request = new TbkDgOptimusMaterialRequest();
        request.setPageNo(goodsListForm.getPageNum().longValue());
        request.setPageSize(goodsListForm.getPageSize().longValue());
        request.setAdzoneId(adzoneId);
        request.setMaterialId(materialId);
        request.setItemId(itemId);
        request.setDeviceType("IMEI");
        request.setDeviceEncrypt("MD5");
        request.setDeviceValue(MD5.md5(goodsListForm.getUserId().toString(),""));
        TbkDgOptimusMaterialResponse response = myTaobaoClient.execute(request);
        List<SmProductEntity> smProductEntities = response.getResultList().stream().map(o -> convertGoodsList(o)).collect(Collectors.toList());
        Integer totalCount =  goodsListForm.getPageSize() <= response.getResultList().size() ? goodsListForm.getPageNum() * goodsListForm.getPageSize() + 1 : (goodsListForm.getPageNum() - 1) * goodsListForm.getPageSize() + response.getResultList().size();
        return packageToPageBean(
                smProductEntities,
                totalCount,
                goodsListForm.getPageNum(),
                goodsListForm.getPageSize());
    }


    private PageBean<SmProductEntity> packageToPageBean(List<SmProductEntity> list,Integer total,Integer pageNum,Integer pageSize){
        list = CollUtil.removeNull(list);
        PageBean<SmProductEntity> pageBean = new PageBean<>(list);
        pageBean.setTotal(total);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    private SmProductEntity convertGoodsList(TbkDgMaterialOptionalResponse.MapData o) {
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.TB);
        smProductEntity.setGoodsId(o.getItemId().toString());
        smProductEntity.setGoodsThumbnailUrl(o.getPictUrl());
        smProductEntity.setName(o.getTitle());
        smProductEntity.setOriginalPrice(o.getZkFinalPrice() != null ? Double.valueOf(Double.valueOf(o.getZkFinalPrice()) * 100).intValue() : 0);
        smProductEntity.setCouponPrice(o.getCouponAmount() != null ? Double.valueOf(Double.valueOf(o.getCouponAmount()) * 100).intValue() : 0);
        smProductEntity.setCouponStartFee(o.getCouponStartFee() == null ? null : Double.valueOf(Double.valueOf(o.getCouponStartFee()) * 100).intValue());
        smProductEntity.setMallName(o.getNick());
        smProductEntity.setSalesTip(o.getVolume().toString());
        smProductEntity.setPromotionRate(0);
//        smProductEntity.setPromotionRate(Integer.valueOf(o.getCommissionRate()));
        smProductEntity.setHasCoupon(o.getCouponAmount() != null && Double.valueOf(o.getCouponAmount()) > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.TB).calcProfit(smProductEntity).intValue());
        smProductEntity.setOptId(o.getCategoryId().toString());
        smProductEntity.setTbBuyUrl(StrUtil.isNotBlank(o.getCouponShareUrl()) ? "https:" + o.getCouponShareUrl() : o.getItemUrl());
        smProductEntity.setGoodsGalleryUrls(o.getSmallImages() == null ? o.getPictUrl() : o.getSmallImages().stream().collect(Collectors.joining(",")));
//        if(smProductEntity.getName().contains("口罩") || smProductEntity.getName().contains("医") || smProductEntity.getName().contains("药")){
//            return null;
//        }
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }
    private SmProductEntity convertGoodsList(TbkDgOptimusMaterialResponse.MapData o) {
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.TB);
        smProductEntity.setGoodsId(o.getItemId().toString());
        smProductEntity.setGoodsThumbnailUrl(o.getPictUrl());
        smProductEntity.setName(o.getTitle());
        smProductEntity.setOriginalPrice(o.getZkFinalPrice() != null ? Double.valueOf(Double.valueOf(o.getZkFinalPrice()) * 100).intValue() : 0);
        smProductEntity.setCouponPrice(o.getCouponAmount() != null ? Double.valueOf(Double.valueOf(o.getCouponAmount()) * 100).intValue() : 0);
        smProductEntity.setCouponStartFee(o.getCouponStartFee() == null ? null : Double.valueOf(Double.valueOf(o.getCouponStartFee()) * 100).intValue());
        smProductEntity.setMallName(o.getNick());
        smProductEntity.setSalesTip(o.getVolume().toString());
        smProductEntity.setPromotionRate(0);
//        smProductEntity.setPromotionRate(Double.valueOf(Double.valueOf(o.getCommissionRate()) * 100).intValue());
        smProductEntity.setHasCoupon(o.getCouponAmount() != null && o.getCouponAmount() > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.TB).calcProfit(smProductEntity).intValue());
        smProductEntity.setOptId(o.getCategoryId().toString());
        smProductEntity.setTbBuyUrl(StrUtil.isNotBlank(o.getCouponShareUrl()) ? "https:" + o.getCouponShareUrl() : o.getClickUrl());
        smProductEntity.setGoodsGalleryUrls(o.getSmallImages() == null ? o.getPictUrl() : o.getSmallImages().stream().collect(Collectors.joining(",")));
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
        TbkItemInfoGetRequest request = new TbkItemInfoGetRequest();
        request.setNumIids(goodsIds.stream().collect(Collectors.joining(",")));
        TbkItemInfoGetResponse rsp = myTaobaoClient.execute(request);
        List<TbkDgMaterialOptionalResponse.MapData> mapData = rsp.getResults().stream().map(o -> {
            try {
                return convertDetailByName(o.getNumIid().toString(),o.getTitle(),userId);
            } catch (ApiException e) {
                log.info("找不到淘宝商品优惠信息：{}",o.getNumIid());
            }
            return null;
        }).collect(Collectors.toList());
        CollUtil.removeNull(mapData);
        return mapData.stream()
                .map(o -> convertGoodsList(o))
                .collect(Collectors.toList());
    }

    /**
     * 获取单个商品详情
     * 内容更加丰富
     * @param goodsId
     * @return
     * @throws Exception
     */
    public SmProductEntity detail(Integer userId,String goodsId,String pid) throws Exception {
        TbkItemInfoGetRequest request = new TbkItemInfoGetRequest();
        request.setNumIids(goodsId);
        TbkItemInfoGetResponse rsp = myTaobaoClient.execute(request);
        if(rsp.getResults() == null || rsp.getResults().isEmpty())
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS,"找不到淘宝商品："+goodsId);
        TbkDgMaterialOptionalResponse.MapData mapData = convertDetailByName(goodsId,rsp.getResults().get(0).getTitle(),userId);
        if(mapData == null)
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS,"找不到淘宝商品优惠信息："+goodsId);
        return convertGoodsList(mapData);
    }

    private TbkDgMaterialOptionalResponse.MapData convertDetailByName(String goodsId,String goodsName,Integer userId) throws ApiException {
        TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
        request.setPageSize(100L);
        request.setAdzoneId(110169250471L);
        request.setMaterialId(6707L);
        request.setQ(goodsName);
        if(userId != null){
            request.setDeviceType("IMEI");
            request.setDeviceEncrypt("MD5");
            request.setDeviceValue(MD5.md5(userId.toString(),""));
        }
        TbkDgMaterialOptionalResponse response = myTaobaoClient.execute(request);
        List<TbkDgMaterialOptionalResponse.MapData> mapData = response.getResultList().stream()
                 .filter(o -> o.getItemId().toString().equals(goodsId))
                 .collect(Collectors.toList());
        if(mapData == null || mapData.isEmpty())
            return null;
        return mapData.get(0);
    }

    /**
     * 获取商品简略信息
     * @param goodsId
     * @return
     * @throws Exception
     */
    public SmProductSimpleVo basicDetail(String goodsId) throws Exception {
        TbkItemInfoGetRequest request = new TbkItemInfoGetRequest();
        request.setNumIids(goodsId);
        TbkItemInfoGetResponse rsp = myTaobaoClient.execute(request);
        SmProductSimpleVo smProductSimpleVo = new SmProductSimpleVo();
        smProductSimpleVo.setGoodsId(rsp.getResults().get(0).getNumIid().toString());
        smProductSimpleVo.setGoodsThumbnailUrl(rsp.getResults().get(0).getPictUrl());
        smProductSimpleVo.setName(rsp.getResults().get(0).getTitle());
        smProductSimpleVo.setOriginalPrice(Double.valueOf(Double.valueOf(rsp.getResults().get(0).getZkFinalPrice())  * 100).intValue());
        smProductSimpleVo.setType(PlatformTypeConstant.TB);
        return smProductSimpleVo;
    }

    /**
     * 获取商品购买信息
     * @param customParams
     * @param pId
     * @param goodsId
     * @return
     * @throws Exception
     */
    public ShareLinkBo getShareLink(String customParams, String pId, String goodsId, String tbBuyUrl) throws Exception {
        ShareLinkBo shareLinkBo = new ShareLinkBo();
        TbkTpwdCreateRequest request = new TbkTpwdCreateRequest();
        request.setText("粉饰生活-优惠券");
        request.setUrl(tbBuyUrl);
        request.setExt(customParams);
        TbkTpwdCreateResponse response = myTaobaoClient.execute(request);
        shareLinkBo.setTbOrder(response.getData().getModel());
        return shareLinkBo;
    }



}
