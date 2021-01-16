package com.xm.cpsmall.module.activite.serialize.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CashoutApprovalForm {
    @NotNull(message = "请输入提现记录ID")
    private List<Integer> cashoutRecordIds;
}
