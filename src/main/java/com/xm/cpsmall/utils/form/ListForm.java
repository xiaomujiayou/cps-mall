package com.xm.cpsmall.utils.form;

import lombok.Data;

@Data
public class ListForm extends BaseForm {
    private Integer pageNum;
    private Integer pageSize;
}
