package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.user.serialize.entity.SuSearchEntity;
import com.xm.cpsmall.module.user.serialize.form.AddSearchForm;
import com.xm.cpsmall.module.user.service.SearchService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-user/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private MallConfigService mallConfigService;


    @GetMapping
    public String[] getSearch(@LoginUser Integer userId, Integer pageNum, Integer pageSize){
        PageBean<SuSearchEntity> result = searchService.get(userId,pageNum,pageSize);
        return result.getList().stream().map(SuSearchEntity::getKeyword).collect(Collectors.toList()).toArray(new String[0]);
    }

    @DeleteMapping
    public void deleteAll(@LoginUser Integer userId){
        searchService.deleteAll(userId);
    }

    @PostMapping
    public void add(@LoginUser Integer userId, @RequestBody @Valid AddSearchForm addSearchForm, BindingResult bindingResult){
        searchService.add(userId,addSearchForm.getKeyWords());
    }

    @GetMapping("/recommend")
    public String[] getRecommend(@LoginUser(necessary = false) Integer userId, Integer pageNum, Integer pageSize){
        String suggestStr = mallConfigService.getConfig(userId, ConfigEnmu.PRODUCT_SEARCH_RECOMMEND, ConfigTypeConstant.SYS_CONFIG).getVal();
        return suggestStr.split(",");
    }
}
