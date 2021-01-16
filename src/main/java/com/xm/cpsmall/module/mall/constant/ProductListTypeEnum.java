package com.xm.cpsmall.module.mall.constant;

public enum ProductListTypeEnum {
    BEST_LIST(1,"bestList"),
    OPTION_LIST(2,"optionList"),
    CUSTOM_LIST(3,"customList"),
    SIMILAR_LIST(4,"similarList"),
    LIKE_LIST(5,"likeList"),
    KEYWORD_LIST(6,"keywordList"),
    HOT_LIST(7,"hotList"),
    THEME_LIST(8,"themeList"),
    RECOMMENT_LIST(9,"recommendList"),
    ;

    private Integer key;
    private String name;

    public Integer getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    ProductListTypeEnum(Integer key, String name) {
        this.key = key;
        this.name = name;
    }
    
    public static void main(String[] args){
        ProductListTypeEnum[] arr = ProductListTypeEnum.class.getEnumConstants();
        for (ProductListTypeEnum productListTypeEnum : arr) {

            System.out.println(productListTypeEnum.getName());
        }
    }
}
