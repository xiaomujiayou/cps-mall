package com.xm.cpsmall.module.mini.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xm.cpsmall.module.mini.serialize.form.GenQrCodeForm;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

/**
 * 二维码工具接口
 */
@RestController
@RequestMapping("/api-mini/qr")
public class QrController {

    /**
     * 生成二维码
     * @param form
     * @param bindingResult
     * @param res
     * @throws IOException
     */
    @GetMapping(produces = IMAGE_PNG_VALUE)
    public void genQrCode(@Valid GenQrCodeForm form, BindingResult bindingResult, HttpServletResponse res) throws IOException {
        QrConfig config = new QrConfig();
        if(form.getWidth() != null)
            config.setWidth(form.getWidth());
        if(form.getHeight() != null)
            config.setHeight(form.getHeight());
        if(form.getLogoUrl() != null)
            config.setImg(form.getLogoUrl());
        if(form.getMargin() != null)
            config.setMargin(form.getMargin());
        if(form.getRatio() != null)
            config.setRatio(form.getRatio());
        BufferedImage qrCode = null;
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        try {
            qrCode = QrCodeUtil.generate(form.getData(),config);
            ImgUtil.writePng(qrCode,res.getOutputStream());
        }finally {
            if(qrCode != null)
                qrCode.getGraphics().dispose();
        }
    }
}
