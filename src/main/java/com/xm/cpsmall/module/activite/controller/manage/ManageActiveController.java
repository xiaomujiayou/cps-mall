package com.xm.cpsmall.module.activite.controller.manage;

import com.xm.cpsmall.module.activite.serialize.form.CashoutApprovalForm;
import com.xm.cpsmall.module.activite.service.manage.CashoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api-active/manage/active")
public class ManageActiveController {

    @Autowired
    private CashoutService cashoutService;

    /**
     * 提现审批
     */
    @PostMapping("/cashout/approval")
    public void approval(@Valid @RequestBody CashoutApprovalForm cashoutApprovalForm){
        cashoutService.approval(cashoutApprovalForm.getCashoutRecordIds());
    }
}
