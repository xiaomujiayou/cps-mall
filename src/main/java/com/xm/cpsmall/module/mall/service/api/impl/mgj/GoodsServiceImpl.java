package com.xm.cpsmall.module.mall.service.api.impl.mgj;

import com.alibaba.fastjson.JSON;
import com.xm.cpsmall.component.MgjSdkComponent;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.BaseGoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailsForm;
import com.xm.cpsmall.module.mall.serialize.form.SaleInfoForm;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductSimpleVo;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.module.mall.service.api.GoodsService;
import com.xm.cpsmall.module.user.serialize.bo.OrderCustomParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mgjGoodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private MgjSdkComponent mgjSdkComponent;
    @Autowired
    private ProfitService profitService;

    @Override
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception {
        SmProductEntity smProductEntity = mgjSdkComponent.detail(
                goodsDetailForm.getGoodsId(),
                goodsDetailForm.getPid());
        return profitService.calcProfit(
                smProductEntity,goodsDetailForm.getUserId(),
                goodsDetailForm.getShareUserId() != null,
                goodsDetailForm.getShareUserId());
    }

    @Override
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception {
        return mgjSdkComponent.details(goodsDetailsForm.getGoodsIds());
    }

    @Override
    public SmProductSimpleVo basicDetail(BaseGoodsDetailForm baseGoodsDetailForm) throws Exception {
        return null;
    }

    @Override
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception {
        OrderCustomParameters parameters = new OrderCustomParameters();
        parameters.setUserId(saleInfoForm.getUserId());
        parameters.setShareUserId(saleInfoForm.getShareUserId());
        parameters.setFromApp(saleInfoForm.getAppType());
        return mgjSdkComponent.getShareLink(
                JSON.toJSONString(parameters),
                saleInfoForm.getPid(),
                saleInfoForm.getGoodsId(),
                saleInfoForm.getCouponId());
    }
}
