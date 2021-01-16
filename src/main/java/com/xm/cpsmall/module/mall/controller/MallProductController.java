package com.xm.cpsmall.module.mall.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xm.cpsmall.annotation.AppType;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.annotation.Pid;
import com.xm.cpsmall.annotation.PlatformType;
import com.xm.cpsmall.exception.ApiCallException;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.aspect.DispatchServiceAspect;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.serialize.bo.ProductIndexBo;
import com.xm.cpsmall.module.mall.serialize.bo.ShareLinkBo;
import com.xm.cpsmall.module.mall.serialize.entity.SmProductEntity;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailForm;
import com.xm.cpsmall.module.mall.serialize.form.GoodsDetailsForm;
import com.xm.cpsmall.module.mall.serialize.form.SaleInfoForm;
import com.xm.cpsmall.module.mall.serialize.form.UrlParseForm;
import com.xm.cpsmall.module.mall.serialize.vo.SmProductVo;
import com.xm.cpsmall.module.mall.service.api.GoodsListService;
import com.xm.cpsmall.module.mall.service.api.GoodsService;
import com.xm.cpsmall.module.mall.service.api.OptGoodsListService;
import com.xm.cpsmall.module.wind.service.CreditBillService;
import com.xm.cpsmall.utils.TextToGoodsUtils;
import com.xm.cpsmall.utils.form.BaseForm;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api-mall/product")
public class MallProductController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private MallProductController mallProductController;
    @Autowired
    private DispatchServiceAspect dispatchServiceAspect;
    @Resource(name = "myExecutor")
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private CreditBillService creditBillService;

    /**
     * 商品列表
     * @return
     */
    @PostMapping("/list")
    public Object getProductList(@LoginUser @Pid @PlatformType @AppType BaseForm baseForm, @RequestBody JSONObject params) throws Exception {
        params.putAll((JSONObject)JSON.toJSON(baseForm));
        PageBean<SmProductEntityEx> pageBean = (PageBean<SmProductEntityEx>)dispatchServiceAspect.getList(
                params,
                GoodsListService.class,
                OptGoodsListService.class);
        //信用支付
        pageBean.setList(creditBillService.productCheck(pageBean.getList()));
        List<SmProductVo> list = pageBean.getList().stream().map(o->{
            SmProductVo smProductVo = new SmProductVo();
            BeanUtil.copyProperties(o,smProductVo);
            return smProductVo;
        }).collect(Collectors.toList());
        PageBean<SmProductVo> productVoPageBean = new PageBean<>();
        productVoPageBean.setList(list);
        productVoPageBean.setPageNum(pageBean.getPageNum());
        productVoPageBean.setPageSize(pageBean.getPageSize());
        productVoPageBean.setTotal(pageBean.getTotal());
        return productVoPageBean;
    }



    /**
     * 获取商品详情
     * @return
     */
    @GetMapping("/detail")
    public SmProductVo getProductDetail(@Valid @PlatformType @LoginUser(necessary = false) @Pid(necessary = false) GoodsDetailForm goodsDetailForm, BindingResult bindingResult) throws Exception {
        return praseDetailVo(mallProductController.getDetailEx(goodsDetailForm));
    }

    public SmProductEntityEx getDetailEx(GoodsDetailForm goodsDetailForm) throws Exception {
        SmProductEntityEx smProductEntityEx = goodsService.detail(goodsDetailForm);
        return smProductEntityEx;
    }

    private SmProductVo praseDetailVo(SmProductEntityEx smProductEntityEx){
        List<SmProductEntityEx> smProductEntityExes = creditBillService.productCheck(Arrays.asList(smProductEntityEx));
        if(smProductEntityExes != null && !smProductEntityExes.isEmpty())
            smProductEntityEx = smProductEntityExes.get(0);
        SmProductVo smProductVo = new SmProductVo();
        BeanUtil.copyProperties(smProductEntityEx,smProductVo);
        return smProductVo;
    }

    /**
     * 批量获取商品详情
     * @return
     */
    @GetMapping("/details")
    public List<SmProductEntity> getProductDetails(Integer platformType, @RequestParam("goodsIds") List<String> goodsIds) throws Exception {
        if(goodsIds == null || goodsIds.isEmpty())
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        GoodsDetailsForm goodsDetailsForm = new GoodsDetailsForm();
        goodsDetailsForm.setPlatformType(platformType);
        goodsDetailsForm.setGoodsIds(goodsIds);
        return goodsService.details(goodsDetailsForm);
    }

    /**
     * 批量获取商品详情
     * @return
     */
    @PostMapping("/details")
    public List<SmProductEntity> getProductDetails(@RequestBody List<ProductIndexBo> productIndexBos) throws Exception {
        Map<Integer, List<ProductIndexBo>> group = productIndexBos.stream().collect(Collectors.groupingBy(ProductIndexBo::getPlatformType));
        List<Future<List<SmProductEntity>>> futures = new ArrayList<>();
        for (Map.Entry<Integer, List<ProductIndexBo>> integerListEntry : group.entrySet()) {
            Future<List<SmProductEntity>> listFuture = executor.submit(new Callable<List<SmProductEntity>>() {
                @Override
                public List<SmProductEntity> call() throws Exception {
                    GoodsDetailsForm goodsDetailsForm = new GoodsDetailsForm();
                    goodsDetailsForm.setPlatformType(integerListEntry.getKey());
                    goodsDetailsForm.setGoodsIds(integerListEntry.getValue()
                            .stream()
                            .map(ProductIndexBo::getGoodsId)
                            .collect(Collectors.toList()));
                    return goodsService.details(goodsDetailsForm);
                }
            });
            futures.add(listFuture);
        }
        //合并结果
        List<SmProductEntity> result = new ArrayList<>();
        futures.forEach(o ->{
            try {
                List<SmProductEntity> smProductEntities = o.get();
                result.addAll(smProductEntities);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    @GetMapping("/sale")
    public ShareLinkBo getProductSaleInfo(@Valid @LoginUser @PlatformType @Pid SaleInfoForm saleInfoForm) throws Exception {
        if(saleInfoForm.getUserId().equals(saleInfoForm.getShareUserId()))
            saleInfoForm.setShareUserId(null);
        return goodsService.saleInfo(saleInfoForm);
    }

    @GetMapping("/url/parse")
    public TextToGoodsUtils.GoodsSpec parseUrl(@Valid @LoginUser(necessary = false) @Pid(necessary = false) UrlParseForm urlParseForm, BindingResult bindingResult) throws Exception {
        TextToGoodsUtils.GoodsSpec goodsSpec = TextToGoodsUtils.parse(urlParseForm.getUrl());
        if(goodsSpec.getParseType() == 1){
            //解析为具体商品
            if(goodsSpec.getPlatformType() == PlatformTypeConstant.PDD){
                try {
                    GoodsDetailForm goodsDetailForm = new GoodsDetailForm();
                    BeanUtil.copyProperties(urlParseForm,goodsDetailForm);
                    goodsDetailForm.setGoodsId(goodsSpec.getGoodsId());
                    goodsDetailForm.setPlatformType(goodsSpec.getPlatformType());
                    goodsSpec.setGoodsInfo(praseDetailVo(mallProductController.getDetailEx(goodsDetailForm)));
                }catch (ApiCallException e){
                    goodsSpec.setParseType(4);
//                    BaseGoodsDetailForm baseGoodsDetailForm = new BaseGoodsDetailForm();
//                    BeanUtil.copyProperties(urlParseForm,baseGoodsDetailForm);
//                    baseGoodsDetailForm.setGoodsId(goodsSpec.getGoodsId());
//                    goodsSpec.setSimpleInfo(goodsService.basicDetail(baseGoodsDetailForm));
                }
            }else if(Arrays.asList(PlatformTypeConstant.MGJ,PlatformTypeConstant.WPH).contains(goodsSpec.getPlatformType())){
                try {
                    GoodsDetailForm goodsDetailForm = new GoodsDetailForm();
                    BeanUtil.copyProperties(urlParseForm,goodsDetailForm);
                    goodsDetailForm.setGoodsId(goodsSpec.getGoodsId());
                    goodsDetailForm.setPlatformType(goodsSpec.getPlatformType());
                    goodsSpec.setGoodsInfo(praseDetailVo(mallProductController.getDetailEx(goodsDetailForm)));
                }catch (Exception e){
                    goodsSpec.setParseType(4);
                }
            }
        }
        return goodsSpec;
    }
}
