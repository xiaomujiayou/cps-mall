package com.xm.cpsmall.module.mall.serialize.bo;

import lombok.Data;

@Data
public class ShareLinkBo {

    //小程序图片
    private String weIconUrl;
    private String weBannerUrl;
    //小程序描述
    private String weDesc;
    //小程序来原名
    private String weSourceDisplayName;
    //小程序path
    private String wePagePath;
    private String weUserName;
    private String weTitle;
    private String weAppId;

    //短链接
    private String shotUrl;
    //长链接
    private String longUrl;

    //淘口令
    private String tbOrder;

}
