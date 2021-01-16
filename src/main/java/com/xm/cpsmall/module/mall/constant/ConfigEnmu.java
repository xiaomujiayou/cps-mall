package com.xm.cpsmall.module.mall.constant;

public enum ConfigEnmu {
    PDD_PRODUCT_BEST_LIST_SORT("pdd_product_best_list_sort","拼多多推荐商品排序"),
    PDD_PRODUCT_BEST_LIST_SHOP_TYPE("pdd_product_best_list_shop_type","拼多多推荐商品列表店铺类型"),
    MAIN_OPTION_SORT_MAN("main_option_sort_man","主option排序(男性)"),
    MAIN_OPTION_SORT_WOMAN("main_option_sort_woman","主option排序(女性)"),
    MAIN_OPTION_SORT("main_option_sort","主option默认排序"),
    PRODUCT_SHARE_USER_RATE("product_share_user_rate","商品分享者默认佣金比例(千分比)"),
    PRODUCT_SHARE_BUY_RATE("product_share_buy_rate","购买分享商品的用户佣金比例(千分比)"),
    PRODUCT_BUY_RATE("product_buy_rate","商品购买默认佣金比例(千分比)"),
    PRODUCT_PROXY_RATE("product_proxy_rate","商品购买代理人佣金比例(千分比)"),
    PRODUCT_SEARCH_RECOMMEND("product_search_recommend","搜索页推荐搜索"),
    PROXY_LEVEL("proxy_level","代理层级"),
    PLATFORM_NAME("platform_name","平台名称"),
    PLATFORM_ID("platform_id","平台id"),
    PLATFORM_ALL_NAME("platform_all_name","所有平台名称"),
    PLATFORM_ALL_ID("platform_all_id","所有平台id"),
    PAGE_HELP_STATE("page_help_state","页面帮助提示(0:关闭,1:打开)"),
    PLATFORM_CHOOSE_BTN("platform_choose_btn","平台切换按钮(0:隐藏,1:显示)"),
    ;
    ConfigEnmu(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    private String desc;
    private String name;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
