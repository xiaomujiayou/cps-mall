package com.xm.cpsmall.module.mall.service.api;

import com.xm.cpsmall.module.mall.serialize.entity.SmBannerEntity;
import com.xm.cpsmall.module.mall.serialize.form.BannerForm;

import java.util.List;

public interface BannerService {

    /**
     * 首页banner
     * @param bannerForm
     * @return
     * @throws Exception
     */
    List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception;

    /**
     * 首页滑动列表
     * @param bannerForm
     * @return
     * @throws Exception
     */
    List<SmBannerEntity> option(BannerForm bannerForm) throws Exception;
}
