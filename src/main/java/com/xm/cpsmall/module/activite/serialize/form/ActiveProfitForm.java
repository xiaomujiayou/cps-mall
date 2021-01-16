package com.xm.cpsmall.module.activite.serialize.form;

import com.xm.cpsmall.utils.form.BaseForm;
import lombok.Data;

@Data
public class ActiveProfitForm extends BaseForm {
    private Integer activeId;
    private Integer state;
}
