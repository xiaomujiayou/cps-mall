package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.module.user.mapper.SuFeedbackMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuFeedbackEntity;
import com.xm.cpsmall.module.user.serialize.form.AddFeedBackForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RequestMapping("/api-user/feedback")
@RestController
public class FeedBackController {

    @Autowired
    private SuFeedbackMapper suFeedbackMapper;

    @PostMapping
    public String add(@LoginUser Integer userId, @Valid @RequestBody AddFeedBackForm addFeedBackForm, BindingResult bindingResult){
        SuFeedbackEntity entity = new SuFeedbackEntity();
        entity.setUserId(userId);
        entity.setDes(addFeedBackForm.getDesc());
        entity.setImages(addFeedBackForm.getImgs() == null?null:String.join(",",addFeedBackForm.getImgs()));
        entity.setCreateTime(new Date());
        suFeedbackMapper.insertSelective(entity);
        return "";
    }
}
