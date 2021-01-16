package com.xm.cpsmall.component;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mogujie.openapi.request.MgjRequest;
import com.mogujie.openapi.response.MgjResponse;
import com.xm.cpsmall.comm.api.client.MyMogujieClient;
import com.xm.cpsmall.comm.api.config.MgjApiConfig;
import com.xm.cpsmall.comm.api.mgj.*;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.serialize.bo.ProductCriteriaBo;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductSimpleVo;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.utils.GoodsPriceUtil;
import com.xm.cpsmall.utils.mybatis.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MgjSdkComponent {

    @Autowired
    private MyMogujieClient myMogujieClient;
    @Autowired
    private MgjApiConfig mgjApiConfig;
    @Lazy
    @Autowired
    private MgjSdkComponent mgjSdkComponent;
    @Autowired
    private ProfitService profitService;

    public PageBean<SmProductEntity> getProductByCriteria(ProductCriteriaBo criteria) throws Exception {
        PromInfoQueryBean promInfoQueryBean = new PromInfoQueryBean();
        promInfoQueryBean.setKeyword(criteria.getKeyword());
        promInfoQueryBean.setPageNo(criteria.getPageNum());
        promInfoQueryBean.setPageSize(criteria.getPageSize());
        promInfoQueryBean.setSortType(criteria.getOrderBy(PlatformTypeConstant.MGJ) == null ? null : Integer.valueOf(criteria.getOrderBy(PlatformTypeConstant.MGJ).toString()));
        promInfoQueryBean.setHasCoupon(criteria.getHasCoupon());
        MgjResponse<String> res = myMogujieClient.execute(new XiaoDianCpsdataPromItemGetRequest(promInfoQueryBean));
        JSONObject json = JSON.parseObject(res.getResult().getData());
        JSONArray goodsArrJson = json.getJSONArray("items");

        return packageToPageBean(
                goodsArrJson.stream().map(o -> convertGoodsList(o)).collect(Collectors.toList()),
                json.getInteger("total"),
                json.getInteger("page"),
                json.getInteger("pageSize"));
    }

    private PageBean<SmProductEntity> packageToPageBean(List<SmProductEntity> list, Integer total, Integer pageNum, Integer pageSize){
        PageBean<SmProductEntity> pageBean = new PageBean<>(list);
        pageBean.setTotal(total);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    private SmProductEntity convertGoodsList(Object goodsObj){
        JSONObject goodsJson = (JSONObject) goodsObj;
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.MGJ);
        smProductEntity.setGoodsId(goodsJson.getString("itemId"));
        smProductEntity.setGoodsThumbnailUrl(goodsJson.getString("pictUrlForH5"));
        smProductEntity.setName(goodsJson.getString("title"));
        smProductEntity.setOriginalPrice((int)(goodsJson.getFloat("zkPrice") * 100));
        smProductEntity.setCouponPrice((int)(goodsJson.getFloat("couponAmount") * 100));
        smProductEntity.setMallName(goodsJson.getJSONObject("shopInfo").getString("name"));
        smProductEntity.setSalesTip(goodsJson.getString("biz30day"));
        smProductEntity.setMallCps(0);
        smProductEntity.setPromotionRate((int)(Float.valueOf(goodsJson.getString("commissionRate").replace("%",""))*100));
        smProductEntity.setHasCoupon(goodsJson.getInteger("couponLeftCount") > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.MGJ).calcProfit(smProductEntity).intValue());
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }


    public List<SmProductEntity> details(List<String> goodsIds) throws Exception {
        List<SmProductEntity> list = goodsIds.stream().map(o-> {
            try {
                return mgjSdkComponent.detail(o,null);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        return CollUtil.removeNull(list);
    }

//    @Cacheable(value = "share.goods.detail.mgj",key = "#goodsId")
    public SmProductEntity detail(String goodsId, String pid) throws Exception {
        XiaoDianCpsdataItemGetRequest request = new XiaoDianCpsdataItemGetRequest("https://shop.mogujie.com/detail/" + goodsId);
        MgjResponse<String> res = myMogujieClient.execute(request);
        return convertDetail(JSON.parseObject(res.getResult().getData()));
    }

    private SmProductEntity convertDetail(JSONObject goodsJson){
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.MGJ);
        smProductEntity.setGoodsId(goodsJson.getString("itemId"));
        smProductEntity.setGoodsThumbnailUrl(goodsJson.getString("pictUrl"));
        smProductEntity.setGoodsGalleryUrls(String.join(",",goodsJson.getString("pictUrl")));
        smProductEntity.setName(goodsJson.getString("title"));
        smProductEntity.setDes(goodsJson.getString("extendDesc"));
        smProductEntity.setOriginalPrice((int)(goodsJson.getFloat("zkPrice") * 100));
        smProductEntity.setCouponPrice((int)(goodsJson.getFloat("couponAmount") * 100));
        smProductEntity.setMallName(goodsJson.getJSONObject("shopInfo").getString("name"));
        smProductEntity.setSalesTip(goodsJson.getString("biz30day"));
        smProductEntity.setMallCps(0);
        smProductEntity.setPromotionRate((int)(Float.valueOf(goodsJson.getString("commissionRate").replace("%",""))*100));
        smProductEntity.setHasCoupon(goodsJson.getInteger("couponLeftCount") > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.MGJ).calcProfit(smProductEntity).intValue());
        smProductEntity.setServiceTags(String.join(",", goodsJson.getString("tag")));
        smProductEntity.setCouponId(goodsJson.getString("promid"));
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }

    public SmProductSimpleVo basicDetail(Long goodsId) throws Exception {
        return null;
    }

    public ShareLinkBo getShareLink(String customParams, String pId, String goodsId, String couponId) throws Exception {
        WxCodeParamBean wxCodeParamBean = new WxCodeParamBean();
        wxCodeParamBean.setUid(mgjApiConfig.getUid());
        wxCodeParamBean.setItemId(goodsId);
        wxCodeParamBean.setPromId(couponId);
        wxCodeParamBean.setGenWxcode(false);
        wxCodeParamBean.setGid(pId);
        MgjRequest<XiaoDianCpsdataWxcodeGetResponse> mgjRequest = new XiaoDianCpsdataWxcodeGetRequest(wxCodeParamBean);
        JSONObject shareBean = JSON.parseObject(myMogujieClient.execute(mgjRequest).getResult().getData());
        ShareLinkBo shareLinkBo = new ShareLinkBo();
//        shareLinkBo.setWePagePath(shareBean.getString("path") + "&feedback=" + customParams);
        shareLinkBo.setWePagePath(shareBean.getString("path"));
        shareLinkBo.setWeAppId(mgjApiConfig.getWeAppId());
        //生成短链接
        String url = "https://union.mogujie.com/jump?userid=" + mgjApiConfig.getUid() +
                "&itemid=" + goodsId +
                "&promid=" + couponId +
                "&feedback=" + Base64.encode(customParams);
        MgjRequest<XiaodianCpsdataUrlShortenResponse> shotReq = new XiaodianCpsdataUrlShortenRequest(url);
        String shotUrl = myMogujieClient.execute(shotReq).getResult().getData();
        shareLinkBo.setShotUrl(shotUrl);
        return shareLinkBo;
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
}
