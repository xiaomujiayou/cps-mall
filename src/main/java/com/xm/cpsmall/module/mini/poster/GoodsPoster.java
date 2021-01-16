package com.xm.cpsmall.module.mini.poster;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.NumberUtil;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mini.drawable.Image;
import com.xm.cpsmall.module.mini.drawable.Line;
import com.xm.cpsmall.module.mini.drawable.Poster;
import com.xm.cpsmall.module.mini.drawable.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GoodsPoster extends Poster {
    public GoodsPoster(SmProductEntityEx smProductEntityEx) {
        this.setWidth(500);
        this.setHeight(400);
        ArrayList<Image> images = new ArrayList<>();
        //添加模板图
        Image templete = new Image();
        templete.setX(0);
        templete.setY(0);
        templete.setWidth(500);
        templete.setHeight(400);
        templete.setUrl("we_app_share_templete_large.png");
        images.add(templete);
        //添加商品图
        Image goodsImg = new Image();
        goodsImg.setX(40);
        goodsImg.setY(40);
        goodsImg.setWidth(200);
        goodsImg.setHeight(200);
        goodsImg.setUrl(smProductEntityEx.getGoodsThumbnailUrl());
        images.add(goodsImg);
        this.setImages(images);

        ArrayList<Text> texts = new ArrayList<>();
        //标题
        Text title = new Text();
        title.setX(260);
        title.setY(35);
        title.setText(smProductEntityEx.getName());
        title.setFontSize(28);
        title.setColor("#333333");
        title.setLineHeight(34);
        title.setLineNum(2);
        title.setWidth(200);
        title.setFont("wryh");
        texts.add(title);
        //销量
        Text sales = new Text();
        sales.setX(260);
        sales.setY(105);
        sales.setText("销量："+smProductEntityEx.getSalesTip());
        sales.setFontSize(24);
        sales.setColor("#A3A3A3");
        sales.setLineHeight(34);
        sales.setLineNum(1);
        sales.setFont("wryh");
        texts.add(sales);
        //价格标志
        Text finanlPriceFlag = new Text();
        finanlPriceFlag.setX(260);
        finanlPriceFlag.setY(165);
        finanlPriceFlag.setText("￥");
        finanlPriceFlag.setFontSize(24);
        finanlPriceFlag.setColor("#C53336");
        finanlPriceFlag.setLineHeight(40);
        finanlPriceFlag.setLineNum(1);
        finanlPriceFlag.setFont("wryh_bold");
        texts.add(finanlPriceFlag);
        //最终价格
        String finalPriceStr = NumberUtil.toStr((smProductEntityEx.getOriginalPrice() - smProductEntityEx.getCouponPrice() - smProductEntityEx.getBuyPrice())/100f);
        Text finanlPrice = new Text();
        finanlPrice.setX(285);
        finanlPrice.setY(159);
        finanlPrice.setText(finalPriceStr);
        finanlPrice.setFontSize(40);
        finanlPrice.setColor("#C53336");
        finanlPrice.setLineHeight(40);
        finanlPrice.setLineNum(1);
        finanlPrice.setFont("wryh_bold");
        texts.add(finanlPrice);
        //原始价格标志
        Text oriPriceFlag = new Text();
        oriPriceFlag.setX(260);
        oriPriceFlag.setY(202);
        oriPriceFlag.setText("￥");
        oriPriceFlag.setFontSize(24);
        oriPriceFlag.setColor("#a3a3a3");
        oriPriceFlag.setLineHeight(40);
        oriPriceFlag.setLineNum(1);
        oriPriceFlag.setFont("wryh");
        texts.add(oriPriceFlag);
        //原始价格
        String oriPriceStr = NumberUtil.toStr(smProductEntityEx.getOriginalPrice()/100f);
        Text oriPrice = new Text();
        oriPrice.setX(282);
        oriPrice.setY(201);
        oriPrice.setText(oriPriceStr);
        oriPrice.setFontSize(28);
        oriPrice.setColor("#a3a3a3");
        oriPrice.setLineHeight(40);
        oriPrice.setLineNum(1);
        oriPrice.setFont("wryh");
        texts.add(oriPrice);
        //按钮提示
        String btnStr = (smProductEntityEx.getCouponPrice() > 0 ? NumberUtil.toStr(smProductEntityEx.getCouponPrice() / 100f) + "元券" : "") + ((smProductEntityEx.getCouponPrice() > 0 && smProductEntityEx.getBuyPrice() > 0) ? " + " : "") + (smProductEntityEx.getBuyPrice() > 0 ? NumberUtil.toStr(smProductEntityEx.getBuyPrice() / 100f) + "元返现" : "");
        Text btn = new Text();
        btn.setX(40);
        btn.setY(281);
        btn.setWidth(420);
        btn.setText(btnStr);
        btn.setFontSize(40);
        btn.setColor("#ffffff");
        btn.setLineHeight(50);
        btn.setLineNum(1);
        btn.setTextAlign("center");
        btn.setFont("wryh_bold");
        texts.add(btn);
        this.setTexts(texts);

        ArrayList<Line> lines = new ArrayList<>();
        Line oriPriceLine = new Line();
        oriPriceLine.setStartX(270);
        oriPriceLine.setStartY(225);
        oriPriceLine.setEndX(NumberUtil.round( 270 + (14 * (oriPriceStr.contains(".")?(oriPriceStr.length()+1):(oriPriceStr.length()+1.5))),0).intValue());
        oriPriceLine.setEndY(225);
        oriPriceLine.setStartX(265);
        oriPriceLine.setColor("#a3a3a3");
        oriPriceLine.setIndex(9999);
        lines.add(oriPriceLine);
        this.setLines(lines);
    }

    public static void main(String[] args) throws IOException {
        SmProductEntityEx smProductEntityEx = new SmProductEntityEx();
        smProductEntityEx.setOriginalPrice(1230000);
        smProductEntityEx.setCouponPrice(550);
        smProductEntityEx.setBuyPrice(330);
        smProductEntityEx.setName("刚出炉的包子刚出炉的包子刚出炉的包子刚出炉的包子");
        smProductEntityEx.setGoodsThumbnailUrl("http://t00img.yangkeduo.com/goods/images/2019-11-20/a292f360-e7d1-4383-a449-76a7a5cf559e.jpg");
        smProductEntityEx.setSalesTip("10万+");
        ImgUtil.writePng(new GoodsPoster(smProductEntityEx).draw(),new FileOutputStream(new File("C:\\Users\\xiaomu\\Desktop\\新建文件夹 (7)\\333.png")));
    }
}
