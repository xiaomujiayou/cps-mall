package com.xm.cpsmall.module.mall.service.api.impl.tb;

import com.alibaba.fastjson.JSON;
import com.xm.cpsmall.component.TbSdkComponent;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.BaseGoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailsForm;
import com.xm.cpsmall.module.mall.serialize.form.SaleInfoForm;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductSimpleVo;
import com.xm.cpsmall.module.mall.service.ProductTestService;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.module.mall.service.api.GoodsService;
import com.xm.cpsmall.module.user.serialize.bo.OrderCustomParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbGoodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbSdkComponent tbSdkComponent;
    @Autowired
    private ProfitService profitService;
    @Autowired
    private ProductTestService productTestService;
    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception {
        return profitService.calcProfit(
                tbSdkComponent.detail(
                        goodsDetailForm.getUserId(),
                        goodsDetailForm.getGoodsId(),
                        goodsDetailForm.getPid()),
                goodsDetailForm.getUserId(),
                goodsDetailForm.getShareUserId() != null,
                goodsDetailForm.getShareUserId());
    }

    @Override
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception {
        return tbSdkComponent.details(
                goodsDetailsForm.getUserId(),
                goodsDetailsForm.getGoodsIds());
    }

    @Override
    public SmProductSimpleVo basicDetail(BaseGoodsDetailForm baseGoodsDetailForm) throws Exception {
        return tbSdkComponent.basicDetail(baseGoodsDetailForm.getGoodsId());
    }

    @Override
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception {
        OrderCustomParameters parameters = new OrderCustomParameters();
        parameters.setUserId(saleInfoForm.getUserId());
        parameters.setShareUserId(saleInfoForm.getShareUserId());
        parameters.setFromApp(saleInfoForm.getAppType());
        return tbSdkComponent.getShareLink(
                JSON.toJSONString(parameters),
                saleInfoForm.getPid(),
                saleInfoForm.getGoodsId(),
                saleInfoForm.getTbBuyUrl());
    }
}
