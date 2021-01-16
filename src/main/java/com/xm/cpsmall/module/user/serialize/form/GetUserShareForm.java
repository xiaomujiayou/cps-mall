package com.xm.cpsmall.module.user.serialize.form;

import com.xm.cpsmall.utils.form.AbsPageForm;
import lombok.Data;

@Data
public class GetUserShareForm extends AbsPageForm {
    private Integer orderType;
    private Integer order;
}
