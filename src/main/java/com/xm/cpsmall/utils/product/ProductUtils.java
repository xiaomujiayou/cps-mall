package com.xm.cpsmall.utils.product;

import com.xm.cpsmall.utils.RegeUtils;
import lombok.Data;

import java.util.regex.Pattern;

public class ProductUtils {

    private static final Pattern ALIBABA = Pattern.compile("(?<=1688.com/offer/)[0-9]+(?=.html)");

    /**
     * 从url解析商品id和种类
     * @param url
     * @return
     */
    public static OutProduct parse(String url){
        if(url == null)
            throw new NullPointerException();
        OutProduct outProduct = new OutProduct();
        if(url.contains("1688")){
            outProduct.setProductType(1);
            outProduct.setOutProductId(RegeUtils.matchFrist(url,ALIBABA));
            return outProduct;
        }
        return null;
    }

    public static String generateUrl(String outProductId,Integer type){
        switch (type){
            case 1:{
                return String.format("https://detail.1688.com/offer/%s.html?sk=consign",outProductId);
            }
        }
        throw new RuntimeException(String.format("找不到指定类型 outProductId:%s type:%s",outProductId,type));
    }

    @Data
    public static class OutProduct{
        private String outProductId;
        private Integer productType;
    }
}
