package com.xm.cpsmall.module.mall.service;

import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.form.GetProductSaleInfoForm;

/**
 * 商品测试接口
 * 上线前应移除
 */
public interface ProductTestService {

    /**
     * 模拟购买
     * @param userId
     * @param pid
     * @param productSaleInfoForm
     * @return
     * @throws Exception
     */
    public ShareLinkBo saleInfo(Integer userId, String pid, GetProductSaleInfoForm productSaleInfoForm) throws Exception;


}
