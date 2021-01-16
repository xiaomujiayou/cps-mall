package com.xm.cpsmall.module.mall.service.api.impl.jd;

import com.xm.cpsmall.module.mall.serialize.entity.SmBannerEntity;
import com.xm.cpsmall.module.mall.serialize.form.BannerForm;
import com.xm.cpsmall.module.mall.service.api.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jdBannerService")
public class BannerServiceImpl implements BannerService {
    @Override
    public List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception {
        return null;
    }

    @Override
    public List<SmBannerEntity> option(BannerForm bannerForm) throws Exception {
        return null;
    }
}
