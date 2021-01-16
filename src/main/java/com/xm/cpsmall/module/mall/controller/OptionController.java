package com.xm.cpsmall.module.mall.controller;

import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.annotation.PlatformType;
import com.xm.cpsmall.module.mall.serialize.entity.SmOptEntity;
import com.xm.cpsmall.module.mall.serialize.form.OptionForm;
import com.xm.cpsmall.module.mall.service.api.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/api-mall/option")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @GetMapping
    public List<SmOptEntity> get(@LoginUser(necessary = false) @PlatformType OptionForm optionForm) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return optionService.childList(optionForm);
    }
}
