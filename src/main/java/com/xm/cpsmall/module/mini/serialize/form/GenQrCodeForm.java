package com.xm.cpsmall.module.mini.serialize.form;

import com.xm.cpsmall.utils.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GenQrCodeForm extends BaseForm {
    @NotBlank(message = "请输入要生成二维码的数据")
    private String data;
    private Integer width;
    private Integer height;
    //边距
    private Integer margin;
    private String logoUrl;
    //LOGO缩放系数
    private Integer ratio;
}
