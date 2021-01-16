package com.xm.cpsmall.module.mini.controller;

import cn.hutool.core.img.ImgUtil;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mini.poster.GoodsPoster;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RequestMapping("/api-mini/share")
@Controller
public class MiniShareController {

    @GetMapping(value = "/goods",produces = MediaType.IMAGE_PNG_VALUE)
    public void goods(SmProductEntityEx smProductEntityEx, HttpServletResponse res) throws IOException {
        BufferedImage image = null;
        try {
            image = new GoodsPoster(smProductEntityEx).draw();
            ImgUtil.writePng(image, res.getOutputStream());
        }finally {
            if(image != null)
                image.getGraphics().dispose();
        }
    }
}
