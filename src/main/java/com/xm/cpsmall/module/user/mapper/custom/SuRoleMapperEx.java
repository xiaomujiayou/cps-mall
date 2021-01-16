package com.xm.cpsmall.module.user.mapper.custom;

import com.xm.cpsmall.module.user.serialize.entity.SuRoleEntity;
import com.xm.cpsmall.module.user.serialize.ex.RolePermissionEx;
import com.xm.cpsmall.utils.MyMapper;

import java.util.List;

public interface SuRoleMapperEx extends MyMapper<SuRoleEntity> {

    /**
     * 获取用户角色权限
     * @param userId
     * @return
     */
    List<RolePermissionEx> getUserRoleEx(Integer userId);

}
