package com.xm.cpsmall.module.mall.controller;

import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.CalcProfitForm;
import com.xm.cpsmall.module.mall.service.ProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api-mall/profit")
@RestController
public class ProfitController {

    @Autowired
    private ProfitService profitService;

    @PostMapping("/calc")
    public List<SmProductEntityEx> calc(@RequestBody CalcProfitForm calcProfitForm){
        return profitService.calcProfit(calcProfitForm.getSmProductEntities(),calcProfitForm.getUserId());
    }
}
