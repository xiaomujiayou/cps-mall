package com.xm.cpsmall.module.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.comm.mq.message.config.BillMqConfig;
import com.xm.cpsmall.comm.mq.message.config.PayMqConfig;
import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import com.xm.cpsmall.comm.mq.message.impl.PayOrderCreateMessage;
import com.xm.cpsmall.comm.mq.message.impl.PayOrderSucessMessage;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.pay.config.WxPayPropertiesEx;
import com.xm.cpsmall.module.pay.mapper.SpWxEntPayOrderInMapper;
import com.xm.cpsmall.module.pay.mapper.SpWxOrderInMapper;
import com.xm.cpsmall.module.pay.mapper.SpWxOrderNotifyMapper;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxEntPayOrderInEntity;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxOrderInEntity;
import com.xm.cpsmall.module.pay.serialize.entity.SpWxOrderNotifyEntity;
import com.xm.cpsmall.module.pay.serialize.message.ActiveAutoEntPayMessage;
import com.xm.cpsmall.module.pay.serialize.message.ActiveEntPayMessage;
import com.xm.cpsmall.module.pay.serialize.message.EntPayMessage;
import com.xm.cpsmall.module.pay.serialize.vo.WxPayOrderResultVo;
import com.xm.cpsmall.module.pay.service.WxPayApiService;
import com.xm.cpsmall.module.user.serialize.bo.SuBillToPayBo;
import com.xm.cpsmall.utils.StringUtils;
import com.xm.cpsmall.utils.product.GenNumUtil;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("wxPayApiService")
public class WxPayApiServiceImpl implements WxPayApiService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SpWxOrderInMapper spWxOrderInMapper;
    @Autowired
    private SpWxOrderNotifyMapper spWxOrderNotifyMapper;
    @Autowired
    private WxPayService wxService;
    @Resource(name = "wxPayPropertiesEx")
    private WxPayPropertiesEx wxPayPropertiesEx;
    @Autowired
    private SpWxEntPayOrderInMapper spWxEntPayOrderInMapper;
    @Value("${spring.profiles.active}")
    private String active;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WxPayOrderResultVo collection(SuBillToPayBo suBillToPayBo) throws WxPayException {
        WxPayUnifiedOrderRequest request = createWxOrderRequest(suBillToPayBo);
        WxPayMpOrderResult res = null;
        SpWxOrderInEntity spWxOrderInEntity = null;
        WxPayOrderResultVo wxPayOrderResultVo = null;
        res = wxService.createOrder(request);
        if (StrUtil.isBlank(res.getPackageValue()) || !res.getPackageValue().contains("prepay_id"))
            throw new GlobleException(MsgEnum.WX_PAY_ORDER_CREATE_FAIL);
        spWxOrderInEntity = saveOrder(suBillToPayBo, request, null, null, null, res.getPackageValue());
        wxPayOrderResultVo = new WxPayOrderResultVo();
        wxPayOrderResultVo.setPackageValue(res.getPackageValue());
        wxPayOrderResultVo.setNonceStr(res.getNonceStr());
        wxPayOrderResultVo.setPaySign(res.getPaySign());
        wxPayOrderResultVo.setTimeStamp(res.getTimeStamp());
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE, "", new PayOrderCreateMessage(suBillToPayBo.getUserId(), suBillToPayBo, spWxOrderInEntity, suBillToPayBo, wxPayOrderResultVo));
        return wxPayOrderResultVo;
    }

    /**
     * 保存记录
     *
     * @param request
     * @param returnMsg
     * @param errCode
     * @param errCodeDes
     */
    private SpWxOrderInEntity saveOrder(SuBillToPayBo suBillToPayBo, WxPayUnifiedOrderRequest request, String returnMsg, String errCode, String errCodeDes, String packageVal) {
        SpWxOrderInEntity spWxOrderInEntity = new SpWxOrderInEntity();
        spWxOrderInEntity.setReqBo(JSON.toJSONString(suBillToPayBo));
        BeanUtil.copyProperties(request, spWxOrderInEntity);
        spWxOrderInEntity.setState(0);
        spWxOrderInEntity.setPackageVal(packageVal);
        JSONObject errMsgJson = new JSONObject();
        errMsgJson.put("returnMsg", returnMsg);
        errMsgJson.put("errCode", errCode);
        errMsgJson.put("errCodeDes", errCodeDes);
        spWxOrderInEntity.setErrMsg(errMsgJson.isEmpty() ? null : errMsgJson.toJSONString());
        spWxOrderInEntity.setCreateTime(new Date());
        spWxOrderInMapper.insertSelective(spWxOrderInEntity);
        return spWxOrderInEntity;
    }


    /**
     * 根据账单生成同一下单订单
     *
     * @param suBillToPayBo
     * @return
     */
    private WxPayUnifiedOrderRequest createWxOrderRequest(SuBillToPayBo suBillToPayBo) {
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setAppid(wxPayPropertiesEx.getAppId());
        wxPayUnifiedOrderRequest.setMchId(wxPayPropertiesEx.getMchId());
        wxPayUnifiedOrderRequest.setBody(suBillToPayBo.getDes());
        Map<String, Object> attach = new HashMap<>();
        attach.put("billId", suBillToPayBo.getId());
        attach.put("userId", suBillToPayBo.getUserId());
        wxPayUnifiedOrderRequest.setAttach(JSON.toJSONString(attach));
        wxPayUnifiedOrderRequest.setOutTradeNo(GenNumUtil.genOrderNum());
        wxPayUnifiedOrderRequest.setTotalFee(suBillToPayBo.getMoney());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(suBillToPayBo.getClientIp());
        wxPayUnifiedOrderRequest.setNotifyUrl(wxPayPropertiesEx.getNotifyUrl());
        wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayUnifiedOrderRequest.setOpenid(suBillToPayBo.getOpenId());
        return wxPayUnifiedOrderRequest;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payment(EntPayMessage entPayMessage) throws WxPayException {
        SpWxEntPayOrderInEntity record = new SpWxEntPayOrderInEntity();
        record.setType(1);
        record.setBillPayId(entPayMessage.getScBillPayEntity().getId());
        if (spWxEntPayOrderInMapper.selectCount(record) > 0)
            return;
        EntPayRequest request = createEntPayRequest(
                wxPayPropertiesEx.getAppId(),
                wxPayPropertiesEx.getMchId(),
                StrUtil.isBlank(entPayMessage.getRetryTradeNo()) ? GenNumUtil.genWxEntPayOrderNum() : entPayMessage.getRetryTradeNo(),
                entPayMessage.getScBillPayEntity().getOpenId(),
                entPayMessage.getDesc(),
                entPayMessage.getIp(),
                entPayMessage.getScBillPayEntity().getTotalMoney());
        EntPayResult result = null;
        SpWxEntPayOrderInEntity spWxEntPayOrderInEntity = null;
        try {
            if (CollUtil.newArrayList(active.split(",")).contains("dev")) {
                result = new EntPayResult();
            }
            if (CollUtil.newArrayList(active.split(",")).contains("prod")) {
                result = wxService.getEntPayService().entPay(request);
            }
            spWxEntPayOrderInEntity = entPay(1,entPayMessage,null,null, request, result, null);
        } catch (WxPayException e) {
            spWxEntPayOrderInEntity = entPay(1,entPayMessage,null,null, request, null, e);
            log.error("微信企业付款失败 信息：{} error：{}", JSON.toJSONString(spWxEntPayOrderInEntity), e);
        } finally {
            rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE_ENT, "", spWxEntPayOrderInEntity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void paymentActive(ActiveEntPayMessage activeEntPayMessage) {
        SpWxEntPayOrderInEntity record = new SpWxEntPayOrderInEntity();
        record.setType(2);
        record.setBillPayId(activeEntPayMessage.getSaCashOutRecordEntity().getId());
        if (spWxEntPayOrderInMapper.selectCount(record) > 0)
            return;
        EntPayRequest request = createEntPayRequest(
                wxPayPropertiesEx.getAppId(),
                wxPayPropertiesEx.getMchId(),
                StrUtil.isBlank(activeEntPayMessage.getRetryTradeNo()) ? GenNumUtil.genWxEntPayOrderNum() : activeEntPayMessage.getRetryTradeNo(),
                activeEntPayMessage.getSaCashOutRecordEntity().getOpenId(),
                activeEntPayMessage.getDesc(),
                activeEntPayMessage.getIp(),
                activeEntPayMessage.getSaCashOutRecordEntity().getMoney());
        EntPayResult result = null;
        SpWxEntPayOrderInEntity spWxEntPayOrderInEntity = null;
        try {
            if (CollUtil.newArrayList(active.split(",")).contains("dev")) {
                result = new EntPayResult();
            }
            if (CollUtil.newArrayList(active.split(",")).contains("prod")) {
                result = wxService.getEntPayService().entPay(request);
            }
            spWxEntPayOrderInEntity = entPay(2,null,activeEntPayMessage,null, request, result, null);
        } catch (WxPayException e) {
            spWxEntPayOrderInEntity = entPay(2,null,activeEntPayMessage,null, request, null, e);
            log.error("微信企业付款失败 信息：{} error：{}", JSON.toJSONString(spWxEntPayOrderInEntity), e);
        } finally {
            rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE_ENT, "", spWxEntPayOrderInEntity);
        }
    }

    private EntPayRequest createEntPayRequest(String appId,String mchId,String tradeNo,String openId,String desc,String billCreateIp,Integer money){
        EntPayRequest request = new EntPayRequest();
        request.setMchAppid(appId);
        request.setMchId(mchId);
        request.setPartnerTradeNo(tradeNo);
        request.setOpenid(openId);
        request.setCheckName("NO_CHECK");
        request.setDescription(StringUtils.getMaxByteLength(desc,100));
        request.setSpbillCreateIp(billCreateIp);
        request.setAmount(money);
        return request;
    }

    @Override
    public void paymentActiveAuto(ActiveAutoEntPayMessage activeAutoEntPayMessage) {
        SpWxEntPayOrderInEntity record = new SpWxEntPayOrderInEntity();
        record.setType(3);
        record.setBillPayId(activeAutoEntPayMessage.getSaBillEntity().getId());
        if (spWxEntPayOrderInMapper.selectCount(record) > 0)
            return;
        EntPayRequest request = createEntPayRequest(
                wxPayPropertiesEx.getAppId(),
                wxPayPropertiesEx.getMchId(),
                StrUtil.isBlank(activeAutoEntPayMessage.getRetryTradeNo()) ? GenNumUtil.genWxEntPayOrderNum() : activeAutoEntPayMessage.getRetryTradeNo(),
                activeAutoEntPayMessage.getSaBillEntity().getOpenId(),
                activeAutoEntPayMessage.getDesc(),
                activeAutoEntPayMessage.getIp(),
                activeAutoEntPayMessage.getSaBillEntity().getMoney());
        EntPayResult result = null;
        SpWxEntPayOrderInEntity spWxEntPayOrderInEntity = null;
        try {
            if (CollUtil.newArrayList(active.split(",")).contains("dev")) {
                result = new EntPayResult();
            }
            if (CollUtil.newArrayList(active.split(",")).contains("prod")) {
                result = wxService.getEntPayService().entPay(request);
            }
            spWxEntPayOrderInEntity = entPay(3,null,null,activeAutoEntPayMessage, request, result, null);
        } catch (WxPayException e) {
            spWxEntPayOrderInEntity = entPay(3,null,null,activeAutoEntPayMessage, request, null, e);
            log.error("微信企业付款失败 信息：{} error：{}", JSON.toJSONString(spWxEntPayOrderInEntity), e);
        } finally {
            rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE_ENT, "", spWxEntPayOrderInEntity);
        }
    }

    //保存付款记录
    private SpWxEntPayOrderInEntity entPay(Integer type, EntPayMessage entPayMessage, ActiveEntPayMessage activeEntPayMessage,ActiveAutoEntPayMessage activeAutoEntPayMessage, EntPayRequest request, EntPayResult result, WxPayException e) {
        SpWxEntPayOrderInEntity entity = new SpWxEntPayOrderInEntity();
        BeanUtil.copyProperties(request, entity);
        entity.setType(type);
        if (type == 1) {
            //订单返现
            entity.setUserId(entPayMessage.getScBillPayEntity().getUserId());
            entity.setBillPayId(entPayMessage.getScBillPayEntity().getId());
            entity.setBillIds(entPayMessage.getScBillPayEntity().getBillIds());
        } else if (type == 2) {
            //活动提现
            entity.setUserId(activeEntPayMessage.getUserId());
            entity.setBillPayId(activeEntPayMessage.getSaCashOutRecordEntity().getId());
            entity.setBillIds(activeEntPayMessage.getSaCashOutRecordEntity().getBillIds());
        } else if(type == 3){
            //活动自动返现
            entity.setUserId(activeAutoEntPayMessage.getUserId());
            entity.setBillPayId(activeAutoEntPayMessage.getSaBillEntity().getId());
            entity.setBillIds(activeAutoEntPayMessage.getSaBillEntity().getId().toString());
        }
        entity.setDes(request.getDescription());
        entity.setCreateTime(new Date());
        if (!(result == null)) {
            BeanUtil.copyProperties(result, entity);
            entity.setState(1);
        }
        if (e != null) {
            entity.setReturnCode(e.getReturnCode());
            entity.setReturnMsg(e.getReturnMsg());
            entity.setResultCode(e.getResultCode());
            entity.setErrCode(e.getErrCode());
            entity.setErrCodeDes(e.getErrCodeDes());
            entity.setState(2);
        }
        spWxEntPayOrderInMapper.insertSelective(entity);
        return entity;
    }


    @Override
    public void orderNotify(WxPayOrderNotifyResult notifyResult) {
        //判断是否已处理
        SpWxOrderNotifyEntity record = new SpWxOrderNotifyEntity();
        record.setOutTradeNo(notifyResult.getOutTradeNo());
        if (spWxOrderNotifyMapper.selectCount(record) > 0) {
            log.debug("微信支付回调：该支付信息已被处理 单号：[{}]", notifyResult.getOutTradeNo());
            return;
        }
        SpWxOrderNotifyEntity spWxOrderNotifyEntity = new SpWxOrderNotifyEntity();
        BeanUtil.copyProperties(notifyResult, spWxOrderNotifyEntity);
        spWxOrderNotifyEntity.setCreateTime(new Date());
        spWxOrderNotifyMapper.insertSelective(spWxOrderNotifyEntity);
        rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE, PayMqConfig.KEY_WX_NOTIFY, spWxOrderNotifyEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onPaySucess(SpWxOrderNotifyEntity spWxOrderNotifyEntity) {
        Integer userId = JSON.parseObject(spWxOrderNotifyEntity.getAttach()).getInteger("userId");
        PageHelper.startPage(1, 1).count(false);
        SpWxOrderInEntity spWxOrderInEntity = new SpWxOrderInEntity();
        spWxOrderInEntity.setOutTradeNo(spWxOrderNotifyEntity.getOutTradeNo());
        spWxOrderInEntity = spWxOrderInMapper.selectOne(spWxOrderInEntity);
        SuBillToPayBo suBillToPayBo = JSON.parseObject(spWxOrderInEntity.getReqBo(), SuBillToPayBo.class);
        rabbitTemplate.convertAndSend(BillMqConfig.EXCHANGE, BillMqConfig.KEY_PAY_SUCESS, suBillToPayBo);
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE, "", new PayOrderSucessMessage(userId, suBillToPayBo, spWxOrderNotifyEntity));
    }

}
