package com.xm.cpsmall.module.mall.service.api;

import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.BaseGoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailsForm;
import com.xm.cpsmall.module.mall.serialize.form.SaleInfoForm;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductSimpleVo;

import java.util.List;

public interface GoodsService {

    /**
     * 获取商品详情
     * @param goodsDetailForm
     * @return
     * @throws Exception
     */
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception;

    /**
     * 批量获取商品详情
     * @param goodsDetailsForm
     * @return
     * @throws Exception
     */
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception;

    /**
     * 获取商品简略信息
     * @param baseGoodsDetailForm
     * @return
     * @throws Exception
     */
    public SmProductSimpleVo basicDetail(BaseGoodsDetailForm baseGoodsDetailForm) throws Exception;

    /**
     * 获取购买信息
     * @return
     */
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception;

}
