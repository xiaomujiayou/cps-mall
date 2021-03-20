package com.xm.cpsmall.module.mall.service.api.impl.abs;

import com.xm.cpsmall.module.mall.constant.AppTypeConstant;
import com.xm.cpsmall.module.mall.constant.BannerTypeEnum;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.mapper.SmBannerMapper;
import com.xm.cpsmall.module.mall.serialize.entity.SmBannerEntity;
import com.xm.cpsmall.module.mall.serialize.form.BannerForm;
import com.xm.cpsmall.module.mall.service.api.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

public abstract class BannerServiceAbs implements BannerService {

    @Autowired
    private SmBannerMapper smBannerMapper;

    @Override
    public List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception {
        OrderByHelper.orderBy("sort desc");
        SmBannerEntity criteria = new SmBannerEntity();
        criteria.setType(BannerTypeEnum.HOME.getType());
        criteria.setDisable(1);
        criteria.setAppType(AppTypeConstant.WE_APP);
//        criteria.setPlatformType(PlatformTypeConstant.PDD);
        List<SmBannerEntity> list = smBannerMapper.select(criteria);
        list.stream().forEach(o -> {
            o.setUrl(String.format("/pages/theme/index?themeId=%s&platformType=%s&themeName=%s&themeImg=%s&keyWords=%s",o.getId(), PlatformTypeConstant.PDD,o.getName(),o.getImg(),o.getKeyWords()));
        });
        return list;
    }

    @Override
    public List<SmBannerEntity> option(BannerForm bannerForm) throws Exception {
        return null;
    }
}
