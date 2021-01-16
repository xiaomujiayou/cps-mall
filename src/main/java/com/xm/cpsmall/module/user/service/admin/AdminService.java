package com.xm.cpsmall.module.user.service.admin;

import com.xm.cpsmall.module.user.serialize.entity.SuAdminEntity;
import com.xm.cpsmall.module.user.serialize.form.AdminAddForm;
import com.xm.cpsmall.module.user.serialize.form.AdminLoginForm;
import com.xm.cpsmall.utils.form.ListForm;
import com.xm.cpsmall.utils.mybatis.PageBean;

public interface AdminService {

    /**
     * 添加一个管理员
     * @param adminAddForm
     */
    public void add(AdminAddForm adminAddForm);

    /**
     * 管理员登录
     */
    public SuAdminEntity login(AdminLoginForm adminLoginForm);

    /**
     * 删除管理员
     */
    public void del(String userName);

    /**
     * 查询管理员
     */
    public PageBean<SuAdminEntity> get(ListForm listForm);
}
