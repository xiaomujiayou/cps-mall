package com.xm.cpsmall.module.mall.serialize.bo;

import lombok.Data;

@Data
public class PddAuthBo {

    /**
     * mobileUrl : https://mobile.yangkeduo.com/app.html?use_reload=1&launch_url=duo_coupon_landing.html%3F__page%3Dauth%26pid%3D8944230_125518088%26customParameters%3D%257B%2522uid%2522%253A%2522111%2522%252C%2522sid%2522%253A%2522222%2522%257D%26cpsSign%3DCC_201120_8944230_125518088_62aa7679f1e94478b91eaeda8a754a80%26duoduo_type%3D2&campaign=ddjb&cid=launch_
     * url : https://mobile.yangkeduo.com/duo_coupon_landing.html?__page=auth&pid=8944230_125518088&customParameters=%7B%22uid%22%3A%22111%22%2C%22sid%22%3A%22222%22%7D&cpsSign=CC_201120_8944230_125518088_62aa7679f1e94478b91eaeda8a754a80&duoduo_type=2
     * weAppInfo : {"appId":"wx32540bd863b27570","desc":"拼多多，多实惠，多乐趣。","pagePath":"/pages/web/web?specialUrl=1&src=https%3A%2F%2Fmobile.yangkeduo.com%2Fduo_coupon_landing.html%3F__page%3Dauth%26pid%3D8944230_125518088%26customParameters%3D%257B%2522uid%2522%253A%2522111%2522%252C%2522sid%2522%253A%2522222%2522%257D%26cpsSign%3DCC_201120_8944230_125518088_62aa7679f1e94478b91eaeda8a754a80%26duoduo_type%3D2","sourceDisplayName":"拼多多","title":"认证页","userName":"gh_0e7477744313@app","weAppIconUrl":"http://xcxcdn.yangkeduo.com/pdd_logo.png"}
     */
    private String mobileUrl;
    private String url;
    private WeAppInfoBean weAppInfo;

    @Data
    public static class WeAppInfoBean {

        /**
         * appId : wx32540bd863b27570
         * desc : 拼多多，多实惠，多乐趣。
         * pagePath : /pages/web/web?specialUrl=1&src=https%3A%2F%2Fmobile.yangkeduo.com%2Fduo_coupon_landing.html%3F__page%3Dauth%26pid%3D8944230_125518088%26customParameters%3D%257B%2522uid%2522%253A%2522111%2522%252C%2522sid%2522%253A%2522222%2522%257D%26cpsSign%3DCC_201120_8944230_125518088_62aa7679f1e94478b91eaeda8a754a80%26duoduo_type%3D2
         * sourceDisplayName : 拼多多
         * title : 认证页
         * userName : gh_0e7477744313@app
         * weAppIconUrl : http://xcxcdn.yangkeduo.com/pdd_logo.png
         */
        private String appId;
        private String desc;
        private String pagePath;
        private String sourceDisplayName;
        private String title;
        private String userName;
        private String weAppIconUrl;
    }
}
