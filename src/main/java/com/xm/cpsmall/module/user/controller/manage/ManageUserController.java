package com.xm.cpsmall.module.user.controller.manage;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.module.user.mapper.SuUserMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.serialize.form.UserSearchForm;
import com.xm.cpsmall.utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@RestController
@RequestMapping("/api-user/manage/user")
public class ManageUserController {

    @Autowired
    private SuUserMapper suUserMapper;

    /**
     * 查询用户
     * @param userSearchForm
     * @return
     */
    @GetMapping
    public PageBean<SuUserEntity> getUser(UserSearchForm userSearchForm){
        PageHelper.startPage(userSearchForm.getPageNum(),userSearchForm.getPageSize());
        OrderByHelper.orderBy("create_time desc");
        List<SuUserEntity> list = suUserMapper.selectByExample(formToExample(userSearchForm));
        PageBean<SuUserEntity> result = new PageBean<>(list);
        return result;
    }

    /**
     * 用户计数
     * @param userSearchForm
     * @return
     */
    @GetMapping("/count")
    public Integer getUserCount(UserSearchForm userSearchForm){
        return suUserMapper.selectCountByExample(formToExample(userSearchForm));
    }

    private Example formToExample(UserSearchForm userSearchForm){
        Example example = new Example(SuUserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(userSearchForm.getUserId() != null)
            criteria.andEqualTo("id",userSearchForm.getUserId());
        if(userSearchForm.getParentId() != null)
            criteria.andEqualTo("parentId",userSearchForm.getParentId());
        if(StrUtil.isNotBlank(userSearchForm.getUserSn()))
            criteria.andEqualTo("userSn",userSearchForm.getUserSn());
        if(userSearchForm.getPId() != null)
            criteria.andEqualTo("pid",userSearchForm.getPId());
        if(ObjectUtil.isAllNotEmpty(userSearchForm.getCreateStart(),userSearchForm.getCreateEnd()))
            criteria.andBetween("createTime",userSearchForm.getCreateStart(),userSearchForm.getCreateEnd());
        if(ObjectUtil.isAllNotEmpty(userSearchForm.getLastStart(),userSearchForm.getLastEnd()))
            criteria.andBetween("lastLogin",userSearchForm.getLastStart(),userSearchForm.getLastEnd());
        return example;
    }
}
