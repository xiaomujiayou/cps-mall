package com.xm.cpsmall.module.user.controller;

import cn.hutool.core.date.DateUtil;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.module.user.serialize.dto.OrderBillDto;
import com.xm.cpsmall.module.user.serialize.form.GetOrderForm;
import com.xm.cpsmall.module.user.serialize.vo.OrderBillVo;
import com.xm.cpsmall.module.user.service.OrderService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取用户订单
     * @param getOrderForm
     * @param bindingResult
     * @return
     */
    @GetMapping
    public PageBean<OrderBillVo> myOrder(@Valid @LoginUser GetOrderForm getOrderForm, BindingResult bindingResult){
        getOrderForm.setPlatformType(null);
        PageBean<OrderBillDto> pageBean = orderService.getOrderBill(
                getOrderForm.getUserId(),
                getOrderForm.getType(),
                getOrderForm.getPlatformType(),
                getOrderForm.getState(),
                getOrderForm.getPageNum(),
                getOrderForm.getPageSize());
        List<OrderBillVo> vos = pageBean.getList().stream().map(o->{
            OrderBillVo vo = new OrderBillVo();
            vo.setOrderNum(o.getOrderSubSn());
            vo.setUserName(o.getSuUserEntity().getNickname());
            vo.setHeadImg(o.getSuUserEntity().getHeadImg());
            vo.setProductImgUrl(o.getImgUrl());
            vo.setTitle(o.getProductName());
            vo.setPlatformType(o.getPlatformType());
            vo.setGoodsId(o.getProductId());
            vo.setBillId(o.getSuBillEntity().getId());
            vo.setBillSn(o.getSuBillEntity().getBillSn());
            vo.setBillState(o.getSuBillEntity().getState());
            vo.setFailReason(o.getFailReason());
            vo.setOrderState(o.getState());
            vo.setOrderSn(o.getOrderSn());
            vo.setOriginalPrice(o.getOriginalPrice());
            vo.setQuantity(o.getQuantity());
            vo.setAmount(o.getAmount());
            vo.setMoney(o.getSuBillEntity().getMoney());
            vo.setCreditState(o.getSuBillEntity().getCreditState());
            vo.setCreateTime(DateUtil.format(o.getCreateTime(),"MM-dd HH:mm"));
            vo.setPayTime(o.getSuBillEntity().getPayTime());
            return vo;
        }).collect(Collectors.toList());
        PageBean<OrderBillVo> result = new PageBean<>(vos);
        result.setPageNum(pageBean.getPageNum());
        result.setPageSize(pageBean.getPageSize());
        result.setTotal(pageBean.getTotal());
        return result;
    }
}
