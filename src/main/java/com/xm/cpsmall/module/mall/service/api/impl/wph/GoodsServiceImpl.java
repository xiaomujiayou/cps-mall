package com.xm.cpsmall.module.mall.service.api.impl.wph;

import com.xm.cpsmall.component.WphSdkComponent;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailsForm;
import com.xm.cpsmall.module.mall.serialize.form.SaleInfoForm;
import com.xm.cpsmall.module.mall.service.ProductTestService;
import com.xm.cpsmall.module.mall.service.ProfitService;
import com.xm.cpsmall.module.mall.service.api.impl.abs.GoodsServiceAbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("wphGoodsService")
public class GoodsServiceImpl extends GoodsServiceAbs {

    @Autowired
    private WphSdkComponent wphSdkComponent;
    @Autowired
    private ProfitService profitService;
    @Autowired
    private ProductTestService productTestService;
    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception {
        SmProductEntity smProductEntity = wphSdkComponent.detail(goodsDetailForm.getGoodsId());
        return profitService.calcProfit(
                smProductEntity,
                goodsDetailForm.getUserId(),
                goodsDetailForm.getShareUserId() != null,
                goodsDetailForm.getShareUserId());
    }

    @Override
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception {
        return wphSdkComponent.details(goodsDetailsForm.getUserId(),goodsDetailsForm.getGoodsIds());
    }

    @Override
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception {
        return wphSdkComponent.getShareLink(saleInfoForm.getPid(),saleInfoForm.getGoodsId());
    }
}
