package com.xm.cpsmall.module.wind.serialize.vo;

import com.xm.cpsmall.module.wind.serialize.entity.SwCreditBillConfEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserCreditVo {
    private Integer scores;
    private SwCreditBillConfEntity swCreditBillConfEntity;
    private String desc;
    private List<String> getCredit;
    private Integer bindCount;
    private Integer bindTotalMoney;
}
