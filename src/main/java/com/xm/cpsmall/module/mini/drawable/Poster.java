package com.xm.cpsmall.module.mini.drawable;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class Poster {

    /**
     * 画布宽度
     */
    @NotNull(message = "画布宽度不能为空")
    private Integer width;

    /**
     * 画布高度
     */
    @NotNull(message = "画布高度不能为空")
    private Integer height;

    /**
     * 画布背景颜色
     */
    private String backgroundColor = null;

    /**
     * 图片格式,支持 png 和 jpg
     */
    private String format = "png";

    /**
     * 文本列表
     */
    private ArrayList<Text> texts;

    /**
     * 图片列表
     */
    private ArrayList<Image> images;

    /**
     * 矩形列表
     */
    private ArrayList<Block> blocks;

    /**
     * 线列表
     */
    private ArrayList<Line> lines;


    private void push2map(Map<Integer, ArrayList<Drawable>> indexMap, Drawable drawable) {
        ArrayList<Drawable> drawables = indexMap.get(drawable.getZIndex());
        drawables = drawables == null ? new ArrayList<>() : drawables;
        drawables.add(drawable);
        indexMap.put(drawable.getZIndex(), drawables);
    }

    /**
     * 绘制图片
     *
     * @return File
     * @throws IOException
     */
    public BufferedImage draw() throws IOException {
        long s = System.currentTimeMillis();
        // 初始化图片
        BufferedImage image = new BufferedImage(width, height, format.equals("png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_3BYTE_BGR);
        System.out.println("初始化图片:"+(System.currentTimeMillis() - s));
        // create graphics
        Graphics2D gd = image.createGraphics();

        // 初始化画布层级 map
        Map<Integer, ArrayList<Drawable>> indexMap = new HashMap<>();
        ArrayList<Drawable> drawables;

        // 如果有背景，画个矩形做背景
        if (backgroundColor != null) {
            gd.setColor(ColorTools.String2Color(backgroundColor));
            gd.fillRect(0, 0, width, height);
        }

        if (this.blocks != null) {
            // 遍历 blocks
            for (Block block : this.blocks) {
                push2map(indexMap, block);
            }
        }

        if (this.lines != null) {
            // 遍历 lines
            for (Line line : this.lines) {
                push2map(indexMap, line);
            }
        }
        if (this.images != null) {
            // 遍历 images
            for (Image img : this.images) {
                push2map(indexMap, img);
            }
        }
        if (this.texts != null) {
            // 遍历 texts
            for (Text text : this.texts) {
                push2map(indexMap, text);
            }
        }
        // 按 index 顺序执行绘画过程
        for (Integer index : indexMap.keySet()) {
            drawables = indexMap.get(index);
            if (drawables != null) {
                for (Drawable drawable : drawables) {
                    drawable.draw(gd, width, height);
                    System.out.println(drawable.getClass().getName()+":"+(System.currentTimeMillis() - s));
                }
            }
        }
        gd.dispose();
        return image;
    }

    /**
     * 绘制图片并且上传到 sm.ms
     *
     * @return
     * @throws IOException
     */
//    public Result drawAndUpload() throws IOException {
//        return SmmsUploader.push(this.draw()); // 上传图片到 smms
//    }

    /**
     * 获取key
     *
     * @return String
     */
//    public String key() {
//        return DigestUtils.md5DigestAsHex(this.toString().getBytes());
//    }
}
