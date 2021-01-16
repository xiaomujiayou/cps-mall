package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.user.serialize.form.GetUserShareForm;
import com.xm.cpsmall.module.user.serialize.vo.ShareVo;
import com.xm.cpsmall.module.user.service.ShareService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/share")
public class UserShareController {

    @Autowired
    private ShareService shareService;

    @GetMapping
    public PageBean<ShareVo> get(@LoginUser Integer userId, GetUserShareForm getUserShareForm){
        return shareService.getList(
                userId,
                getUserShareForm.getOrderType(),
                getUserShareForm.getOrder(),
                getUserShareForm.getPageNum(),
                getUserShareForm.getPageSize());
    }

    @DeleteMapping("/{id}")
    public void deleteAll(@LoginUser Integer userId,@PathVariable("id")Integer id){
        if(id == null || id == 0)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        shareService.del(userId,id);
    }
}
