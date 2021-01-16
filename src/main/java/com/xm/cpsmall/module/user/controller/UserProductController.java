package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.module.mall.serialize.form.GetProductForm;
import com.xm.cpsmall.module.mall.serialize.form.ProductCollectForm;
import com.xm.cpsmall.module.mall.serialize.form.ProductCollectIsForm;
import com.xm.cpsmall.module.user.serialize.form.DelUserProductForm;
import com.xm.cpsmall.module.user.serialize.vo.SuProductHistoryVo;
import com.xm.cpsmall.module.user.service.ProductService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户收藏/历史记录相关接口
 */
@RestController
@RequestMapping("/api-user/product")
public class UserProductController {

    @Autowired
    private ProductService productService;

    /**
     * 商品是否被收藏
     * @param userId
     * @param productCollectIsForm
     * @return
     */
    @GetMapping("/collect/is")
    public Boolean collect(@LoginUser Integer userId, @Valid ProductCollectIsForm productCollectIsForm, BindingResult bindingResult){
        return productService.isCollect(userId,productCollectIsForm.getPlatformType(),productCollectIsForm.getGoodsId());
    }

    /**
     * 收藏/取消收藏
     * @param userId
     * @param productCollectForm
     * @return
     */
    @PostMapping("/collect")
    public void collect(@LoginUser Integer userId, @Valid @RequestBody ProductCollectForm productCollectForm, BindingResult bindingResult){
        if(productCollectForm.getShareUserId() != null && userId.equals(productCollectForm.getShareUserId()))
            productCollectForm.setShareUserId(null);
        productService.collect(userId,productCollectForm.getPlatformType(),productCollectForm.getGoodsId(),productCollectForm.getShareUserId(),productCollectForm.getIsCollect());
    }

    /**
     * 删除一条记录
     * @return
     */
    @PostMapping("/del")
    public void delHistory(@Valid @LoginUser @RequestBody DelUserProductForm delUserProductForm, BindingResult bindingResult){
        productService.delHistory(
                delUserProductForm.getUserId(),
                delUserProductForm.getId(),
                delUserProductForm.getProductType());
    }

    /**
     * 获取收藏/历史记录
     * @param userId
     * @param getProductForm
     * @param bindingResult
     * @return
     */
    @GetMapping
    public PageBean<SuProductHistoryVo> get(@LoginUser Integer userId, @Valid GetProductForm getProductForm, BindingResult bindingResult){
        return productService.getUserProduct(
                userId,
                getProductForm.getPageNum(),
                getProductForm.getPageSize(),
                getProductForm.getSuProductType());
    }

    /**
     * 添加一条历史记录
     * @param userId
     * @return
     */
//    @PostMapping("/history")
//    public void get(@LoginUser Integer userId,@RequestBody @Valid AddUserHistoryForm addUserHistoryForm,BindingResult bindingResult){
//        if(userId != null) {
//            Integer shareUserId = addUserHistoryForm.getShareUserId()== null?null:userId.equals(addUserHistoryForm.getShareUserId())?null:addUserHistoryForm.getShareUserId();
//            productService.addHistory(userId,addUserHistoryForm.getPlatformType(),addUserHistoryForm.getGoodsId(),shareUserId,addUserHistoryForm.getSmProductVo());
//        }
//        //添加分享记录
//        if(addUserHistoryForm.getShareUserId() != null && !addUserHistoryForm.getShareUserId().equals(userId)){
//            shareService.show(addUserHistoryForm.getShareUserId(),addUserHistoryForm.getGoodsId(),addUserHistoryForm.getPlatformType());
//        }
//    }

}
