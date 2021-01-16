package com.xm.cpsmall.module.user.serialize.form;

import com.xm.cpsmall.utils.form.AbsPageForm;
import lombok.Data;

@Data
public class GetProxyProfitForm extends AbsPageForm {

    private Integer orderColumn;

    private Integer orderBy;
    //账单状态
    private Integer state;
}
