package com.xm.cpsmall.module.mall.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xm.cpsmall.comm.mq.message.config.UserActionConfig;
import com.xm.cpsmall.comm.mq.message.impl.UserClickGoodsMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserSearchGoodsMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserShareGoodsMessage;
import com.xm.cpsmall.comm.mq.message.impl.UserSmartSearchGoodsMessage;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.KeywordGoodsListForm;
import com.xm.cpsmall.module.mall.serialize.form.UrlParseForm;
import com.xm.cpsmall.utils.TextToGoodsUtils;
import com.xm.cpsmall.utils.mybatis.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成用户动作mq消息
 */
@Aspect
@Component
@Slf4j
public class MallUserActionMessageAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("execution(public * com.xm.cpsmall.module.mall.controller.MallProductController.getDetailEx(..))")
    public void clickGoodsMessagePointCut(){}

    /**
     * 生成
     * UserClickGoodsMessage
     * UserShareGoodsMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("clickGoodsMessagePointCut()")
    public Object clickGoodsMessagePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        SmProductEntityEx smProductEntityEx = (SmProductEntityEx)joinPoint.proceed();
        GoodsDetailForm goodsDetailForm = (GoodsDetailForm) joinPoint.getArgs()[0];
        if(goodsDetailForm.getUserId() != null){
            Integer shareUserId = null;
            if(ObjectUtil.isAllNotEmpty(goodsDetailForm.getShareUserId(),goodsDetailForm.getUserId()) && !goodsDetailForm.getShareUserId().equals(goodsDetailForm.getUserId())) {
                shareUserId = goodsDetailForm.getShareUserId();
                rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserShareGoodsMessage(shareUserId,goodsDetailForm.getUserId(),smProductEntityEx));
            }
            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserClickGoodsMessage(goodsDetailForm.getUserId(),shareUserId,goodsDetailForm.getIp(),goodsDetailForm.getAppType(),smProductEntityEx));
        }
        return smProductEntityEx;
    }

//    @Pointcut("execution(public * com.xm.api_mall.service.api.impl.def.GoodsServiceImpl.detail(..))")
//    public void goodsDetailMessagePointCut(){}
//
//    /**
//     * 生成
//     * UserShareGoodsMessage
//     * @param joinPoint
//     * @return
//     * @throws Throwable
//     */
//    @Around("goodsDetailMessagePointCut()")
//    public Object goodsDetailMessagePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
//        SmProductEntityEx smProductEntityEx = (SmProductEntityEx)joinPoint.proceed();
//        GoodsDetailForm goodsDetailForm = (GoodsDetailForm) joinPoint.getArgs()[0];
//        if(goodsDetailForm.getShareUserId() != null && goodsDetailForm.getUserId() != null && !goodsDetailForm.getShareUserId().equals(goodsDetailForm.getUserId()))
//            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserShareGoodsMessage(goodsDetailForm.getShareUserId(),goodsDetailForm.getUserId(),smProductEntityEx));
//        return smProductEntityEx;
//    }


    @Pointcut("execution(public * com.xm.cpsmall.module.mall.service.api.impl.def.GoodsListServiceImpl.keyword(..))")
    public void keywordListPointCut(){}
    /**
     * 生成
     * UserSearchGoodsMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("keywordListPointCut()")
    public Object keywordListPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        PageBean<SmProductEntityEx> smProductEntityExPageBean = (PageBean<SmProductEntityEx>)joinPoint.proceed();
        KeywordGoodsListForm keywordGoodsListForm = (KeywordGoodsListForm) joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserSearchGoodsMessage(
                keywordGoodsListForm.getUserId(),
                keywordGoodsListForm.getPlatformType(),
                keywordGoodsListForm.getKeywords(),
                keywordGoodsListForm.getPageNum()));
        return smProductEntityExPageBean;
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.mall.controller.MallProductController.parseUrl(..))")
    public void parseUrlPointCut(){}
    /**
     * 生成
     * UserSmartSearchGoodsMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("parseUrlPointCut()")
    public Object parseUrlPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        TextToGoodsUtils.GoodsSpec goodsSpecMsg = (TextToGoodsUtils.GoodsSpec)joinPoint.proceed();
        if(goodsSpecMsg.getParseType() != 1)
            return goodsSpecMsg;
        SmProductEntityEx smProductEntity = new SmProductEntityEx();
        BeanUtil.copyProperties(goodsSpecMsg.getGoodsInfo(),smProductEntity);
        UrlParseForm urlParseForm = (UrlParseForm) joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserSmartSearchGoodsMessage(urlParseForm.getUserId(),smProductEntity,urlParseForm.getUrl(),smProductEntity.getName()));
        return goodsSpecMsg;
    }
}

