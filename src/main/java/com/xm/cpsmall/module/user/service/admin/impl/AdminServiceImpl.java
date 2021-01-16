package com.xm.cpsmall.module.user.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.user.mapper.SuAdminMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuAdminEntity;
import com.xm.cpsmall.module.user.serialize.form.AdminAddForm;
import com.xm.cpsmall.module.user.serialize.form.AdminLoginForm;
import com.xm.cpsmall.module.user.service.admin.AdminService;
import com.xm.cpsmall.utils.MD5;
import com.xm.cpsmall.utils.form.ListForm;
import com.xm.cpsmall.utils.lock.LockUtil;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private SuAdminMapper suAdminMapper;

    @Override
    public void add(AdminAddForm adminAddForm) {
        if(StrUtil.isBlank(adminAddForm.getUserName()))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        SuAdminEntity example = new SuAdminEntity();
        example.setUserName(adminAddForm.getUserName());
        LockUtil.lock(redisLockRegistry.obtain(adminAddForm.getUserName()),() -> {
            int count = suAdminMapper.selectCount(example);
            if(count > 0)
                throw new GlobleException(MsgEnum.DATA_ALREADY_EXISTS,"该用户已存在");
            SuAdminEntity adminEntity = new SuAdminEntity();
            adminAddForm.setUserName(adminAddForm.getUserName());
            adminAddForm.setPassword(MD5.md5(adminAddForm.getPassword(),""));
            adminAddForm.setHeadImg(adminAddForm.getHeadImg());
            suAdminMapper.insertSelective(adminEntity);
        });
    }

    @Override
    public SuAdminEntity login(AdminLoginForm adminLoginForm) {
        if(StrUtil.hasBlank(adminLoginForm.getUserName(),adminLoginForm.getPassword()))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        SuAdminEntity example = new SuAdminEntity();
        example.setUserName(adminLoginForm.getUserName());
        example.setPassword(MD5.md5(adminLoginForm.getPassword(),""));
        PageHelper.startPage(1,1).count(false);
        SuAdminEntity suAdminEntity = suAdminMapper.selectOne(example);
//        if(suAdminEntity == null)
//            throw new GlobleException(MsgEnum.SYSTEM_LOGIN_ERROR);
        return suAdminEntity;
    }

    @Override
    public void del(String userName) {
        if(StrUtil.isBlank(userName))
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        SuAdminEntity example = new SuAdminEntity();
        example.setUserName(userName);
        int count = suAdminMapper.delete(example);
        if(count <= 0)
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS,"用户不存在");
    }

    @Override
    public PageBean<SuAdminEntity> get(ListForm listForm) {
        PageHelper.startPage(listForm.getPageNum(),listForm.getPageSize());
        OrderByHelper.orderBy("create_time desc");
        return (PageBean<SuAdminEntity>)suAdminMapper.selectAll();
    }


}
