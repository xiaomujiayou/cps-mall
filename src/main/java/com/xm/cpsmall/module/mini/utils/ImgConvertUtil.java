package com.xm.cpsmall.module.mini.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import lombok.Data;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品图片转换工具
 */
public class ImgConvertUtil {

    private static File ttf;
    private static File templete;

    static {
        try {
            templete = ResourceUtils.getFile("classpath:img/we_app_share_templete.png");
            ttf = ResourceUtils.getFile("classpath:ttf/PingFangSCRegular.ttf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Image convertShareImg(SmProductEntityEx smProductEntityEx) throws IOException {
        //绘制商品图
        Image templeteImg = ImgUtil.read(templete);
        Image goodsImg = ImageIO.read(new URL(smProductEntityEx.getGoodsThumbnailUrl()));
        goodsImg = ImgUtil.scale(goodsImg,100,100);
        Image targrt = ImgUtil.pressImage(templeteImg,goodsImg,-55 ,-30,1);
        //绘制标题
        List<TextInfo> textInfos = calcTitle(smProductEntityEx.getName());
        textInfos.addAll(
                calcPrice(
                        smProductEntityEx.getOriginalPrice(),
                        smProductEntityEx.getOriginalPrice() - smProductEntityEx.getCouponPrice() - smProductEntityEx.getBuyPrice(),
                        smProductEntityEx.getCouponPrice(),
                        smProductEntityEx.getBuyPrice(),
                        "销量："+smProductEntityEx.getSalesTip()));
        for (TextInfo textInfo : textInfos) {
            targrt = ImgUtil.pressText(targrt,textInfo.getText(),textInfo.getFontColor(),textInfo.getFont(),textInfo.getX(),textInfo.getY(),1);
        }
        return goodsImg;
//        ImgUtil.write(targrt,new File("C:\\Users\\xiaomu\\Desktop\\新建文件夹 (7)\\aaa.png"));
    }
    public static void main(String[] args) throws IOException {

    }

    /**
     * 计算价格
     */
    private static List<TextInfo> calcPrice(Integer originalPrice, Integer finalPrice, Integer coupon, Integer red, String sales){
        List<TextInfo> textInfos = new ArrayList<>();
        //最终价格
        Float finalPriceFontSize = 24f;
        TextInfo finalPriceInfoFlag = new TextInfo("￥",5,-15,ImgUtil.createFont(ttf).deriveFont(18f).deriveFont(Font.BOLD),ImgUtil.getColor("#C53336"),true);
        TextInfo finalPriceInfo = new TextInfo((finalPrice / 100)+"",25,-20,ImgUtil.createFont(ttf).deriveFont(finalPriceFontSize).deriveFont(Font.BOLD),ImgUtil.getColor("#C53336"),true);
        textInfos.add(finalPriceInfo);
        textInfos.add(finalPriceInfoFlag);
        //原始价格
        Float originalPriceFontSize = 14f;
        TextInfo originalPriceInfoFlag = new TextInfo("￥",7,5,ImgUtil.createFont(ttf).deriveFont(14f).deriveFont(Font.PLAIN),ImgUtil.getColor("#A3A3A3"),true);
        String originalPriceStr = (finalPrice / 100) + "";
        TextInfo originalPriceInfo = new TextInfo(originalPriceStr,22,5,ImgUtil.createFont(ttf).deriveFont(originalPriceFontSize).deriveFont(Font.PLAIN),ImgUtil.getColor("#A3A3A3"),true);
        textInfos.add(originalPriceInfo);
        textInfos.add(originalPriceInfoFlag);
        //横线
        StringBuffer line = new StringBuffer("__");
        for (int i = 0; i < originalPriceStr.length(); i++) {
            line.append("_");
        }
        TextInfo lineInfoFlag = new TextInfo(line.toString(),12,-1,ImgUtil.createFont(ttf).deriveFont(14f).deriveFont(Font.PLAIN),ImgUtil.getColor("#A3A3A3"),true);
        textInfos.add(lineInfoFlag);

        //销量
        Float salesFontSize = 10f;
        TextInfo salesInfo = new TextInfo(sales,5,-40,ImgUtil.createFont(ttf).deriveFont(salesFontSize).deriveFont(Font.PLAIN),ImgUtil.getColor("#A3A3A3"),true);
        textInfos.add(salesInfo);

        //优惠券返现信息
        //最终价格
        StringBuffer couponStr = new StringBuffer();
        if(coupon != null && coupon > 0)
            couponStr.append(NumberUtil.toStr(NumberUtil.div((double)coupon,100d))+"元券");
        if(coupon != null && coupon > 0 && red != null && red > 0){
            couponStr.append("+");
            couponStr.append(NumberUtil.toStr(NumberUtil.div((double)red,100d))+"元返现");
        }else if((coupon == null || coupon <= 0) && red != null && red >0){
            couponStr.append(NumberUtil.toStr(NumberUtil.div((double)red,100d))+"元返现");
        }else {
            couponStr.append("查看详情");
        }

        Float couponFontSize = 18f;
        TextInfo couponInfo = new TextInfo(couponStr.toString(),0,55,ImgUtil.createFont(ttf).deriveFont(couponFontSize).deriveFont(Font.BOLD),ImgUtil.getColor("#FFFFFF"),false);
        textInfos.add(couponInfo);
        return textInfos;
    }

    /**
     * 计算标题
     * @param title
     * @return
     */
    private static List<TextInfo> calcTitle(String title){
        List<TextInfo> list = new ArrayList<>();
        String topTitle = null;
        String bottomTitle = null;
        StringBuffer sb = new StringBuffer();
        Float fontSize = 14f;
        for (int i = 0; i < title.length(); i++) {
            String sub = title.substring(i,i+1);
            sb.append(sub);
            if(topTitle == null && calcFontWidth(sb.toString()) * fontSize >= 100){
                topTitle = sb.substring(0,sb.length()-1);
                sb = new StringBuffer(sub);
            }
            if(topTitle != null && bottomTitle == null && calcFontWidth(sb.toString()) * fontSize > 100 - 14 ){
                bottomTitle = sb.substring(0,sb.length() - 1) + "...";
                break;
            }
        }
        if(topTitle == null) {
            topTitle = sb.toString();
        }else if(topTitle != null && bottomTitle == null) {
            bottomTitle = sb.toString();
        }
        if(StrUtil.isNotBlank(topTitle)){
            TextInfo topTitleInfo = new TextInfo(topTitle,5,-80,ImgUtil.createFont(ttf).deriveFont(fontSize).deriveFont(Font.PLAIN),ImgUtil.getColor("#333333"),true);
            list.add(topTitleInfo);
        }
        if(StrUtil.isNotBlank(bottomTitle)){
            TextInfo bottomTitleInfo = new TextInfo(bottomTitle,5,-60,ImgUtil.createFont(ttf).deriveFont(fontSize).deriveFont(Font.PLAIN),ImgUtil.getColor("#333333"),true);
            list.add(bottomTitleInfo);
        }
        //计算上标题
        return list;
    }



    private static Integer calcFontWidth(String text){
        int length = text.length();
        for(int i=0;i<text.length();i++){
            String s = String.valueOf(text.charAt(i));
            if(s.getBytes().length>1){	//中文字符
                length++;
            }
        }
        length = length%2 == 0?length/2:length/2+1;  //中文和英文字符的转换
        if(text.contains("..."))
            length = length - 1;
        return length;
    }

    @Data
    static class TextInfo{

        public TextInfo() {}

        public TextInfo(String text,Integer x,Integer y,Font font, Color fontColor,Boolean isLeft) {
            this.height = font.getSize();
            this.width = calcFontWidth(text) * font.getSize();
            this.x = x;
            this.y = y;
            if(isLeft){
                this.x = this.x + this.width / 2;
                this.y = this.y + this.height / 2;
            }
            this.font = font;
            this.fontColor = fontColor;
            this.text = text;
        }
        private Integer x;
        private Integer y;
        private Integer width;
        private Integer height;
        private Font font;
        private Color fontColor;
        private String text;
    }


}
