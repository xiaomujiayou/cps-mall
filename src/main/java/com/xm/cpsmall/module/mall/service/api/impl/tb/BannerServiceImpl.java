package com.xm.cpsmall.module.mall.service.api.impl.tb;

import com.xm.cpsmall.module.mall.constant.AppTypeConstant;
import com.xm.cpsmall.module.mall.constant.BannerTypeEnum;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.mapper.SmBannerMapper;
import com.xm.cpsmall.module.mall.serialize.entity.SmBannerEntity;
import com.xm.cpsmall.module.mall.serialize.form.BannerForm;
import com.xm.cpsmall.module.mall.service.api.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@Service("tbBannerService")
public class BannerServiceImpl implements BannerService {

    @Autowired
    private SmBannerMapper smBannerMapper;

    @Override
    public List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception {
        return null;
    }

    @Override
    public List<SmBannerEntity> option(BannerForm bannerForm) throws Exception {
        OrderByHelper.orderBy("sort desc");
        SmBannerEntity criteria = new SmBannerEntity();
        criteria.setAppType(AppTypeConstant.WE_APP);
        criteria.setPlatformType(PlatformTypeConstant.TB);
        criteria.setDisable(1);
        criteria.setType(BannerTypeEnum.HOME_SLIDE_MUEN.getType());
        return smBannerMapper.select(criteria);
    }
}
