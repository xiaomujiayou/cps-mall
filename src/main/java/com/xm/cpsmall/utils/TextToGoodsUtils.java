package com.xm.cpsmall.utils;

import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductSimpleVo;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductVo;
import lombok.Data;

import java.util.regex.Pattern;

public class TextToGoodsUtils {
    public static Pattern PDD_URL_PATTERN = Pattern.compile("(?<=(yangkeduo)\\S{1,255}(goods_id=))[0-9]+");
    public static Pattern MGJ_URL_PATTERN = Pattern.compile("(?<=(mogu.com)\\S{1,255}(itemId=))[0-9a-zA-Z]+");
    public static Pattern WPH_URL_PATTERN = Pattern.compile("(?<=(-))[0-9]{19}(?=.html)");

    /**
     * 解析剪辑版内容
     * @param text
     */
    public static GoodsSpec parse(String text){
        GoodsSpec goodsSpec = new GoodsSpec();
        //链接识别
        String goodsId = RegeUtils.matchFrist(text,PDD_URL_PATTERN);
        if(goodsId!=null){
            goodsSpec.setGoodsId(goodsId);
            goodsSpec.setPlatformType(PlatformTypeConstant.PDD);
            goodsSpec.setParseType(1);
            return goodsSpec;
        }
        goodsId = RegeUtils.matchFrist(text,MGJ_URL_PATTERN);
        if(goodsId!=null){
            goodsSpec.setGoodsId(goodsId);
            goodsSpec.setPlatformType(PlatformTypeConstant.MGJ);
            goodsSpec.setParseType(1);
            return goodsSpec;
        }
        goodsId = RegeUtils.matchFrist(text,WPH_URL_PATTERN);
        if(goodsId!=null){
            goodsSpec.setGoodsId(goodsId);
            goodsSpec.setPlatformType(PlatformTypeConstant.WPH);
            goodsSpec.setParseType(1);
            return goodsSpec;
        }
        //标题识别
        if(isGoodsName(text)){
            goodsSpec.setParseType(3);
            return goodsSpec;
        }
        goodsSpec.setParseType(0);
        return goodsSpec;
    }

    private static boolean isGoodsName(String text){
        if(text.length() > 15 && text.length() < 40)
            return true;
        return false;
    }

    @Data
    public static class GoodsSpec{
        private Integer platformType;
        private String goodsId;
        //解析结果类型:0:解析失败,1:解析为精确商品,2:商品简要信息,3:解析为商品名称查询,4:该商品没有优惠
        private Integer parseType;
        private SmProductVo goodsInfo;
        private SmProductSimpleVo simpleInfo;
    }
}
