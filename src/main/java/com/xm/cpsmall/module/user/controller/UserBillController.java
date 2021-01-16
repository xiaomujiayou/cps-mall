package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.lottery.serialize.ex.SlPropSpecEx;
import com.xm.cpsmall.module.user.serialize.bo.SuBillToPayBo;
import com.xm.cpsmall.module.user.serialize.dto.BillOrderDto;
import com.xm.cpsmall.module.user.serialize.vo.BillVo;
import com.xm.cpsmall.module.user.service.UserBillService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-user/bill")
public class UserBillController {

    @Autowired
    private UserBillService userBillService;

    @GetMapping
    public PageBean<BillVo> get(@LoginUser Integer userId, Integer state, Integer type, Integer pageNum, Integer pageSize){
        return userBillService.getList(userId,state,type,pageNum,pageSize);
    }

    /**
     * 生成道具购买账单，供道具服务调用
     * @param slPropSpecEx
     * @return
     */
    @PostMapping("/create/prop")
    public SuBillToPayBo createByProp(@RequestBody SlPropSpecEx slPropSpecEx){
        if(slPropSpecEx == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        return userBillService.createByProp(slPropSpecEx);
    }

    @GetMapping("/info")
    public List<BillOrderDto> getBillInfo(Integer userId, @RequestParam("billIds") List<String> billIds){
        return userBillService.getBillInfo(userId,billIds);
    }
}
