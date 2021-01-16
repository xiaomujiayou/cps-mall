package com.xm.cpsmall.module.mall.controller;

import com.xm.cpsmall.annotation.PlatformType;
import com.xm.cpsmall.module.mall.serialize.entity.SmBannerEntity;
import com.xm.cpsmall.module.mall.serialize.form.BannerForm;
import com.xm.cpsmall.module.mall.service.api.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-mall/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 获取首页banner列表
     */
    @GetMapping
    public List<SmBannerEntity> banner(@PlatformType BannerForm bannerForm) throws Exception {
        return bannerService.banner(bannerForm);
    }

    @GetMapping("/option")
    public List<SmBannerEntity> optBanner(@PlatformType BannerForm bannerForm) throws Exception {
        return bannerService.option(bannerForm);
    }
}
