package com.xm.cpsmall.module.activite.serialize.form;

import com.xm.cpsmall.utils.form.ListForm;
import lombok.Data;

@Data
public class ActiveBillListForm extends ListForm {
    private Integer activeId;
    private Integer state;
}
