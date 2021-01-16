package com.xm.cpsmall.module.activite.service.manage;


import java.util.List;

public interface CashoutService {
    //提现审批
    void approval(List<Integer> cashoutRecordIds);
}
